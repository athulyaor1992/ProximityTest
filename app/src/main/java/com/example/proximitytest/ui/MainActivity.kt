package com.example.proximitytest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myclassifieds.adapter.AqiAdapter
import com.example.proximitytest.viewmodel.AqiViewModel
import com.example.proximitytest.R
import com.example.proximitytest.data.Aqi
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import androidx.lifecycle.Observer
import com.example.proximitytest.util.Status
import com.example.proximitytest.util.snackbar
import org.json.JSONException
import java.util.*


class MainActivity : AppCompatActivity(),AqiAdapter.CellClickListener {

    private var client: OkHttpClient? = null
    private lateinit var adapter: AqiAdapter
    private lateinit var viewModel: AqiViewModel

    private inner class EchoWebSocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                output(text)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            try {
                output("Closing : $code / $reason")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            try {
                output("Error : " + t.message)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }

    private fun output(text: String) {
        runOnUiThread {
            val view = window.decorView.rootView
            viewModel.setupObserver(text).observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            aqiView.visibility = View.VISIBLE
                            progress_bar.visibility = View.GONE
                            resource.data?.let { users -> retrieveList(resource.data) }
                        }
                        Status.ERROR -> {
                            aqiView.visibility = View.VISIBLE
                            progress_bar.visibility = View.GONE
                            view.snackbar(this, resource.message.toString())
                        }
                        Status.LOADING -> {
                            aqiView.visibility = View.VISIBLE
                            progress_bar.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }


    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client = OkHttpClient()

        viewModel = ViewModelProvider(this).get(AqiViewModel::class.java)

        setupUI()
        setupWebSocket()
    }


    private fun setupUI() {
        aqiView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = AqiAdapter(arrayListOf(),this)
        aqiView.adapter = adapter
    }

    private fun setupWebSocket() {
        val request = Request.Builder().url("ws://city-ws.herokuapp.com/").build()
        val listener: EchoWebSocketListener = EchoWebSocketListener()
        val ws = client!!.newWebSocket(request, listener)
        client!!.dispatcher().executorService().shutdown()
    }

    private fun retrieveList(arrayList: ArrayList<Aqi>) {

        adapter.apply {
            addUsers(arrayList)
            notifyDataSetChanged()
        }
    }

    override fun onCellClickListener(data: Aqi) {

        val intent = Intent (this@MainActivity, ChartActivity::class.java)
        intent.putExtra("AQICity", data.city)
        startActivity(intent)
    }
}




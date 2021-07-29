package com.example.myclassifieds.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proximitytest.R
import com.example.proximitytest.data.Aqi
import kotlinx.android.synthetic.main.aqi_item.view.*


class AqiAdapter(
    private val users: ArrayList<Aqi?>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<AqiAdapter.DataViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(data: Aqi)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: Aqi) {
            itemView.apply {

                city.text = user.city
                aqi.text = String.format("%.2f", user.aqi.toDouble())

                var index  =(user.aqi.toDouble()).toInt()

                if(index in 0..50){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.Good))
                }
                if(index in 51..100){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.Satisfactory))
                }
                if(index in 101..200){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.Moderate))
                }
                if(index in 201..300){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.Poor))
                }
                if(index in 301..400){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.VeryPoor))
                }
                if(index in 401..500){
                    aqiHead.setBackgroundColor(resources.getColor(R.color.Severe))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.aqi_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position]!!)

        val data = users[position]

        holder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(data!!)
        }

    }


    fun addUsers(users: List<Aqi>) {
        this.users.apply {
            clear()
            addAll(users)
        }

    }
}
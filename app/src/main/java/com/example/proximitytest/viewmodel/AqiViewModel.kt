package com.example.proximitytest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.proximitytest.data.Aqi
import com.example.proximitytest.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException

class AqiViewModel(
): ViewModel(){


    fun setupObserver(txt: String)= liveData(Dispatchers.IO){

        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = output(txt)))
        } catch (exception: Exception) {
            android.util.Log.e("Error",exception.toString());
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

     fun output(txt: String): ArrayList<Aqi>? {

        var jsonArray: JSONArray?
        try {
            jsonArray = JSONArray(txt)
            val listdata =

                Gson().fromJson<ArrayList<Aqi>>(
                    jsonArray.toString(),
                    object : TypeToken<ArrayList<Aqi?>?>() {}.type
                )

            return listdata
        } catch (e: JSONException) {
            e.printStackTrace()
        }

         return null
    }

}

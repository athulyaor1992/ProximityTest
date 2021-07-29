package com.example.proximitytest.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Aqi(

    val city: String,
    val aqi: String,
):Parcelable

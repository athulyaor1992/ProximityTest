package com.example.proximitytest.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.proximitytest.R
import com.google.android.material.snackbar.Snackbar


fun View.snackbar(context: Context, message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.
        setAction("Ok") {
            snackbar.dismiss()
        }.view.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
    }.
    show()

}



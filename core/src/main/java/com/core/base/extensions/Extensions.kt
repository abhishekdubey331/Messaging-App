package com.core.base.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat


fun Context.checkSmsReadPermission(): Boolean {
    val checkPermission: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
    return checkPermission == PackageManager.PERMISSION_GRANTED
}

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}


fun View.gone() {
    visibility = View.GONE
}

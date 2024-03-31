package com.rcompany.rchat.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}

fun time2HumanReadable(dateTimeStr: String): String {
    val regex = "^(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}).*".toRegex()
    val matchResult = regex.find(dateTimeStr) ?: throw IllegalArgumentException("Invalid date format")
    val year = matchResult.groupValues[1]
    val month = matchResult.groupValues[2].padStart(2, '0')
    val day = matchResult.groupValues[3].padStart(2, '0')
    val hours = matchResult.groupValues[4].padStart(2, '0')
    val minutes = matchResult.groupValues[5].padStart(2, '0')
    return "$day:$month:$year $hours:$minutes"
}
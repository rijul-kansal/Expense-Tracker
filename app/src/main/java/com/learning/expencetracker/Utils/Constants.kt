package com.learning.expencetracker.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Constants {

    const val  EMAIL = "email"
    const val  PASSWORD = "password"
    const val TOKEN = "token"
    const val BOOKID="bookId"
    const val MEMBERS="members"
    const val BOOKS_DATA="books_data"
    const val PAYMENT_TYPE="paymentType"
    const val BOOKNAME = "bookname"
//    val baseUrl = "https://us-central1-all-backend-fd5c7.cloudfunctions.net"
    val baseUrl = "http://13.201.49.175:3200"
    val baseUrl1 = "https://expense-tracker-backend-2qzh.onrender.com"


    var client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }

        }
        return false
    }
    fun parseErrorMessage(errorBody: String?): String? {
        errorBody?.let {
            try {
                val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                return jsonObject?.get("message")?.asString
            } catch (e: Exception) {
                Log.e("rk", "Error parsing error message: ${e.message}")
            }
        }
        return null
    }
}
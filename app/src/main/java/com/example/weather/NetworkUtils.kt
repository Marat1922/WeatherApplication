package com.example.weather

import android.net.Uri
import android.util.Log
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import java.util.*

internal class NetworkUtils {
    private val WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=9b73a44765724da152bc4a0bdb3c1a1f&lang=ru&units=metric" //Не забудьте ввести свой APPID после '='

    companion object {
        private const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather"
        private const val CITY = "q"
        private const val APPID = "APPID"
        private const val LANG = "lang"
        private const val UNITS = "units"
        fun generateURL(city: String?): URL? {
            Timber.d("NetworkUtils generateURL")
            val builtUri = Uri.parse(WEATHER_API_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(CITY, city)
                    .appendQueryParameter(APPID,
                            "9b73a44765724da152bc4a0bdb3c1a1f")
                    .appendQueryParameter(LANG, "ru")
                    .appendQueryParameter(UNITS, "metric")
                    .build()
            var url: URL? = null
            try {
                url = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return url
        }

        @Throws(IOException::class)
        fun getResponseFromURL(url: URL?): String? {
            Timber.d("NetworkUtils getResponseFromURL ")
            val urlConnection = url?.openConnection() as HttpURLConnection
            return try {
                val `in` = urlConnection.inputStream
                val scanner = Scanner(`in`)
                scanner.useDelimiter("//A")
                val hasInput = scanner.hasNext()
                if (hasInput) {
                    scanner.next()
                } else {
                    null
                }
            } catch (e: UnknownHostException) {
                null
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}
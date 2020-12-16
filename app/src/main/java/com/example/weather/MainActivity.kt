package com.example.weather

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var editTextCity: EditText? = null
    private var textViewWeather: TextView? = null
    private var b_search: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextCity = findViewById(R.id.et_weather)
        textViewWeather = findViewById(R.id.tv_result)
        b_search = findViewById(R.id.b_search)
        val onClickListener = View.OnClickListener {
            val generatedURL = NetworkUtils.generateURL(editTextCity?.getText().toString())
            DownloadWeatherTask().execute(generatedURL)
        }
        b_search?.setOnClickListener(onClickListener)
    }

    internal inner class DownloadWeatherTask : AsyncTask<URL?, Void?, String?>() {

        override fun doInBackground(vararg params: URL?): String? {
            var response: String? = null
            try {
                response = NetworkUtils.getResponseFromURL(params[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)
            var city: String? = null
            var temp: String? = null
            var description: String? = null
            if (response != null && response != "") {
                try {
                    val jsonObject = JSONObject(response)
                    city = jsonObject.getString("name")
                    temp = jsonObject.getJSONObject("main").getString("temp")
                    description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val weather = String.format("%s\nТемпература: %s\nНа улице: %s", city, temp, description)
                textViewWeather!!.text = weather
            } else {
                Toast.makeText(applicationContext, "fff", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
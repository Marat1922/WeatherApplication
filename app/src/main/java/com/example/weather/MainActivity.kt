package com.example.weather

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var editTextCity: EditText
    lateinit var textViewWeather: TextView
    lateinit var btnSearch: Button
    lateinit var rvListCity: RecyclerView
    lateinit var adapter: CityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyTag", "MainActivity onCreate ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextCity = findViewById(R.id.et_weather)
        textViewWeather = findViewById(R.id.tv_result)
        btnSearch = findViewById(R.id.b_search)
        rvListCity = findViewById(R.id.rv_list_city)
        val onClickListener = View.OnClickListener {
            val generatedURL = NetworkUtils.generateURL(editTextCity.getText().toString())
            DownloadWeatherTask(this).execute(generatedURL)
        }
        btnSearch.setOnClickListener(onClickListener)

        val cityNameList = resources.getStringArray(R.array.city_name).map { nameFromArray ->
            City(name = nameFromArray)
        }
        adapter = CityAdapter(cityNameList, object : CityAdapter.OnItemClickListener {
            override fun onItemClick(cityName: String) {
                Log.d("MyTag", "MainActivity OnItemClick ")
                val gen2 = NetworkUtils.generateURL(cityName)
                DownloadWeatherTask(this@MainActivity).execute(gen2)
                Log.i("MainActivity", "blablbabla")
            }
        })
        rvListCity.layoutManager = LinearLayoutManager(this)
        rvListCity.adapter = adapter
    }

    internal class DownloadWeatherTask(context: MainActivity) : AsyncTask<URL?, Void?, String?>() {

        private val activityReference: WeakReference<MainActivity> = WeakReference(context)

        override fun doInBackground(vararg params: URL?): String? {
            Log.d("MyTag", "MainActivity.DownloadWeatherTask doInBackground ")
            var response: String? = null
            try {
                response = NetworkUtils.getResponseFromURL(params[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(response: String?) {
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            Log.d("MyTag", "MainActivity.DownloadWeatherTask onPostExecute ")
            super.onPostExecute(response)
            var city: String? = null
            var description: String? = null
            var temp: String? = null
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
                val temper = String.format(" %s", temp)
                activity.textViewWeather.text = weather
                Log.d("MyTag", "blablbabla2 $temp")
                activity.adapter.onTemperatureArrived(city!!, temper)
            } else {
                Toast.makeText(activity, "fff", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
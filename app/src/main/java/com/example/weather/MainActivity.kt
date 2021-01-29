package com.example.weather

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var editTextCity: EditText
    lateinit var textViewWeather: TextView
    lateinit var btnSearch: Button
    lateinit var rvListCity: RecyclerView
    lateinit var adapter: CityAdapter
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val RECORD_REQUEST_CODE: Int = 101
        const val KEY_CITY: String = "City"
        const val DEFAULT_CITY: String = "Бугульма"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("метод onCreate")
        Timber.d("MainActivity onCreate ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextCity = findViewById(R.id.et_weather)
        textViewWeather = findViewById(R.id.tv_result)
        btnSearch = findViewById(R.id.b_search)
        rvListCity = findViewById(R.id.rv_list_city)

        btnSearch.setOnClickListener {
            loadWeather(editTextCity.text.toString(), false)
        }

        val cityNameList = resources.getStringArray(R.array.city_name).map { nameFromArray ->
            City(name = nameFromArray)
        }

        loadWeather(взятьГородИзШары(), false)
        val permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION),
                    RECORD_REQUEST_CODE)
        } else if (permission == PackageManager.PERMISSION_GRANTED) {

            получитьГород()
        }

        adapter = CityAdapter(cityNameList, object : CityAdapter.OnItemClickListener {
            override fun onItemClick(cityName: String) {
                Timber.d("MainActivity OnItemClick ")
                loadWeather(cityName, true)
            }
        })
        rvListCity.layoutManager = LinearLayoutManager(this)
        rvListCity.adapter = adapter

    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun loadWeather(city: String, isFromRecycler: Boolean) {
        Timber.d("метод loadWeather")
        DownloadWeatherTask(this, isFromRecycler).execute(NetworkUtils.generateURL(city))
    }

    private fun получитьГород() {
        Timber.d("метод получаетГород")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val adress: List<Address> = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1)
                val result = adress[0].locality
                сохранитьГородВШару(result)
                loadWeather(result, false)
            }
        }
    }

    fun сохранитьГородВШару(cityName: String) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.getString(KEY_CITY, DEFAULT_CITY)
        sharedPreferences.edit().putString(KEY_CITY, cityName).apply()
    }

    fun взятьГородИзШары(): String {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getString(KEY_CITY, DEFAULT_CITY) ?: DEFAULT_CITY
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.d("метод onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_REQUEST_CODE) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == android.Manifest.permission.ACCESS_COARSE_LOCATION) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        получитьГород()
                    }
                }
            }
        }
    }

    internal class DownloadWeatherTask(context: MainActivity, val isFromRecycler: Boolean) : AsyncTask<URL?, Void?, String?>() {

        private val activityReference: WeakReference<MainActivity> = WeakReference(context)

        override fun doInBackground(vararg params: URL?): String? {
            Timber.d("MainActivity.DownloadWeatherTask doInBackground ")
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
            Timber.d("MainActivity.DownloadWeatherTask onPostExecute ")
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
                    Timber.e(e, "an error occurred while parsing the server response")
                }
                if (isFromRecycler) {
                    activity.adapter.onTemperatureArrived(city!!, temp!!)
                } else {
                    activity.textViewWeather.text = activity.getString(R.string.s, city, temp, description)
                }
            } else {
                Toast.makeText(activity, "Город не найден", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.example.weather

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber


class AddCityActivity : AppCompatActivity() {

    lateinit var editTextCity: EditText
    lateinit var textViewAddCity: TextView
    lateinit var listViewSelectedCity: ListView
    lateinit var spinner: Spinner
    lateinit var btnAddCityList: Button
    lateinit var btnAddNewCity: Button
    lateinit var adapter: ArrayAdapter<String>
    lateinit var cityList: ArrayList<City>
    lateinit var database: SQLiteDatabase
    private val cityNames = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        editTextCity = findViewById(R.id.et_name_city)
        textViewAddCity = findViewById(R.id.tv_add_city_list)
        spinner = findViewById(R.id.spinner_list_city)
        listViewSelectedCity = findViewById(R.id.lv_selected_city)
        btnAddCityList = findViewById(R.id.btn_add_city_list)
        btnAddNewCity = findViewById(R.id.btn_add_city)
        val dbHelper: CitiesDBHelper = CitiesDBHelper(this)
        database = dbHelper.writableDatabase
        adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cityNames)
        listViewSelectedCity.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addNewCity(spinner.getItemAtPosition(position).toString())
            }
        }
        btnAddCityList.setOnClickListener {
            if (editTextCity.text.toString() != "") {
                addNewCity(editTextCity.text.toString())
            } else {
                Toast.makeText(this, "Введите правильное название города", Toast.LENGTH_LONG).show()
            }
        }

        btnAddNewCity.setOnClickListener {
            cityList = cityNames.map { City(it, null) } as ArrayList<City>
            addToDataBase(cityList)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun addToDataBase(cityList: ArrayList<City>) {
        cityList.forEach { city ->
            val contentValues = ContentValues()
            contentValues.put(CityContract.CitiesEntry.COLUMN_CITY, city.name)
            database.insert(CityContract.CitiesEntry.TABLE_NAME, null, contentValues)
        }
    }

    fun addNewCity(newCity: String) {
        if (!cityNames.contains(newCity)) {
            cityNames.add(newCity)
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Такой город уже есть", Toast.LENGTH_LONG).show()
        }
    }
}
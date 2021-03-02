package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class AddCityActivity : AppCompatActivity() {

    lateinit var editTextCity: EditText
    lateinit var textViewAddCity: TextView
    lateinit var listViewSelectedCity: ListView
    lateinit var spinner: Spinner
    lateinit var btnAddCityList: Button
    lateinit var btnAddNewCity: Button
    lateinit var cityNames: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        editTextCity = findViewById(R.id.et_name_city)
        textViewAddCity = findViewById(R.id.tv_add_city_list)
        spinner = findViewById(R.id.spinner_list_city)
        listViewSelectedCity = findViewById(R.id.lv_selected_city)
        btnAddCityList = findViewById(R.id.btn_add_city_list)
        btnAddNewCity = findViewById(R.id.btn_add_city)
        cityNames = ArrayList()
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
            MainActivity.cityList = cityNames.map { City(it, null) } as ArrayList<City>
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun addNewCity(newCity: String) {
        if (!cityNames.contains(newCity)) {
            cityNames.add(newCity)
        } else {
            Toast.makeText(this, "Такой город уже есть", Toast.LENGTH_LONG).show()
        }
        adapter.notifyDataSetChanged()
    }
}
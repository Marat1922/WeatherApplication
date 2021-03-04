//package com.example.weather
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.Query
//
//@Dao
//interface CityDao {
//
//    @Query("SELECT * FROM city")
//    fun getAllCity(): List<City>
//
//    @Insert
//    fun insertCity(city: City)
//
//    @Delete
//    fun deleteCity(city: City)
//
//    @Query("DELETE FROM city")
//    fun deleteAllCity()
//}
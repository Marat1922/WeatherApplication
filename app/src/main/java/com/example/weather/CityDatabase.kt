//package com.example.weather
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [City::class], version = 1, exportSchema =  false)
//abstract class CityDatabase : RoomDatabase() {
//
//    abstract fun cityDao(): CityDao
//
//    companion object {
//        private var database: CityDatabase? = null
//        private const val DB_NAME: String = "city2"
//        private val LOCK = Object()
//
//        fun getInstance(context: Context):CityDatabase {
//            synchronized(LOCK) {
//                if (database == null) {
//                    database = Room.databaseBuilder(context, CityDatabase::class.java, DB_NAME)
//                            .allowMainThreadQueries()
//                            .build()
//                }
//            }
//                return database!!
//        }
//    }
//}
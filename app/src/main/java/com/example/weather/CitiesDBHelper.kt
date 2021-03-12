package com.example.weather

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CitiesDBHelper: SQLiteOpenHelper {

    constructor(context: Context?) : super(context, DB_NAME, null, DB_VERSION)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CityContract.CitiesEntry.CREATE_COMMAND)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(CityContract.CitiesEntry.DROP_COMMAND)
        onCreate(db)
    }

    companion object{
        const val DB_NAME: String = "cities.db"
        const val DB_VERSION: Int = 1
    }
}
package com.example.weather

import android.provider.BaseColumns
import android.provider.BaseColumns._ID

class CityContract {

    class CitiesEntry : BaseColumns {
        companion object {
            const val TABLE_NAME: String = "weather"
            const val COLUMN_CITY: String = "city"
            const val COLUMN_TEMPER: String = "temper"
            const val COLUMN_IMAGE: String = "image"

            const val TYPE_TEXT: String = "TEXT"
            const val TYPE_INTEGER: String = "INTEGER"
            const val TYPE_BLOB: String = "BLOB"

            const val CREATE_COMMAND: String = "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
                    "($_ID $TYPE_INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_CITY $TYPE_TEXT, $COLUMN_TEMPER $TYPE_TEXT, $COLUMN_IMAGE $TYPE_BLOB)"

            const val DROP_COMMAND: String = "DROP TABLE EXISTS $TABLE_NAME"
        }
    }

}
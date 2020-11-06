package com.example.rxsamplesearch.database


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [(Country::class)], version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao

    companion object {
        var TEST_MODE = false
        private var INSTANCE: CountryDatabase? = null

        fun getInstance(context: Context): CountryDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (TEST_MODE) {
                        INSTANCE = Room.inMemoryDatabaseBuilder(
                            context,
                            CountryDatabase::class.java
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                    INSTANCE = Room.databaseBuilder(
                        context,
                        CountryDatabase::class.java, "country.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as CountryDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}

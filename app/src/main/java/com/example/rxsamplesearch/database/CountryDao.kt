package com.example.rxsamplesearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries WHERE name LIKE :name")
    fun findCountry(name: String): List<Country>

    @Insert(onConflict = IGNORE)
    fun insertAll(countries: List<Country>): List<Long>

    @Insert(onConflict = IGNORE)
    fun insert(country: Country)
}
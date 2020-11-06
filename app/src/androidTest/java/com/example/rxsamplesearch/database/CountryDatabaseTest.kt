package com.example.rxsamplesearch.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CountryDatabaseTest {

    private lateinit var countryDao: CountryDao
    private lateinit var db: CountryDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CountryDatabase::class.java
        ).build()
        countryDao = db.countryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val country = Country(1, "Bharath")
        countryDao.insert(country)
        val byName = countryDao.findCountry("Bharath")
        Assert.assertThat(byName.get(0), IsEqual.equalTo(country))

    }
}
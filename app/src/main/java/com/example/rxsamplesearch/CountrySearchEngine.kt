package com.example.rxsamplesearch

import android.content.Context
import android.util.Log
import com.example.rxsamplesearch.database.Country
import com.example.rxsamplesearch.database.CountryDatabase


class CountrySearchEngine(private val context: Context) {

    fun search(query: String): List<Country>? {
        Thread.sleep(2000)
        Log.d("Searching", "Searching for $query")
        return CountryDatabase.getInstance(context).countryDao().findCountry("%$query%")
    }

}
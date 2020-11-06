package com.example.rxsamplesearch.ui

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rxsamplesearch.adapter.CountryAdapter
import com.example.rxsamplesearch.CountrySearchEngine
import com.example.rxsamplesearch.R
import com.example.rxsamplesearch.database.Country
import com.example.rxsamplesearch.database.CountryDatabase
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_countries.*


abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var countrySearchEngine: CountrySearchEngine
    private val cheeseAdapter = CountryAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        list.adapter = cheeseAdapter

        countrySearchEngine = CountrySearchEngine(this@BaseActivity)

        val initialLoadDisposable = loadInitialData(this@BaseActivity).subscribe()
        compositeDisposable.add(initialLoadDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        CountryDatabase.destroyInstance()
        compositeDisposable.clear()
    }

    protected fun showProgress() {
        progressBar.visibility = VISIBLE
    }

    protected fun hideProgress() {
        progressBar.visibility = GONE
    }

    protected fun showResult(result: List<Country>) {
        if (result.isEmpty()) {
            Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show()
        }
        cheeseAdapter.countries = result
    }

    private fun loadInitialData(context: Context): Flowable<List<Long>> {
        return Maybe.fromAction<List<Long>> {

            val database = CountryDatabase.getInstance(context = context).countryDao()
            val countries = resources.getStringArray(R.array.countries_array)

            val cheeseList = arrayListOf<Country>()
            for (cheese in countries) {
                cheeseList.add(Country(name = cheese))
            }
            database.insertAll(cheeseList)

        }.toFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
            }
            .doOnError {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
    }
}
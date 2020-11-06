package com.example.rxsamplesearch.ui

import android.text.Editable
import android.text.TextWatcher
import com.example.rxsamplesearch.database.Country
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_countries.*
import java.util.concurrent.TimeUnit

class CountryActivity : BaseActivity() {

    private lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()

        val buttonClickStream = createButtonClickObservable()
            .toFlowable(BackpressureStrategy.LATEST) // 1

        val textChangeStream = createTextChangeObservable()
            .toFlowable(BackpressureStrategy.BUFFER) // 2

        val searchTextFlowable = Flowable.merge<String>(buttonClickStream, textChangeStream)

        disposable = searchTextFlowable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showProgress() }
            .observeOn(Schedulers.io())
            .map { countrySearchEngine.search(it) as List<Country> }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                hideProgress()
                showResult(it)
            }
    }

    @Override
    override fun onStop() {
        super.onStop()
        // 1
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }


    private fun createButtonClickObservable(): Observable<String> {

        return Observable.create<String> { emitter ->

            searchButton.setOnClickListener {
                emitter.onNext(queryEditText.text.toString())
            }

            emitter.setCancellable {
                searchButton.setOnClickListener(null)
            }
        }
    }


    private fun createTextChangeObservable(): Observable<String> {

        val textChangeObservable = Observable.create<String> { emitter ->

            val textWatcher = object : TextWatcher {

                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

            }

            queryEditText.addTextChangedListener(textWatcher)

            emitter.setCancellable {
                queryEditText.removeTextChangedListener(textWatcher)
            }
        }

        return textChangeObservable
            .filter { it.length >= 2 }
            .debounce(1000, TimeUnit.MILLISECONDS)

    }

}
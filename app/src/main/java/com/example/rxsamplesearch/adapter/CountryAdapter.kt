package com.example.rxsamplesearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rxsamplesearch.R
import com.example.rxsamplesearch.database.Country
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.list_item.view.*

class CountryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var compositeDisposable = CompositeDisposable()

    var countries: List<Country> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = countries.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val country = countries[position]
        holder.itemView.textView.text = country.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
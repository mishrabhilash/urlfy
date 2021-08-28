package com.abhilashmishra.urlfy2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by abhilash.mishra on 6/15/17.
 */

class UrlListAdapter(private val list: List<LinkData>?, private val context: Context,
                     val delete: (index: Int) -> Unit, val info: (indext: Int) -> Unit) : RecyclerView.Adapter<UrlListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UrlListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_url_list, viewGroup, false)
        return UrlListViewHolder(view, context)
    }

    override fun onBindViewHolder(urlListViewHolder: UrlListViewHolder, i: Int) {
        urlListViewHolder.initView(list?.get(i), delete, info)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}

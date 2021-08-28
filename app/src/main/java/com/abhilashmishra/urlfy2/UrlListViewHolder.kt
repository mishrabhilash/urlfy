package com.abhilashmishra.urlfy2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by abhilash.mishra on 6/15/17.
 */

class UrlListViewHolder(private val mItemView: View, private val context: Context) : RecyclerView.ViewHolder(mItemView) {

    private var textTitle:TextView = mItemView.findViewById(R.id.text_title)
    private var textLink:TextView = mItemView.findViewById(R.id._text_link)
    private var buttonDelete:View = mItemView.findViewById(R.id.button_delete)
    private var buttonInfo:View = mItemView.findViewById(R.id.button_info)
    fun initView(linkData: LinkData?, delete: (index: Int) -> Unit, info: (indext: Int) -> Unit) {
        textTitle
        textTitle.text = linkData?.linkTitle
        textLink.text = linkData?.linkUrl
        buttonDelete.setOnClickListener { delete.invoke(adapterPosition) }
        buttonInfo.setOnClickListener { info.invoke(adapterPosition) }
        mItemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkData?.linkUrl))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, R.string.no_app_found_text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

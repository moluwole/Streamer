package com.example.yung.streamer

import android.app.Application
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.squareup.picasso.Picasso
import java.security.AccessController

/**
 * Created by yung on 7/9/17.
 */

class DataAdapter constructor(mList: List<SearchResult>?) : RecyclerView.Adapter<DataAdapter.DataViewAdapter>() {

    var mList: List<SearchResult>? = null
    var app_context: Context? = null

    init {
        this.mList = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewAdapter {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_list, parent, false)
        val vh = DataViewAdapter(view)

        app_context = view.context
        return vh
    }

    override fun onBindViewHolder(holder: DataViewAdapter, position: Int) {
        var video_data = mList?.get(position)
        var rId = video_data?.id

        holder.video_title.text = video_data?.snippet?.title

        //download thumbnail and load into ImageView
        var video_id = rId?.videoId
        var thumbnail_url = "http://img.youtube.com/vi/$video_id/default.jpg"

        Picasso.with(app_context).load(thumbnail_url).into(holder.video_thumbnail)


    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    inner class DataViewAdapter(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        val video_title = layoutView.findViewById(R.id.video_title) as TextView
        val video_thumbnail = layoutView.findViewById(R.id.video_thumbnail) as ImageView
    }
}
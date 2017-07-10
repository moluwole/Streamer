package com.example.yung.streamer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video

/**
 * Created by yung on 7/9/17.
 */

open class VideoList(video_author: String, video_title: String, video_date: String, video_thumbnail_url: String)

class DataAdapter : RecyclerView.Adapter<DataAdapter.DataViewAdapter>() {

    var mList: List<VideoList>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewAdapter {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_list, parent, false)
        val vh = DataViewAdapter(view)
        return vh
    }

    override fun onBindViewHolder(holder: DataViewAdapter, position: Int) {
        var myclass: VideoList? = mList?.get(position)
    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    inner class DataViewAdapter(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        val video_title = layoutView.findViewById(R.id.video_title) as TextView
        val video_author = layoutView.findViewById(R.id.video_author) as TextView
        val video_date = layoutView.findViewById(R.id.video_date) as TextView
        val video_thumbnail = layoutView.findViewById(R.id.video_thumbnail) as ImageView
    }
}
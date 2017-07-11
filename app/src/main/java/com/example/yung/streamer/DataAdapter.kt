package com.example.yung.streamer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.SearchResult
import com.squareup.picasso.Picasso

/**
 * Created by yung on 7/9/17.
 */

class DataAdapter constructor(mList: List<SearchResult>?, frag_manager: FragmentManager?) : RecyclerView.Adapter<DataAdapter.DataViewAdapter>() {

    var mList: List<SearchResult>? = null
    var frag_manager: FragmentManager? = null
    var rootView: View? = null
    var app_context: Context? = null

    init {
        this.mList = mList
        this.frag_manager = frag_manager
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewAdapter {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_list, parent, false)
        rootView = view
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

        holder.video_title.setOnClickListener {
            var extra_bundle: Bundle = Bundle()
            extra_bundle.putString("Video ID", video_id)

            var intent = Intent(app_context as Home, Play::class.java)
            intent.putExtra("Video ID", video_id)

            app_context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    class DataViewAdapter(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        var video_title = layoutView.findViewById(R.id.video_title) as TextView
        val video_thumbnail = layoutView.findViewById(R.id.video_thumbnail) as ImageView
    }
}
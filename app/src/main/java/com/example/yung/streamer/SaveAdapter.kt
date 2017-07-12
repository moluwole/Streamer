package com.example.yung.streamer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by yung on 7/11/17.
 */
class SaveAdapter constructor(mList: ArrayList<String>?) : RecyclerView.Adapter<SaveAdapter.SaveView>() {

    var mList: ArrayList<String>? = null

    init {
        this.mList = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SaveView {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.saved_video_list, parent, false)
        val vh = SaveView(view)

        return vh
    }

    override fun onBindViewHolder(holder: SaveView, position: Int) {
        holder.saved_video.text = mList?.get(position)
    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    class SaveView(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        var saved_video = layoutView.findViewById(R.id.saved_videos_title) as TextView
    }
}
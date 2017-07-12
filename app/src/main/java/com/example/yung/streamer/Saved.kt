package com.example.yung.streamer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File
import android.R.attr.path
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log


class Saved : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    var mFiles: ArrayList<String>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView = inflater?.inflate(R.layout.fragment_saved, container, false)
        var file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer")
        Log.d("Files", "Path: " + file)
        val directory = file
        val files = directory.listFiles()
//        Log.d("Files", "Size: " + files.size)
        if (files != null) {
            for (i in files.indices) {
                mFiles?.add(files[i].name)
                Log.d("Files", "FileName:" + files[i].name)
            }

            Log.d("Number of Files", mFiles?.size.toString())
        }

        var saved_list = rootView?.findViewById(R.id.saved_videos_list) as RecyclerView
        saved_list.adapter = null
        saved_list.setHasFixedSize(true)
        var mLayoutManager = LinearLayoutManager(context)
        saved_list.layoutManager = mLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
                saved_list.context,
                mLayoutManager.orientation)
        saved_list.addItemDecoration(dividerItemDecoration)

        // Specify an adapter

        var mAdapter = SaveAdapter(mFiles)
        saved_list.adapter = mAdapter
        // Inflate the layout for this fragment
        return rootView
    }

}// Required empty public constructor

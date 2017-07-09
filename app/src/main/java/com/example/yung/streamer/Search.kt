package com.example.yung.streamer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootview = inflater?.inflate(R.layout.fragment_search, container, false)
        val button_search = rootview?.findViewById(R.id.button_search) as Button

        button_search.setOnClickListener { getYoutubeVid() }

        // Inflate the layout for this fragment
        return rootview


        //    button_search.setOnClickListener{ getYoutubeVid() }
    }

    //function to grab Videos from Youtube based on keyword search
    fun getYoutubeVid() {
        TODO()
    }

}// Required empty public constructor

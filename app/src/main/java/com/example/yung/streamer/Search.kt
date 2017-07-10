package com.example.yung.streamer


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.auth.api.Auth
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {

    //Key values
    val youtube_api_key = "AIzaSyB0zT7_Jl14161Y5ENYjPVPdxxOk56TKMc"
    val video_limit: Long = 25
    val LOG_ERROR = "Error"
    val LOG_MESSAGE = "Message"
    //xvar youtube: YouTube = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootview = inflater?.inflate(R.layout.fragment_search, container, false)
        val button_search = rootview?.findViewById(R.id.button_search) as Button
        var search_query = (rootview.findViewById(R.id.edit_search) as EditText).text.toString()

        button_search.setOnClickListener { getYoutubeVid(search_query, rootview) }

        // Inflate the layout for this fragment
        return rootview


        //    button_search.setOnClickListener{ getYoutubeVid() }
    }

    //function to grab Videos from Youtube based on keyword search
    fun getYoutubeVid(search_query: String, display_view: View) {
        // TODO()
        if (TextUtils.isEmpty(search_query)) {
            Snackbar.make(display_view, "Enter a Search Parameter", Snackbar.LENGTH_LONG).show()
        } else {
            var properties = Properties()
            try {
                var input = Search::class.java.getResourceAsStream("/" + youtube_api_key)
                properties.load(input)

                var youtube = YouTube.Builder(NetHttpTransport(), JacksonFactory(), HttpRequestInitializer {

                }).setApplicationName("youtube-cmdline-search-sample").build()

                //youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("youtube-cmdline-search-sample").build()
                var search = youtube.search().list("id,snippet")
                var api_key = properties.getProperty("youtube.apikey")
                search.key = api_key
                search.q = search_query
                search.type = "video"

                search.fields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"
                search.maxResults = video_limit

                var search_response = search.execute()
                var search_list = search_response.items
            } catch (e: Exception) {
                Log.d(LOG_ERROR, "An Exception Occurred " + e.message)
            }


        }

    }

}// Required empty public constructor

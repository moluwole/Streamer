package com.example.yung.streamer


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.android.gms.auth.api.Auth
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    fun onItemclick(rootview: View) {
        val video_list = rootview.findViewById(R.id.list_videos) as RecyclerView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun checkValue(search_query: String): Boolean {
        return TextUtils.isEmpty(search_query)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootview = inflater?.inflate(R.layout.fragment_search, container, false)
        val button_search = rootview?.findViewById(R.id.button_search) as Button
        var search_query = (rootview.findViewById(R.id.edit_search) as EditText).text.toString()

        button_search.setOnClickListener {
            if (checkValue(search_query)) loadVid(context, rootview).execute() ?: Snackbar.make(rootview, "Enter a search parameter", Snackbar.LENGTH_LONG).show()
        }

        // Inflate the layout for this fragment
        return rootview

        //    button_search.setOnClickListener{ getYoutubeVid() }
    }

    //function to grab Videos from Youtube based on keyword search
    class loadVid(context: Context, rootview: View) : AsyncTask<String, String, String>() {

        //Key values
        val youtube_api_key = "AIzaSyB0zT7_Jl14161Y5ENYjPVPdxxOk56TKMc"
        val video_limit: Long = 50
        val LOG_ERROR = "Error"

        var context: Context? = context
        var rootview: View? = rootview
        var search_list: List<SearchResult>? = null
        var search_query: String? = null

        init {
            this.context = context
            this.rootview = rootview
        }

        override fun doInBackground(vararg params: String?): String {
            getYoutubeVid(search_query)
            return ""
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        private fun showProgress(show: Boolean) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.

            val search_container = rootview?.findViewById(R.id.search_container) as LinearLayout
            val search_progress = rootview?.findViewById(R.id.search_progress) as ProgressBar

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

                search_container.visibility = if (show) View.GONE else View.VISIBLE
                search_container.animate()
                        .setDuration(100)
                        .alpha((if (show) 0 else 1).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                search_container.visibility = if (show) View.GONE else View.VISIBLE
                            }
                        })

                search_progress.visibility = if (show) View.VISIBLE else View.GONE
                search_progress.animate()
                        .setDuration(100)
                        .alpha((if (show) 1 else 0).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                search_progress.visibility = if (show) View.VISIBLE else View.GONE
                            }
                        })
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                search_progress.visibility = if (show) View.VISIBLE else View.GONE
                search_container.visibility = if (show) View.GONE else View.VISIBLE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            search_query = (rootview?.findViewById(R.id.edit_search) as EditText).text.toString()
            showProgress(true)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var list_videos = rootview?.findViewById(R.id.list_videos) as RecyclerView

            list_videos.adapter = null
            list_videos.setHasFixedSize(true)
            var mLayoutManager = LinearLayoutManager(context)
            list_videos.layoutManager = mLayoutManager

            val dividerItemDecoration = DividerItemDecoration(
                    list_videos.context,
                    mLayoutManager.orientation)
            list_videos.addItemDecoration(dividerItemDecoration)

            // Specify an adapter
            var mAdapter = DataAdapter(search_list)
            list_videos.adapter = mAdapter
            showProgress(false)
        }

        fun getYoutubeVid(search_query: String?) {

            //var properties = Properties()
            try {
                /*    var input = Search::class.java.getResourceAsStream("/" + youtube_api_key)
                    properties.load(input)
    */
                var youtube = YouTube.Builder(NetHttpTransport(), JacksonFactory(), HttpRequestInitializer {

                }).setApplicationName("Streamer").build()

                //youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("youtube-cmdline-search-sample").build()
                var search = youtube.search().list("id,snippet")
                search.key = youtube_api_key
                search.q = search_query
                search.type = "video"

                search.fields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"
                search.maxResults = video_limit

                var search_response = search.execute()
                Log.d("Items", search.toString())
                search_list = search_response.items

                // var list_adapter = DataAdapter(search_list)

            } catch (e: Exception) {
                e.printStackTrace()
                var msg = e
                Log.d(LOG_ERROR, "An Exception Occurred: $msg")
            }


        }

    }


}// Required empty public constructor

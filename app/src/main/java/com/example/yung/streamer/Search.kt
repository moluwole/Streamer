package com.example.yung.streamer


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class Search : Fragment() {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun checkValue(search_query: String): Boolean {
        return TextUtils.isEmpty(search_query)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootview = inflater?.inflate(R.layout.fragment_search, container, false)
        val frag_manager = fragmentManager
        val button_search = rootview?.findViewById(R.id.button_search) as Button
        val search_query = (rootview.findViewById(R.id.edit_search) as EditText).text.toString()

        button_search.setOnClickListener {
            try {
                val imm = rootview.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow((rootview.context as Activity).window
                        .currentFocus.windowToken, 0)
            } catch (e: Exception) {

            }
            if (checkValue(search_query)) loadVid(context, rootview, frag_manager).execute() ?: Snackbar.make(rootview, "Enter a search parameter", Snackbar.LENGTH_LONG).show()
        }

        // Inflate the layout for this fragment
        return rootview
    }

    //function to grab Videos from Youtube based on keyword search
    class loadVid(context: Context, rootview: View, frag_manager: FragmentManager) : AsyncTask<String, String, String>() {

        //Key values
        val youtube_api_key = "AIzaSyB0zT7_Jl14161Y5ENYjPVPdxxOk56TKMc"
        val video_limit: Long = 50
        val LOG_ERROR = "Error"

        @SuppressLint("StaticFieldLeak")
        var context: Context? = context
        @SuppressLint("StaticFieldLeak")
        var rootview: View? = rootview
        var frag_manager: FragmentManager? = frag_manager
        var search_list: List<SearchResult>? = null
        var search_query: String? = null

        init {
            this.context = context
            this.rootview = rootview
            this.frag_manager = frag_manager
        }

        override fun doInBackground(vararg params: String?): String {
            getYoutubeVid(search_query, rootview!!)
            return ""
        }

        @SuppressLint("ObsoleteSdkInt")
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
            val list_videos = rootview?.findViewById(R.id.list_videos) as RecyclerView

            list_videos.adapter = null
            list_videos.setHasFixedSize(true)
            val mLayoutManager = LinearLayoutManager(context)
            list_videos.layoutManager = mLayoutManager

            val dividerItemDecoration = DividerItemDecoration(
                    list_videos.context,
                    mLayoutManager.orientation)
            list_videos.addItemDecoration(dividerItemDecoration)

            // Specify an adapter

            val mAdapter = DataAdapter(search_list, frag_manager)
            list_videos.adapter = mAdapter
            showProgress(false)
        }

        fun getYoutubeVid(search_query: String?, rootView: View) {
            try {
                val youtube = YouTube.Builder(NetHttpTransport(), JacksonFactory(), HttpRequestInitializer {

                }).setApplicationName("Streamer").build()

                //youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("youtube-cmdline-search-sample").build()
                val search = youtube.search().list("id,snippet")
                search.key = youtube_api_key
                search.q = search_query
                search.type = "video"

                search.fields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"
                search.maxResults = video_limit

                val search_response = search.execute()
                Log.d("Items", search.toString())
                search_list = search_response.items

            } catch (e: Exception) {
                e.printStackTrace()
                val msg = e
                Log.d(LOG_ERROR, "An Exception Occurred: $msg")
                Snackbar.make(rootView, "Enter a search parameter", Snackbar.LENGTH_LONG).show()
            }
        }
    }


}// Required empty public constructor

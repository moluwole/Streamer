package com.example.yung.streamer

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView
import com.squareup.leakcanary.LeakCanary
import com.tonyodev.fetch.Fetch
import com.tonyodev.fetch.request.Request
import kotlinx.android.synthetic.main.activity_play.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class Play : AppCompatActivity() {

    fun filePath(): String {
        var return_value = ""
        if (isStorageAvailable()) {
            val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer")
            if (!file.exists()) file.mkdir()
            return_value = file.toString()
            Log.d("File Path", file.toString())
        } else {
            Log.e("Mount Error", "Unable to access sdCard")
        }
        return return_value
    }

    fun isStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }

    var fetch: Fetch? = null

    companion object {
        private val REQUEST_READ_WRITE_EXTERNAL_STORAGE = 0
    }

    fun reqPermisson(): Boolean {
        // Here, thisActivity is the current activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(youtube_player_view, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_READ_WRITE_EXTERNAL_STORAGE) })
        } else {
            //requestPermissions(Manifest.permission.)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_READ_WRITE_EXTERNAL_STORAGE)
        }
        return false
    }

    var filepath: String = ""

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_READ_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                filepath = filePath()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(application)
        val video_id = intent.getStringExtra("Video ID")
        val video_name = intent.getStringExtra("Video Title") + ".mp4"

        reqPermisson()

        val youTubePlayerView = findViewById(R.id.youtube_player_view) as YouTubePlayerView
        youTubePlayerView.enterFullScreen()
        youTubePlayerView.initialize(object : AbstractYouTubeListener() {
            override fun onReady() {
                youTubePlayerView.loadVideo(video_id, 0f)
            }
        }, true)

        val youtubeLink = "http://youtube.com/watch?v=" + video_id
        var downloadUrl: String

        object : YouTubeExtractor(this) {
            public override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta) {
                if (ytFiles != null) {
                    val itag = 22
                    downloadUrl = ytFiles.get(itag).url
                    Log.d("URL: ", downloadUrl)
                    try {
                        // var test_path = "/storage/sdcard0/streamer"
                        val test_path = filePath()
                        if (test_path != "") {
                            filepath = filePath()
                            if (reqPermisson()) {
                                // var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath)
                                Log.d("Test Path", filepath)
                                //var req = file.mkdirs()
                                //Log.d("Make Dir", req.toString())
                                fetch = Fetch.getInstance(applicationContext)
                                //  var file_path = filePath()
                                val request = Request(downloadUrl, filepath, video_name)
                                val downloadId = fetch?.enqueue(request)
                                Log.d("download ID", downloadId.toString())
                                Log.d("Request", request.toString())

                                if (downloadId.toString() == Fetch.ENQUEUE_ERROR_ID.toString()) {
                                    Snackbar.make(youtube_player_view, "Unable to load Video Offline", Snackbar.LENGTH_LONG).show()
                                } else {
                                    Snackbar.make(youtube_player_view, "Downloading Video Offline...", Snackbar.LENGTH_LONG).show()
                                }

                            } else {
                                Snackbar.make(youtube_player_view, "Could not gain access to Storage", Snackbar.LENGTH_LONG).show()
                            }
                        } else {
                            Snackbar.make(youtube_player_view, "Unable to download video offline. Check your Media Card", Snackbar.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("Error", e.toString())
                    }
                }
            }
        }.extract(youtubeLink, true, true)


        /*    var file_path =File( Environment.getExternalStorageDirectory(), "streamer")
            file_path.mkdirs()
            Snackbar.make(youtube_player_view, file_path.toString(), Snackbar.LENGTH_LONG).show()
    */
        // getFileName()


    }

    class downloadVideo constructor(vid_url: String, file_path: String) : AsyncTask<String, String, String>() {

        var vid_url: String = ""
        var file_path: String = ""

        init {
            this.vid_url = vid_url
            this.file_path = file_path
        }

        override fun doInBackground(vararg params: String?): String {
            // downloadFile(vid_url, file_path)
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


        private fun downloadFile(fileURL: String, fileName: String, file_path: String) {
            try {
                /*  val rootDir = Environment.getExternalStorageDirectory().toString() + File.separator + "Video"
                  val rootFile = File(rootDir)
                  rootFile.mkdir()*/
                val url = URL(fileURL)
                val c = url.openConnection() as HttpURLConnection
                c.requestMethod = "GET"
                c.doOutput = true
                c.connect()
                Log.d("DOwnload", "Downloading Video")
                val f = FileOutputStream(File(file_path,
                        fileName))
                val input_stream = c.inputStream
                val buffer = ByteArray(1024)
                var len1 = input_stream.read(buffer)
                while (len1 > 0) {
                    len1 = input_stream.read(buffer)
                    f.write(buffer, 0, len1)
                }
                f.close()
            } catch (e: IOException) {
                Log.d("Error....", e.toString())
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fetch?.release()
        finish()
    }
}

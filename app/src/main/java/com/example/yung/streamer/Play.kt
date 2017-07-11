package com.example.yung.streamer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView


class Play : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        var video_id = intent.getStringExtra("Video ID")
        val youTubePlayerView = findViewById(R.id.youtube_player_view) as YouTubePlayerView
        youTubePlayerView.enterFullScreen()
        youTubePlayerView.initialize(object : AbstractYouTubeListener() {
            override fun onReady() {
                youTubePlayerView.loadVideo(video_id, 0f)
            }
        }, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}

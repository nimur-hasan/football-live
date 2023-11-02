package com.nehalappstudio.footballlive

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class FullScreenTv : AppCompatActivity() {

    private lateinit var playerView: StyledPlayerView
    private lateinit var player: SimpleExoPlayer
    private var wakeLock: PowerManager.WakeLock? = null
    var channelUrl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_full_screen_tv)

        playerView = findViewById(R.id.full_screen_player_view)

        val url = intent.getStringExtra("url")
        if(url != ""){
            channelUrl = url.toString()
        }

        // Create a default TrackSelector and LoadControl
        val trackSelector = DefaultTrackSelector(this)
        val loadControl = DefaultLoadControl()

        // Create an ExoPlayer instance
        player = SimpleExoPlayer.Builder(this, DefaultRenderersFactory(this))
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()

        // Attach the player to the view
        playerView.player = player
    }

    override fun onStart() {
        super.onStart()

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "YourApp:WakeLockTag"
        )
        wakeLock?.acquire()



        // Create a MediaSource using the M3U8 URL.
        val userAgent = Util.getUserAgent(this, "YourApp")
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(channelUrl))

        // Prepare the player with the MediaSource.
        player.prepare(mediaSource)
        player.play()
    }


    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}
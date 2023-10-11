package com.nehalappstudio.footballlive.fragment

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.nehalappstudio.footballlive.R

class TV : Fragment() {

    private lateinit var playerView: StyledPlayerView
    private lateinit var player: SimpleExoPlayer
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_t_v, container, false)
        playerView = view.findViewById(R.id.player_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a default TrackSelector and LoadControl
        val trackSelector = DefaultTrackSelector(requireContext())
        val loadControl = DefaultLoadControl()

        // Create an ExoPlayer instance
        player = SimpleExoPlayer.Builder(requireContext(), DefaultRenderersFactory(requireContext()))
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()

        // Attach the player to the view
        playerView.player = player
    }

    override fun onStart() {
        super.onStart()
        
        val powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "YourApp:WakeLockTag"
        )
        wakeLock?.acquire()

        val mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                // Handle the error here, e.g., log or display a message.
                Log.d("exooooooooooooooooooo", error.toString())
            }
        })
    }

    override fun onStop() {
        super.onStop()
        // Release the player when the fragment is stopped
        player.release()
    }
}

package com.nehalappstudio.footballlive.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.nehalappstudio.footballlive.FullScreenTv
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.adapter.TvAdapter
import com.nehalappstudio.footballlive.models.MatchCardModel
import com.nehalappstudio.footballlive.models.SingleTeam
import com.nehalappstudio.footballlive.models.TeamModel
import com.nehalappstudio.footballlive.models.TvModel
import com.nehalappstudio.footballlive.models.VenueModel

class TV : Fragment() {

    private lateinit var playerView: StyledPlayerView
    private lateinit var player: SimpleExoPlayer
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var tvList:ArrayList<TvModel>
    private lateinit var rvTv:RecyclerView
    private lateinit var tvAdapter: TvAdapter
    private lateinit var btnFullScreen: Button
    var channelUrl:String = "https://chop.fordems.live/hls/for-2.m3u8"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_t_v, container, false)

        tvList = ArrayList()
        rvTv = view.findViewById(R.id.rvTv)
        tvAdapter = TvAdapter(requireContext(), tvList, onItemClick)

        rvTv.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        rvTv.adapter = tvAdapter

        btnFullScreen = view.findViewById(R.id.btnFullScreen)
        playerView = view.findViewById(R.id.player_view)
        fetchDayMatches()

        btnFullScreen.setOnClickListener {
            val intent = Intent(requireContext(), FullScreenTv::class.java)
            intent.putExtra("url", channelUrl)
            startActivity(intent)
        }
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


        // Create a MediaSource using the M3U8 URL.
        val userAgent = getUserAgent(requireContext(), "YourApp")
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://chop.fordems.live/hls/for-2.m3u8"))

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


    private fun fetchDayMatches() {

//        lottieAnimationView.visibility = View.VISIBLE
//        rvMatches.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        val apiUrl = "http://139.59.8.82/zahirfot/api.php?get_all_channels"

        val stringRequest = object : JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            Response.Listener { response ->
                println("API Response: $response")
//                Toast.makeText(requireContext(), "Api Request Successful", Toast.LENGTH_LONG).show()

                val responseArr = response.getJSONArray("LIVETV")

                tvList.clear()

                for (i in 0 until responseArr.length()) {
                    val TvObj = responseArr.getJSONObject(i)

                    val id = TvObj.getString("id")
                    val channel_title = TvObj.getString("channel_title")
                    val channel_url = TvObj.getString("channel_url")
                    val channel_thumbnail = TvObj.getString("channel_thumbnail")


                    tvList.add(TvModel(id, channel_title, channel_url, channel_thumbnail))

                }

                Toast.makeText(requireContext(), tvList.size.toString(), Toast.LENGTH_LONG).show()
//                lottieAnimationView.visibility = View.GONE
//                rvMatches.visibility = View.VISIBLE
//                matchAdapter.notifyDataSetChanged()


            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Api Request Failed.", Toast.LENGTH_LONG).show()
                println("API Request Failed: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-rapidapi-key"] = "9934587b22930a733e2774cb3b1f3e1d"
                headers["x-rapidapi-host"] = "v3.football.api-sports.io"
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    val onItemClick: (Int) -> Unit = { tvId ->
        val selectedChannel = tvList[tvId]
//        Toast.makeText(requireContext(), selectedChannel.channel_url, Toast.LENGTH_LONG).show()
        // Release the current player
        player.release()

        channelUrl = selectedChannel.channel_url
        // Create a new ExoPlayer instance
        val newPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        playerView.player = newPlayer

        // Change the video source URL
        val newVideoUrl = selectedChannel.channel_url
        val newMediaSource = buildMediaSource(newVideoUrl)
        newPlayer.prepare(newMediaSource)

        // Assign the new player to the player variable
        player = newPlayer
        player.play()
    }

    private fun buildMediaSource(videoUrl: String): MediaSource {
        val userAgent = Util.getUserAgent(requireContext(), "YourApp")
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoUrl))
    }
}

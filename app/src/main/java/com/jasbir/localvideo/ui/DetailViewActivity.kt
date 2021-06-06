package com.jasbir.localvideo.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jasbir.localvideo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailViewActivity : AppCompatActivity(), Player.EventListener {
    var video_player: ExoPlayer? = null
    private lateinit var next: ImageView
    private lateinit var pause: ImageView
    private lateinit var previous: ImageView
    private lateinit var mute: ImageView
    private lateinit var unmute: ImageView
    val viewModel: MainViewModel by viewModels()

    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        viewModel.context = this
        pause = findViewById(R.id.pause)
        next = findViewById(R.id.nextVideo)
        previous = findViewById(R.id.perVideo)
        unmute = findViewById(R.id.unmute)
        mute = findViewById(R.id.mute)

        val path = intent.getIntExtra("path",0)
        position = path

        viewModel.invoke()

        Set_Player(position)

        pause.setOnClickListener {
            video_player?.playWhenReady = false
        }
        mute.setOnClickListener {
            mute.visibility = View.GONE


        }
        unmute.setOnClickListener {
            unmute.visibility = View.GONE
            mute.visibility = View.VISIBLE
        }
        next.setOnClickListener {
            video_player?.release()
            Set_Player(position)
        }
        previous.setOnClickListener {
            video_player?.release()
            var pos = position-1
            position = pos
            Set_Player(position)
        }
    }

    fun Set_Player(pos : Int) {
        val trackSelector = DefaultTrackSelector()
        video_player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "LV")
        )
        viewModel.getVideoData.observe(this, Observer {

            var videoPath = it.get(pos).video_path
            val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoPath))

            video_player!!.prepare(videoSource)
        })


        video_player!!.addListener(this)


        val playerView = findViewById<PlayerView>(R.id.myPlayer)
        playerView.player = video_player
        video_player!!.setPlayWhenReady(true)

        position+= 1



    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }
}
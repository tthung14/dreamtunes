package com.tuhoc.dreamtunes.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tuhoc.dreamtunes.MainActivity
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.receiver.NotificationReceiver
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.play.PlayFragment
import com.tuhoc.dreamtunes.ui.play.PlayFragment.Companion.isPlaying
import com.tuhoc.dreamtunes.utils.ApplicationClass
import com.tuhoc.dreamtunes.utils.Constants
import kotlinx.coroutines.Runnable


class MusicService : Service(), AudioManager.OnAudioFocusChangeListener {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable
    lateinit var audioManager: AudioManager

    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int) {
        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val contentIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            ApplicationClass.PREVIOUS
        )
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, flag)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            ApplicationClass.PLAY
        )
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, flag)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            ApplicationClass.NEXT
        )
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, flag)

//        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
//            ApplicationClass.EXIT
//        )
//        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)

        val image = BitmapFactory.decodeResource(resources, R.drawable.music)

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayFragment.musicListPA[PlayFragment.songPosition].songName)
            .setContentText(PlayFragment.musicListPA[PlayFragment.songPosition].singer!!.singerName)
            .setSmallIcon(R.drawable.music)
            .setLargeIcon(image)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .addAction(R.drawable.ic_previous, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .build()

        // seekbar nếu sài bật lên
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val playbackSpeed = if(isPlaying) 1F else 0F
            mediaSession.setMetadata(MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
                .build())
            val playBackState = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession.setPlaybackState(playBackState)
            mediaSession.setCallback(object: MediaSessionCompat.Callback(){
                //called when headphones buttons are pressed
                //currently only pause or play music on button click
                override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                    if(isPlaying){
                        //pause music
                        PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_play)
                        isPlaying = false
                        mediaPlayer!!.pause()
                        showNotification(R.drawable.ic_play)
                    } else{
                        //play music
                        PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_pause)
                        isPlaying = true
                        mediaPlayer!!.start()
                        showNotification(R.drawable.ic_pause)
                    }

                    return super.onMediaButtonEvent(mediaButtonEvent)
                }
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    mediaPlayer!!.seekTo(pos.toInt())
                    val playBackStateNew = PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                        .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                        .build()
                    mediaSession.setPlaybackState(playBackStateNew)
                }
            })
        }

        startForeground(13, notification)
    }

    fun createMediaPlayer() {
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(PlayFragment.musicListPA[PlayFragment.songPosition].link)
            mediaPlayer!!.prepare()
            PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_pause)
            showNotification(R.drawable.ic_pause)
            PlayFragment.bindingD.tvCurrentTime.text =
                Constants.formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayFragment.bindingD.tvTotalTime.text =
                Constants.formatDuration(mediaPlayer!!.duration.toLong())
            PlayFragment.bindingD.seekBar.progress = 0
            PlayFragment.bindingD.seekBar.max = mediaPlayer!!.duration
            PlayFragment.nowPlayingId = PlayFragment.musicListPA[PlayFragment.songPosition].songId!!
            HomeFragment.currentSongId = PlayFragment.nowPlayingId

        } catch (e: Exception) {
            return
        }
    }

    fun seekBarSetup() {
        runnable = Runnable {
            PlayFragment.bindingD.tvCurrentTime.text =
                Constants.formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayFragment.bindingD.seekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
//            handler.postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
//        handler.postDelayed(runnable, 0)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) {
            //pause music
            PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_play)

            isPlaying = false
            mediaPlayer!!.pause()
            showNotification(R.drawable.ic_play)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (::runnable.isInitialized) {
//            handler.removeCallbacks(runnable)
//        }
        mediaPlayer?.release() // Dừng MediaPlayer
        stopForeground(true) // Gỡ bỏ notification và dừng foreground service
    }
}
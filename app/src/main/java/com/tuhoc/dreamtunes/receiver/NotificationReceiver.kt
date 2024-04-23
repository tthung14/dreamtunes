package com.tuhoc.dreamtunes.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.ui.play.PlayFragment

import com.tuhoc.dreamtunes.utils.ApplicationClass

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            //only play next or prev song, when music list contains more than one song
            ApplicationClass.PREVIOUS -> if (PlayFragment.musicListPA.size > 1) prevNextSong(
                increment = false,
                context = context!!
            )
            ApplicationClass.PLAY -> if (PlayFragment.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> if (PlayFragment.musicListPA.size > 1) prevNextSong(
                increment = true,
                context = context!!
            )
            ApplicationClass.EXIT -> {
//                 exitApplication()
            }
        }
    }

    private fun playMusic() {
        PlayFragment.isPlaying = true
        PlayFragment.musicService!!.mediaPlayer!!.start()
        PlayFragment.musicService!!.showNotification(R.drawable.ic_pause)
        PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_pause)
    }

    private fun pauseMusic() {
        PlayFragment.isPlaying = false
        PlayFragment.musicService!!.mediaPlayer!!.pause()
        PlayFragment.musicService!!.showNotification(R.drawable.ic_play)
        PlayFragment.bindingD.imgPausePlay.setImageResource(R.drawable.ic_play)
    }

    private fun prevNextSong(increment: Boolean, context: Context) {
        PlayFragment.setSongPosition(increment = increment)
        PlayFragment.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayFragment.musicListPA[PlayFragment.songPosition].image)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(PlayFragment.bindingD.imgAvatar)
        PlayFragment.bindingD.tvName.text = PlayFragment.musicListPA[PlayFragment.songPosition].songName
        PlayFragment.sharedViewModel!!.setMessage(PlayFragment.musicListPA[PlayFragment.songPosition].songId!!)

        playMusic()
    }
}
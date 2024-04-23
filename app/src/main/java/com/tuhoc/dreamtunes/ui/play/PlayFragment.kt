package com.tuhoc.dreamtunes.ui.play

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.FragmentPlayBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.service.MusicService
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.Constants.LIST
import com.tuhoc.dreamtunes.utils.Constants.SONG

class PlayFragment : BaseFragment<FragmentPlayBinding>(FragmentPlayBinding::inflate),
    ServiceConnection, MediaPlayer.OnCompletionListener {
    private lateinit var playViewModel: PlayViewModel
    private var isActive = true
    private var x = 0

    companion object {
        var musicListPA = mutableListOf<Song>()
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        var sharedViewModel: SharedViewModel? = null
        var isRandom: Boolean = false

        @SuppressLint("StaticFieldLeak")
        lateinit var bindingD: FragmentPlayBinding
        var nowPlayingId: Int = 0

        fun setSongPosition(increment: Boolean): Int {
//            if (isRandom) {
//                // Nếu chế độ ngẫu nhiên được bật, chọn một vị trí ngẫu nhiên trong danh sách bài hát
//                songPosition = (0 until musicListPA.size).random()
//            } else {
//                if (increment) {
//                    if (musicListPA.size - 1 == songPosition)
//                        songPosition = 0
//                    else ++songPosition
//                } else {
//                    if (0 == songPosition)
//                        songPosition = musicListPA.size - 1
//                    else --songPosition
//                }
//            }
//
//            return musicListPA[songPosition].songId!!
            val previousSongPosition = songPosition // Lưu lại vị trí bài hát trước khi cập nhật

            if (isRandom) {
                // Nếu chế độ ngẫu nhiên được bật, chọn một vị trí ngẫu nhiên trong danh sách bài hát
                songPosition = (0 until musicListPA.size).random()
            } else {
                // Nếu chế độ ngẫu nhiên không được bật, xác định vị trí tiếp theo dựa trên increment
                if (increment) {
                    if (musicListPA.size - 1 == songPosition)
                        songPosition = 0
                    else ++songPosition
                } else {
                    if (0 == songPosition)
                        songPosition = musicListPA.size - 1
                    else --songPosition
                }
            }

            // Nếu vị trí bài hát đã thay đổi, thì mới cần cập nhật giao diện
            return if (songPosition != previousSongPosition) {
                musicListPA[songPosition].songId!!
            } else {
                // Nếu vị trí không thay đổi, không cần cập nhật giao diện
                -1
            }
        }
    }

//    override fun observerData() {
//        super.observerData()
//        playViewModel = ViewModelProvider(this)[PlayViewModel::class.java]
//    }

    override fun initData() {
        super.initData()
        isActive = true
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        bindingD = binding

        playViewModel = ViewModelProvider(this)[PlayViewModel::class.java]

        initializeLayout()

        binding.imgVolume.setOnClickListener {
            initVolume()
        }

        binding.btnBack.setOnClickListener {
            isActive = false
            requireActivity().onBackPressed()
        }

        binding.imgPausePlay.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
        binding.imgPrevious.setOnClickListener { prevNextSong(increment = false) }
        binding.imgNext.setOnClickListener { prevNextSong(increment = true) }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                    musicService!!.showNotification(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    override fun initView() {
        super.initView()

        requireActivity().runOnUiThread {
            if (isPlaying) {
                binding.imgAvatar.rotation = x++.toFloat()
            } else {
                binding.imgAvatar.rotation = 0F
            }
        }

        val song = arguments?.getParcelable<Song>(SONG)!!
        val user = LoginManager.getCurrentUser(requireContext())
        user!!.userId?.let {
            song.songId?.let { it1 ->
                playViewModel.isFavoriteExits(it, it1) {
                    if (it) {
                        binding.imgFavorite.setBackgroundResource(R.drawable.ic_love_enable)
                    } else {
                        binding.imgFavorite.setBackgroundResource(R.drawable.ic_love)
                    }
                }
            }
        }
    }

    override fun handleEvent() {
        super.handleEvent()

        onClick()
    }

    private fun onClick() {
        binding.imgFavorite.setOnClickListener {
            val user = LoginManager.getCurrentUser(requireContext())
            val song = arguments?.getParcelable<Song>(SONG)!!
            user!!.userId?.let {
                song.songId?.let { it1 ->
                    playViewModel.isFavorite(it, it1) {
                        if (it) {
                            binding.imgFavorite.setBackgroundResource(R.drawable.ic_love)
                        } else {
                            binding.imgFavorite.setBackgroundResource(R.drawable.ic_love_enable)
                        }
                    }
                }
            }
        }

        binding.imgRandom.setOnClickListener {
            if (!isRandom) {
                isRandom = true
                binding.imgRandom.setBackgroundResource(R.drawable.ic_random_enable)
            } else {
                isRandom = false
                binding.imgRandom.setBackgroundResource(R.drawable.ic_random)
            }
        }
    }

    //Important Function
    @SuppressLint("SuspiciousIndentation")
    private fun initializeLayout() {
        val isNow = arguments?.getBoolean("play")!!
        val playList = arguments?.getParcelableArrayList<Song>(LIST)
        val po = arguments?.getInt("po")!!
        val song = arguments?.getParcelable<Song>(SONG)!!
        sharedViewModel!!.setMessage(song.songId!!)
        songPosition = po

        if (isNow) {
            setLayout()
            binding.tvCurrentTime.text =
                Constants.formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            binding.tvTotalTime.text =
                Constants.formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            binding.seekBar.progress = musicService!!.mediaPlayer!!.currentPosition
            binding.seekBar.max = musicService!!.mediaPlayer!!.duration
            if (isPlaying) {
                binding.imgPausePlay.setImageResource(R.drawable.ic_pause)
            } else {
                binding.imgPausePlay.setImageResource(R.drawable.ic_play)
            }
        } else {
            initServiceAndPlaylist(playList!!, shuffle = false)
            if (musicService != null && !isPlaying) playMusic()
        }
    }

    private fun setLayout() {
        //fIndex = favouriteChecker(musicListPA[songPosition].id)
        Glide.with(requireActivity())
            .load(musicListPA[songPosition].image)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(binding.imgAvatar)
        binding.tvName.text = musicListPA[songPosition].songName

        val user = LoginManager.getCurrentUser(requireContext())
        user!!.userId?.let {
            musicListPA[songPosition].songId?.let { it1 ->
                playViewModel.isFavoriteExits(it, it1) {
                    if (it) {
                        binding.imgFavorite.setBackgroundResource(R.drawable.ic_love_enable)
                    } else {
                        binding.imgFavorite.setBackgroundResource(R.drawable.ic_love)
                    }
                }
            }
        }

        if (isRandom) {
            binding.imgRandom.setBackgroundResource(R.drawable.ic_random_enable)
        } else {
            binding.imgRandom.setBackgroundResource(R.drawable.ic_random)
        }

//        val drawable: BitmapDrawable = binding.imgAvatar.drawable as BitmapDrawable
//        val bitmap: Bitmap = drawable.bitmap
//        val bgColor = getColorsFromBitmap(bitmap)
//        val gradient = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(0xFFFFFF, bgColor))
//        binding.root.background = gradient
//        requireActivity().window?.statusBarColor = bgColor
    }

    private fun getColorsFromBitmap(bitmap: Bitmap): Int {
        val palette = Palette.from(bitmap).generate()
        return palette.getDominantColor(Color.BLACK)
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].link)
            musicService!!.mediaPlayer!!.prepare()
            binding.tvCurrentTime.text =
                Constants.formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            binding.tvTotalTime.text =
                Constants.formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            binding.seekBar.progress = 0
            binding.seekBar.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            nowPlayingId = musicListPA[songPosition].songId!!

            playMusic()

        } catch (e: Exception) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun playMusic() {
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
        binding.imgPausePlay.setImageResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause)
    }

    private fun pauseMusic() {
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        binding.imgPausePlay.setImageResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
    }

    private fun prevNextSong(increment: Boolean) {
        val songId = setSongPosition(increment)
        if (songId != -1) { // Kiểm tra nếu vị trí bài hát đã thay đổi
            setLayout()
            createMediaPlayer()
            sharedViewModel!!.setMessage(songId)
        }
//        if (increment) {
//            val songId = setSongPosition(increment = true)
//            setLayout()
//            createMediaPlayer()
//            sharedViewModel!!.setMessage(songId)
//        } else {
//            val songId = setSongPosition(increment = false)
//            setLayout()
//            createMediaPlayer()
//            sharedViewModel!!.setMessage(songId)
//        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (musicService == null) {
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()
            musicService!!.audioManager =
                requireActivity().getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
            musicService!!.audioManager.requestAudioFocus(
                musicService,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
        createMediaPlayer()
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        sharedViewModel!!.setMessage(setSongPosition(increment = true))
        createMediaPlayer()

        if (isActive) {
            setLayout()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 || resultCode == AppCompatActivity.RESULT_OK)
            return
    }


    private fun initServiceAndPlaylist(
        playlist: MutableList<Song>,
        shuffle: Boolean,
        playNext: Boolean = false
    ) {
        val intent = Intent(requireActivity(), MusicService::class.java)
        requireActivity().bindService(intent, this, AppCompatActivity.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
        musicListPA.clear()
        musicListPA.addAll(playlist)
        if (shuffle) musicListPA.shuffle()
        setLayout()
    }

    override fun onDestroy() {
        isActive = false
        super.onDestroy()
    }

    private fun initVolume() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        audioManager?.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
    }
}
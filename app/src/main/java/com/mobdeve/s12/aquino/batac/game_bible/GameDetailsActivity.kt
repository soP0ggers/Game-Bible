package com.mobdeve.s12.aquino.batac.game_bible

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mobdeve.s12.aquino.batac.game_bible.databinding.ActivityGameDetailsBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import org.w3c.dom.Text
import java.util.*

class GameDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailsBinding
    private lateinit var videoID: String
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Show tool bar back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        TODO: Replace details accordingly (With SQLite)
//        TODO: Add Reviews + Stats LATER
        var gameDetails = intent

        binding.detTitleTv.text = gameDetails.getStringExtra("title")
        binding.detDescTv.text = gameDetails.getStringExtra("desc")
        binding.detGenreTv.text = "Genre: " + gameDetails.getStringExtra("genre")
        binding.detDateTv.text = "Release Date: " + gameDetails.getStringExtra("releaseDate")
        binding.detDevTv.text = "Developer: " + gameDetails.getStringExtra("developer")
        binding.detPubTv.text = "Publisher: " + gameDetails.getStringExtra("publisher")
        Glide.with(this).load(gameDetails.getStringExtra("img")).into(binding.detThumbIv)

//      Setup - Text-To-Speech API
        initTTS()

//      Setup - YouTube API
//      Setup Link
        videoID = intent.getStringExtra("trailer").toString()
        initYT(videoID)

//      Viewing Community Reviews
        binding.detViewBtn.setOnClickListener{
            var intent = Intent(this, ViewReviewActivity::class.java)
            var gid = gameDetails.getIntExtra("gid", 0)
            intent.putExtra("gid", gid)

            startActivity(intent)
        }
    }

    private fun initTTS(){
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS){
                tts.setLanguage(Locale.US)
            }
        })

        binding.detSpeechIv.setOnClickListener{
            if(tts.isSpeaking){
                tts.stop()
                binding.detSpeechIv.setImageResource(R.drawable.ic_volume_down)
            }else{
                tts.speak(binding.detDescTv.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
                binding.detSpeechIv.setImageResource(R.drawable.ic_volume_up)
            }
        }
    }
    private fun initYT(videoID: String){
        lifecycle.addObserver(binding.detYTPlayer)

        binding.detYTPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.cueVideo(videoID, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                // this method is called if video has ended,
                super.onStateChange(youTubePlayer, state)
            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate)
            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction)
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
            }
        })
    }

    //  Finish activity after clicking back button on ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
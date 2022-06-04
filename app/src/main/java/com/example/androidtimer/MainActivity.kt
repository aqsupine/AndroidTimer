package com.example.androidtimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import com.example.androidtimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var conterIsActiv = false
    var musicFinish: MediaPlayer? = null
    var countDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.startBtn.setOnClickListener {
            start()
        }

        binding.timeSbr.max = 300
        binding.timeSbr.progress = 300

        musicFinish = MediaPlayer.create(this, R.raw.alarm_bell)
        binding.timeSbr.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    update(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                binding.imageView.setImageResource(R.drawable.bulb_off)
            }
        })
    }

    private fun update(progress: Int) {
        var minutes = progress / 60
        var secondes = progress % 60
        var timerFinal = ""
        if (secondes <= 9) {
            timerFinal = "0$secondes"
        } else {
            timerFinal = "$secondes"
        }
        binding.apply {
            timeSbr.progress = progress
            timeText.text = "$minutes:$timerFinal"
        }
    }

    fun start() {
        if (!conterIsActiv) {
            conterIsActiv = true
            binding.timeSbr.isEnabled = false
            binding.startBtn.text = "STOP"
            binding.startBtn.setBackgroundColor(getColor(R.color.red))
            countDownTimer =
                object : CountDownTimer((binding.timeSbr.progress * 1000).toLong(), 1000) {
                    override fun onTick(p0: Long) {
                        update((p0 / 1000).toInt())
                    }

                    override fun onFinish() {
                        reset()
                        if (musicFinish != null) {
                            musicFinish!!.start()
                        }
                        binding.imageView.setImageResource(R.drawable.bulb_on)
                    }
                }.start()
        } else {
            reset()
        }
    }

    private fun reset() {
        binding.apply {
            timeText.text = "0:00"
            binding.imageView.setImageResource(R.drawable.bulb_off)
            timeSbr.progress = 0
            timeSbr.isEnabled = true
            countDownTimer?.cancel()
            startBtn.text = "START"
            startBtn.setBackgroundColor(getColor(R.color.purple_500))
            conterIsActiv = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (conterIsActiv) {
            countDownTimer?.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (conterIsActiv) {
            countDownTimer?.cancel()
        }
    }

}
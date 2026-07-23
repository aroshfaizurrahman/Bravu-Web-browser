package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrankAudioPlayer(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playPrankFanfare() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Cheerful playful tune (C5 -> E5 -> G5 -> C6 festive fanfare)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_3, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_6, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_C, 300)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun playAlertAlarm() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Virus alert style warning beeps
                repeat(3) {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 120)
                    delay(150)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun triggerVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 100, 50, 150, 50, 300),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 100, 50, 150, 50, 300), -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

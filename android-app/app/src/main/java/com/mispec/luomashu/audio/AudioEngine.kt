package com.mispec.luomashu.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin

/**
 * Pure sine-wave tone generator via AudioTrack.
 * No resource files — all tones generated programmatically.
 * Supports tone playback, pitch pair discrimination, and rhythmic pattern generation.
 * @sample playTone(880.0, 400, 0.6f)
 */
object AudioEngine {
    private const val SAMPLE_RATE = 44100
    private const val CHANNELS = AudioFormat.CHANNEL_OUT_MONO
    private const val ENCODING = AudioFormat.ENCODING_PCM_16BIT

    @Volatile private var track: AudioTrack? = null
    private val mutex = Mutex()

    // Frequency presets — mid-C tuned for pleasant test tones
    const val FREQ_LOW    = 440   // A4 — bass tone for pitch low
    const val FREQ_MID    = 880   // A5 — standard beep
    const val FREQ_HIGH   = 1760  // A6 — treble for pitch high
    const val FREQ_BEEP   = 1000  // hearing-test alert tone

    private suspend fun ensureTrack() = mutex.withLock {
        try {
            track?.release()
            track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(ENCODING)
                    .setChannelMask(CHANNELS)
                    .build()
            )
            .setBufferSizeInBytes(AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNELS, ENCODING) * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
            track?.play()
        } catch (e: Exception) {
            android.util.Log.e("AudioEngine", "ensureTrack failed", e)
            track = null
        }
    }

    /** Play a sine-wave tone with amplitude fade-in/out.
     * @param frequencyHz tone frequency in Hertz
     * @param durationMs playback duration in milliseconds
     * @param volume amplitude multiplier 0.0-1.0
     */
    suspend fun playTone(frequencyHz: Double, durationMs: Long, volume: Float = 0.7f) =
        withContext(Dispatchers.IO) {
            try {
                ensureTrack()
            val t = track ?: return@withContext
            val numSamples = (SAMPLE_RATE * durationMs / 1000).toInt()
            val buffer = ShortArray(numSamples)
            val fadeSamples = (numSamples * 0.05).toInt().coerceAtLeast(8) // 5% fade in/out

            for (i in buffer.indices) {
                val fade = when {
                    i < fadeSamples           -> i.toFloat() / fadeSamples
                    i > numSamples - fadeSamples -> (numSamples - i).toFloat() / fadeSamples
                    else -> 1f
                }
                buffer[i] = (Short.MAX_VALUE * volume * fade * sin(2.0 * PI * frequencyHz * i / SAMPLE_RATE)).toInt().toShort()
            }
                t.write(buffer, 0, buffer.size)
            } catch (e: Exception) {
                android.util.Log.e("AudioEngine", "playTone failed", e)
            }
        }

    /** Play two-tone sequence for pitch discrimination tests.
     * @param lowFirst if true plays low→high, otherwise high→low
     */
    suspend fun playPitchPair(lowFirst: Boolean) {
        val (f1, f2) = if (lowFirst) FREQ_LOW.toDouble() to FREQ_HIGH.toDouble()
                       else FREQ_HIGH.toDouble() to FREQ_LOW.toDouble()
        val gap = 200L
        playTone(f1, 400, 0.6f)
        kotlinx.coroutines.delay(gap)
        playTone(f2, 400, 0.6f)
    }

    /** Play a sequence of tones forming a rhythmic pattern.
     * @param stepIndices indices in the pattern to play
     * @param freqBase base frequency for the first step
     */
    suspend fun playRhythmPattern(stepIndices: List<Int>, freqBase: Double = FREQ_MID.toDouble()) {
        for (idx in stepIndices) {
            val freq = freqBase * (1.0 + idx * 0.15)
            playTone(freq, 200, 0.55f)
            kotlinx.coroutines.delay(250)
        }
    }

    fun release() {
        try {
            track?.stop()
            track?.release()
            track = null
        } catch (e: Exception) {
            android.util.Log.e("AudioEngine", "release failed", e)
            track = null
        }
    }
}

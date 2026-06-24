package com.mispec.luomashu.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Creates a branded result card bitmap for sharing.
 */
fun createResultBitmap(toolName: String, score: String, subtitle: String = ""): Bitmap {
    val width = 800
    val height = 480
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Background
    canvas.drawColor(Color.parseColor("#FAF8F5"))

    // Brand accent bar at top
    val accentPaint = Paint().apply { color = Color.parseColor("#4F5ED8") }
    canvas.drawRect(0f, 0f, width.toFloat(), 8f, accentPaint)

    // Tool name
    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#18181A")
        textSize = 48f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(toolName, width / 2f, 100f, namePaint)

    // Score
    val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4F5ED8")
        textSize = 96f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(score, width / 2f, 240f, scorePaint)

    // Subtitle
    if (subtitle.isNotEmpty()) {
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#57534E")
            textSize = 32f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(subtitle, width / 2f, 300f, subPaint)
    }

    // Brand watermark
    val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CCC8C2")
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("罗码术 · Luomashu", width / 2f, height - 40f, brandPaint)

    return bitmap
}

/**
 * Share the result bitmap via Android share sheet.
 * File I/O runs on a background thread to avoid blocking the main thread.
 */
fun shareResult(context: Context, bitmap: Bitmap, toolName: String = "", score: String = "") {
    Thread {
        try {
            val file = File(context.cacheDir, "share_result_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val shareText = if (toolName.isNotEmpty() && score.isNotEmpty()) {
                "我在「罗码术」完成了「$toolName」，成绩：$score！来试试吧 🧠"
            } else {
                "来看看我的测试结果！来自罗码术 App"
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, "分享成绩"))
        } catch (e: Exception) {
            android.util.Log.e("ShareUtils", "分享失败", e)
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                android.widget.Toast.makeText(context, "分享失败，请重试", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }.start()
}

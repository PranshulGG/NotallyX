package com.philkes.notallyx.utils

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.philkes.notallyx.BuildConfig
import com.philkes.notallyx.R
import com.philkes.notallyx.data.model.Color
import com.philkes.notallyx.data.model.ListItem
import com.philkes.notallyx.databinding.LabelBinding
import com.philkes.notallyx.presentation.getColorFromAttr
import com.philkes.notallyx.presentation.viewmodel.preference.TextSize
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.DateFormat

object Operations {

    private const val TAG = "Operations"

    const val extraCharSequence = "com.philkes.notallyx.extra.charSequence"

    fun getLastExceptionLog(app: Application): String? {
        val logFile = getLog(app)
        if (logFile.exists()) {
            val logContents = logFile.readText().substringAfterLast("[Start]")
            return URLEncoder.encode(logContents, StandardCharsets.UTF_8.toString())
        }
        return null
    }

    fun log(app: Application, throwable: Throwable? = null, stackTrace: String? = null) {
        Log.e(TAG, "Exception occurred", throwable)
        val file = getLog(app)
        val output = FileOutputStream(file, !file.exists() || !file.isLargerThan(2048))
        val writer = PrintWriter(OutputStreamWriter(output, Charsets.UTF_8))

        val formatter = DateFormat.getDateTimeInstance()
        val time = formatter.format(System.currentTimeMillis())

        writer.println("[Start]")
        throwable?.printStackTrace(writer)
        stackTrace?.let { writer.println(it) }
        writer.println("Version code : " + BuildConfig.VERSION_CODE)
        writer.println("Version name : " + BuildConfig.VERSION_NAME)
        writer.println("Model : " + Build.MODEL)
        writer.println("Device : " + Build.DEVICE)
        writer.println("Brand : " + Build.BRAND)
        writer.println("Manufacturer : " + Build.MANUFACTURER)
        writer.println("Android : " + Build.VERSION.SDK_INT)
        writer.println("Time : $time")
        writer.println("[End]")

        writer.close()
    }

    @ColorInt
    fun extractColor(color: Color, context: Context): Int {
        val id =
            when (color) {
                Color.DEFAULT -> return context.getColorFromAttr(R.attr.colorSurface)
                Color.CORAL -> R.color.Coral
                Color.ORANGE -> R.color.Orange
                Color.SAND -> R.color.Sand
                Color.STORM -> R.color.Storm
                Color.FOG -> R.color.Fog
                Color.SAGE -> R.color.Sage
                Color.MINT -> R.color.Mint
                Color.DUSK -> R.color.Dusk
                Color.FLOWER -> R.color.Flower
                Color.BLOSSOM -> R.color.Blossom
                Color.CLAY -> R.color.Clay
            }
        return ContextCompat.getColor(context, id)
    }

    fun shareNote(context: Context, title: String, body: CharSequence) {
        val text = body.toString()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(extraCharSequence, body)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_TITLE, title)
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        val chooser = Intent.createChooser(intent, null)
        context.startActivity(chooser)
    }

    fun getBody(list: List<ListItem>) = buildString {
        for (item in list) {
            val check = if (item.checked) "[✓]" else "[ ]"
            val childIndentation = if (item.isChild) "    " else ""
            appendLine("$childIndentation$check ${item.body}")
        }
    }

    fun bindLabels(group: ChipGroup, labels: List<String>, textSize: TextSize) {
        if (labels.isEmpty()) {
            group.visibility = View.GONE
        } else {
            group.visibility = View.VISIBLE
            group.removeAllViews()

            val inflater = LayoutInflater.from(group.context)
            val labelSize = textSize.displayBodySize

            for (label in labels) {
                val view = LabelBinding.inflate(inflater, group, true).root
                view.background = getOutlinedDrawable(group.context)
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, labelSize)
                view.text = label
            }
        }
    }

    fun getLog(app: Application): File {
        val folder = File(app.filesDir, "logs")
        folder.mkdir()
        return File(folder, "Log.v1.txt")
    }

    fun Fragment.reportBug(stackTrace: String?) {
        requireContext().catchNoBrowserInstalled {
            startActivity(createReportBugIntent(stackTrace))
        }
    }

    fun Context.reportBug(stackTrace: String?) {
        catchNoBrowserInstalled { startActivity(createReportBugIntent(stackTrace)) }
    }

    fun Context.catchNoBrowserInstalled(callback: () -> Unit) {
        try {
            callback()
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(this, R.string.install_a_browser, Toast.LENGTH_LONG).show()
        }
    }

    private fun createReportBugIntent(stackTrace: String?): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://github.com/PhilKes/NotallyX/issues/new?labels=bug&projects=&template=bug_report.yml&version=${BuildConfig.VERSION_NAME}&android-version=${Build.VERSION.SDK_INT}${stackTrace?.let {  "&logs=$stackTrace"} ?: ""}"
                    .take(2000)
            ),
        )
    }

    private fun getOutlinedDrawable(context: Context): MaterialShapeDrawable {
        val model =
            ShapeAppearanceModel.builder()
                .setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(RelativeCornerSize(0.5f))
                .build()

        val drawable = MaterialShapeDrawable(model)
        drawable.fillColor = ColorStateList.valueOf(0)
        drawable.strokeWidth = context.resources.displayMetrics.density
        drawable.strokeColor = ContextCompat.getColorStateList(context, R.color.chip_stroke)

        return drawable
    }

    private fun File.isLargerThan(kilobytes: Long): Boolean {
        return (length() / 1024) > kilobytes
    }
}

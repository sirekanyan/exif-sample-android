package me.vadik.exif

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.media.ExifInterface
import android.support.media.ExifInterface.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private companion object {
        private val DIRECTORY = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton(R.id.button0, ORIENTATION_UNDEFINED)
        initButton(R.id.button1, ORIENTATION_NORMAL)
        initButton(R.id.button2, ORIENTATION_FLIP_HORIZONTAL)
        initButton(R.id.button3, ORIENTATION_ROTATE_180)
        initButton(R.id.button4, ORIENTATION_FLIP_VERTICAL)
        initButton(R.id.button5, ORIENTATION_TRANSPOSE)
        initButton(R.id.button6, ORIENTATION_ROTATE_90)
        initButton(R.id.button7, ORIENTATION_TRANSVERSE)
        initButton(R.id.button8, ORIENTATION_ROTATE_270)
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            startActivity(Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
            ))
        }
    }

    private fun initButton(buttonResId: Int, orientation: Int) {
        val button = findViewById(buttonResId) as Button
        button.text = getString(R.string.button_text, orientation)
        button.setOnClickListener {
            val outputFile = File(DIRECTORY, "$orientation.jpg")
            assets.open(getString(R.string.input_filename)).use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            outputFile.setOrientation(orientation)
            startActivity(Intent(ACTION_VIEW).setDataAndType(Uri.fromFile(outputFile), "image/*"))
        }
    }

    private fun File.setOrientation(orientation: Int) {
        ExifInterface(path).apply {
            setAttribute(TAG_ORIENTATION, orientation.toString())
            saveAttributes()
        }
    }
}

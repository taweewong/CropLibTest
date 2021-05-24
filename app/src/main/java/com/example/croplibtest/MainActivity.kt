package com.example.croplibtest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.yashoid.instacropper.InstaCropperView


class MainActivity : AppCompatActivity() {
    companion object {
        private const val PICK_IMAGE = 123
    }

    private val instaCropperView: InstaCropperView by lazy { findViewById(R.id.instaCropperView) }
    private val imageView: ImageView by lazy { findViewById(R.id.imageView) }
    private val cropButton: Button by lazy { findViewById(R.id.cropButton) }
    private val pickButton: Button by lazy { findViewById(R.id.pickButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pickButton.setOnClickListener {
            openGallery()
        }
        cropButton.setOnClickListener {
            instaCropperView.crop(
                View.MeasureSpec.makeMeasureSpec(1024, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            ) {
                imageView.setImageBitmap(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            when {
                data != null -> {
                    instaCropperView.setImageUri(data.data)
                    Log.d("DEBUG_TEST", data.data?.path ?: "no")
                }
                else -> toast("data is null")
            }
        }
    }

    private fun openGallery() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }
}
package com.example.fruitlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_create_fruit.*

class CreateFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"

        fun newInstance(context: Context) = Intent(context, CreateFruitActivity::class.java)
    }

    fun returnResult(imageResource: Int, name: String, summary: String) {
        val data = Intent().apply {
            putExtra(RESULT_IMAGE_RESOURCE, imageResource)
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_fruit)



        createFruitButton.setOnClickListener {
//            val name = editTextFruitName.text.toString()
//            val summary =
//            val imageResource = 1
            returnResult(1, editTextFruitName.text.toString(), editTextFruitSummary.text.toString())
        }

    }
}
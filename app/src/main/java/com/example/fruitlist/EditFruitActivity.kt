package com.example.fruitlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_fruit.*

class EditFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"
        const val RESULT_POSITION = "position"

        fun newInstance(context: Context) = Intent(context, EditFruitActivity::class.java)
    }

    fun returnResult(imageResource: Int, name: String, summary: String, position: Int) {
        val data = Intent().apply {
            putExtra(RESULT_IMAGE_RESOURCE, imageResource)
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
            putExtra(RESULT_POSITION, position)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_fruit)
        val position = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_POSITION_EXTRA, 0)
        val imageResource =
            intent.getIntExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA, 0)
        val name = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA)
        val summary = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA)
        editTextFruitName.setText(name)
        editTextSummary.setText(summary)
        editButton.setOnClickListener {
            returnResult(
                imageResource,
                editTextFruitName.text.toString(),
                editTextSummary.text.toString(),
                position
            )
        }

    }
}
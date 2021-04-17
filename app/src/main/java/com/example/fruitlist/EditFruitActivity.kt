package com.example.fruitlist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_fruit.*


class EditFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"
        const val RESULT_BENEFITS = "benefits"
        const val RESULT_POSITION = "position"
        const val RESULT_ACTION = "action"
        val IMAGE_PICK_CODE = 1000;

        val PERMISSION_CODE = 1001;
        var newImageResource: Uri = Uri.EMPTY

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            imageEditView.setImageURI(data?.data)
            newImageResource = data?.data!!
        }
    }

    fun returnEditResult(
        imageResource: Uri,
        name: String,
        summary: String,
        benefits: String,
        position: Int
    ) {
        val data = Intent().apply {
            putExtra(RESULT_ACTION, "edit")
            putExtra(RESULT_IMAGE_RESOURCE, imageResource.toString())
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
            putExtra(RESULT_BENEFITS, benefits)
            putExtra(RESULT_POSITION, position)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    fun returnDeleteResult(position: Int) {
        val data = Intent().apply {
            putExtra(RESULT_ACTION, "delete")
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
            intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA)
        newImageResource = Uri.parse(imageResource)
        val name = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA)
        val summary = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA)
        val benefits = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_BENEFITS_EXTRA)
        editTextFruitName.setText(name)
        editTextSummary.setText(summary)
        editTextBenefits.setText(benefits)
        imageEditView.setImageURI(newImageResource)

        imageEditButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        }
        editButton.setOnClickListener {
            returnEditResult(
                newImageResource,
                editTextFruitName.text.toString(),
                editTextSummary.text.toString(),
                editTextBenefits.text.toString(),
                position
            )
        }

        deleteButton.setOnClickListener {
            returnDeleteResult(position)
        }

    }
}
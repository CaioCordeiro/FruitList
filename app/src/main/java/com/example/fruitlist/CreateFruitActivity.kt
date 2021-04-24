package com.example.fruitlist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_create_fruit.*


class CreateFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"
        const val RESULT_BENEFITS = "benefits"

        val IMAGE_PICK_CODE = 1000;

        val PERMISSION_CODE = 1001;
        open var newImageResource: Uri = Uri.EMPTY

    }

    fun returnResult(imageResource: Uri, name: String, summary: String, benefits: String) {
        val data = Intent().apply {
            putExtra(RESULT_IMAGE_RESOURCE, imageResource.toString())
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
            putExtra(RESULT_BENEFITS, benefits)
        }
        setResult(RESULT_OK, data)
        finish()
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

            imageView.setImageURI(data?.data)
            newImageResource = data?.data!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_fruit)
        setSupportActionBar(mainToolbar as Toolbar?);
        supportActionBar?.title = "Adicionar Nova Fruit";
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageCreateButton.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
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

        createFruitButton.setOnClickListener {
            returnResult(
                newImageResource,
                editTextFruitName.text.toString(),
                editTextFruitSummary.text.toString(),
                editTextFruitBenefits.text.toString()
            )
        }


    }
}
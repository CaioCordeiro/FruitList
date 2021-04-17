package com.example.fruitlist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_fruit.*



class CreateFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"

        //image pick code
        val IMAGE_PICK_CODE = 1000;

        //Permission code
        val PERMISSION_CODE = 1001;
        open var newImageResource: Uri = Uri.EMPTY

        fun newInstance(context: Context) = Intent(context, CreateFruitActivity::class.java)
    }

    fun returnResult(imageResource: Uri, name: String, summary: String) {
        val data = Intent().apply {
            putExtra(RESULT_IMAGE_RESOURCE, imageResource.toString())
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
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
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
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

        imageCreateButton.setOnClickListener {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        createFruitButton.setOnClickListener {
            returnResult(newImageResource , editTextFruitName.text.toString(), editTextFruitSummary.text.toString())
        }


    }
}
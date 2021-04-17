package com.example.fruitlist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_fruit.*
import kotlinx.android.synthetic.main.activity_edit_fruit.editTextFruitName


class EditFruitActivity : AppCompatActivity() {
    companion object {
        const val RESULT_IMAGE_RESOURCE = "image_resource"
        const val RESULT_NAME = "name"
        const val RESULT_SUMMARY = "summary"
        const val RESULT_POSITION = "position"
        const val RESULT_ACTION = "action"
        //image pick code
        val IMAGE_PICK_CODE = 1000;

        //Permission code
        val PERMISSION_CODE = 1001;
        open var newImageResource: Uri = Uri.EMPTY

        fun newInstance(context: Context) = Intent(context, EditFruitActivity::class.java)
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

            imageEditView.setImageURI(data?.data)
            newImageResource = data?.data!!
        }
    }

    fun returnEditResult(imageResource: Uri, name: String, summary: String, position: Int) {
        val data = Intent().apply {
            putExtra(RESULT_ACTION, "edit")
            putExtra(RESULT_IMAGE_RESOURCE, imageResource.toString())
            putExtra(RESULT_NAME, name)
            putExtra(RESULT_SUMMARY, summary)
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
        val uriImageResource = Uri.parse(imageResource)
        val name = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA)
        val summary = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA)
        editTextFruitName.setText(name)
        editTextSummary.setText(summary)
        imageEditView.setImageURI(uriImageResource)
        imageEditButton.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        editButton.setOnClickListener {
            returnEditResult(
                newImageResource,
                editTextFruitName.text.toString(),
                editTextSummary.text.toString(),
                position
            )
        }

        deleteButton.setOnClickListener {
            returnDeleteResult(position)
        }

    }
}
package com.example.fruitlist

import android.R.*
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*


//import javax.management.ObjectName

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), FruitAdapter.OnItemClickListener {
    companion object {
        const val MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE = 1
        const val MAIN_ACTIVITY_EDIT_FRUIT_REQUEST_CODE = 2
        const val MAIN_ACTIVITY_EDIT_FRUIT_POSITION_EXTRA = "position"
        const val MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA = "name"
        const val MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA = "summary"
        const val MAIN_ACTIVITY_EDIT_FRUIT_BENEFITS_EXTRA = "benefits"
        const val MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA = "image_resource"

    }

    private var fruitList = mutableListOf<Fruit>()
    private val adapter = FruitAdapter(fruitList, this)
    private var isListAlpha = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolBar as Toolbar?);
        supportActionBar?.setDisplayShowTitleEnabled(false);
        supportActionBar?.title = "Lista de Frutas";


        recycle_view.adapter = adapter
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.setHasFixedSize(true)

        AddFruit.setOnClickListener {
            val createFruitActivity = Intent(this, CreateFruitActivity::class.java)
            startActivityForResult(createFruitActivity, MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE)
        }
        menu_button.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Filter")
            alertDialogBuilder.setNeutralButton("Sort Alpha") { dialog, which ->
                Toast.makeText(applicationContext,
                    "Sorted", Toast.LENGTH_SHORT).show()
                this.sortAlpha()
                isListAlpha = true
            }
            alertDialogBuilder.setNegativeButton("Sort Insertion") { dialog, which ->
                Toast.makeText(applicationContext,
                    "Sorted", Toast.LENGTH_SHORT).show()
                this.getFruits()
                isListAlpha = false
            }
            alertDialogBuilder.show()
        }
        this.getFruits()
//        this.populateList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE == requestCode) {
                data.apply {
                    val name = getStringExtra(CreateFruitActivity.RESULT_NAME)
                    val sumary = getStringExtra(CreateFruitActivity.RESULT_SUMMARY)
                    val benefits = getStringExtra(CreateFruitActivity.RESULT_BENEFITS)
                    val imageResource = getStringExtra(CreateFruitActivity.RESULT_IMAGE_RESOURCE)
                    val uriImageResource = Uri.parse(imageResource)
                    if (name != null && sumary != null && benefits != null)
                        insertItem(uriImageResource, name, sumary, benefits)
                }
            }
            if (MAIN_ACTIVITY_EDIT_FRUIT_REQUEST_CODE == requestCode) {
                data.apply {
                    val action = getStringExtra(EditFruitActivity.RESULT_ACTION)
                    val position = getIntExtra(EditFruitActivity.RESULT_POSITION, 0)
                    if (action != null && "delete".compareTo(action) == 0) {
                        removeItem(position)
                    } else {
                        val name = getStringExtra(EditFruitActivity.RESULT_NAME)
                        val summary = getStringExtra(EditFruitActivity.RESULT_SUMMARY)
                        val benefits = getStringExtra(EditFruitActivity.RESULT_BENEFITS)
                        val imageResource = getStringExtra(EditFruitActivity.RESULT_IMAGE_RESOURCE)
                        val uriImageResource = Uri.parse(imageResource)
                        editItem(uriImageResource, name, summary, position, benefits)
                    }

                }
            }
        }
    }

    private fun getFruits() {
        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.getFruits().observe(this, Observer { fruitSnapshot ->
            Log.i(TAG, "Received contacts from view model")
            fruitList.clear()
            fruitList.addAll(fruitSnapshot)
            adapter.notifyDataSetChanged()
        })
    }

    private fun sortAlpha(){
        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.getAlpha().observe(this, Observer { fruitSnapshot ->
            Log.i(TAG, "Received contacts from view model")
            fruitList.clear()
            fruitList.addAll(fruitSnapshot)
            adapter.notifyDataSetChanged()
            recycle_view.scrollToPosition(0)
        })
    }

    private fun insertItem(imageResource: Uri, name: String, summary: String, benefits: String) {
        val newItem = Fruit(
            imageResource,
            name,
            summary,
            benefits
        )
        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.insertFruit(0, newItem, isListAlpha).observe(this, Observer { fruitSnapshot ->
            Log.i(TAG, "Received contacts from view model")
            fruitList.clear()
            fruitList.addAll(fruitSnapshot)
            adapter.notifyItemInserted(0)
            recycle_view.scrollToPosition(0)
        })

    }

    private fun removeItem(index: Int) {
        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.removeFruit(index).observe(this, Observer { fruitSnapshot ->
            Log.i(TAG, "Received contacts from view model")
            fruitList.clear()
            fruitList.addAll(fruitSnapshot)
            adapter.notifyItemRemoved(index)
        })
    }

    private fun editItem(
        imageResource: Uri,
        name: String?,
        summary: String?,
        position: Int,
        benefits: String?
    ) {
        val newItem = Fruit(
            imageResource,
            name!!,
            summary!!,
            benefits!!
        )
        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.editFruit(position, newItem, isListAlpha).observe(this, Observer { fruitSnapshot ->
            Log.i(TAG, "Received contacts from view model")
            fruitList.clear()
            fruitList.addAll(fruitSnapshot)
            if(isListAlpha) {
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyItemChanged(position)
            }
        })
    }

    override fun onItemClick(position: Int) {
        val clickedItem = fruitList[position]
        val name = clickedItem.name
        Toast.makeText(this, "$name clicked", Toast.LENGTH_SHORT).show()
        val editFruitActivity = Intent(this, EditFruitActivity::class.java)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_POSITION_EXTRA, position)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA, clickedItem.name)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA, clickedItem.summary)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_BENEFITS_EXTRA, clickedItem.benefits)
        editFruitActivity.putExtra(
            MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA,
            clickedItem.imageResource.toString()
        )
        startActivityForResult(editFruitActivity, MAIN_ACTIVITY_EDIT_FRUIT_REQUEST_CODE)
    }

    fun populateList() {
        this.insertItem(Uri.EMPTY, "CCCCCC", "?? uma ma????1", "de fato uma ma????")
        this.insertItem(Uri.EMPTY, "BBBBBB", "?? uma ma????2", "de fato uma ma????1")
        this.insertItem(Uri.EMPTY, "ZZZZZZ", "?? uma ma????3", "de fato uma ma????2")
        this.insertItem(Uri.EMPTY, "AAAAAA", "?? uma ma????4", "de fato uma ma????3")
//        this.insertItem(Uri.EMPTY, "Ma????4", "?? uma ma????5", "de fato uma ma????4")
//        this.insertItem(Uri.EMPTY, "Ma????5", "?? uma ma????6", "de fato uma ma????5")
//        this.insertItem(Uri.EMPTY, "Ma????6", "?? uma ma????7", "de fato uma ma????6")
//        this.insertItem(Uri.EMPTY, "Ma????7", "?? uma ma????8", "de fato uma ma????7")
//        this.insertItem(Uri.EMPTY, "Ma????8", "?? uma ma????9", "de fato uma ma????8")
//        this.insertItem(Uri.EMPTY, "Ma????9", "?? uma ma????10", "de fato uma ma????9")

    }
}
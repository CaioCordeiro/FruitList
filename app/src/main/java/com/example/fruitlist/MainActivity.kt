package com.example.fruitlist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
//import javax.management.ObjectName
import kotlin.random.Random


class MainActivity : AppCompatActivity(), FruitAdapter.OnItemClickListener {
    companion object {
        const val MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE = 1
        const val MAIN_ACTIVITY_DELETE_FRUIT_REQUEST_CODE = 2
        const val MAIN_ACTIVITY_EDIT_FRUIT_REQUEST_CODE = 2
        const val MAIN_ACTIVITY_EDIT_FRUIT_POSITION_EXTRA = "position"
        const val MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA = "name"
        const val MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA = "summary"
        const val MAIN_ACTIVITY_EDIT_FRUIT_BENEFITS_EXTRA = "benefits"
        const val MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA = "image_resource"

        const val MAIN_ACTIVITY_INSERT_FRUIT_RESULT = "insert_fruit_extra"
    }

    //    private val fruitList = generateDummyList(100)
    private var fruitList = ArrayList<Fruit>()
    private val adapter = FruitAdapter(fruitList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycle_view.adapter = adapter
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.hasFixedSize()
        AddFruit.setOnClickListener {
            val createFruitActivity = Intent(this, CreateFruitActivity::class.java)
            startActivityForResult(createFruitActivity, MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE)
        }
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
                    if (name != null && sumary != null&& benefits != null)
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

//    fun requestResult() {
//
//        startActivityForResult(CreateFruitActivity.newInstance(), MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE)
//    }

    fun insertItem(imageResource: Uri, name: String, summary: String, benefits: String) {
//        val index = Random.nextInt(8)
//        val newItem = Fruit(
//            R.drawable.ic_baseline_widgets_24, "New item at position $index",
//            "Fruta boa, tem gosto de tamarindo, mas tem cor de limão e parece de laranja"
//        )
        val newItem = Fruit(
            imageResource,
            name,
            summary,
            benefits
        )
        fruitList.add(0, newItem)
        adapter.notifyItemInserted(0)
    }

    fun removeItem(index: Int) {
        fruitList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    fun editItem(imageResource: Uri, name: String?, summary: String?, position: Int, benefits: String?) {
        val clickedItem = fruitList[position]
        if (name != null)
            clickedItem.name = name
        if (summary != null)
            clickedItem.summary = summary
        if (benefits != null)
            clickedItem.benefits = benefits
        if (imageResource != null)
            clickedItem.imageResource = imageResource
        adapter.notifyItemChanged(position)
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
    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        savedInstanceState.putParcelableArrayList("FruitList", fruitList)

        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val check: ArrayList<Fruit>? = savedInstanceState.getParcelableArrayList("FruitList")
        if (check != null) {
            fruitList = check
        }

    }
}
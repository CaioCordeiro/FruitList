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
                    val imageResource = getStringExtra(CreateFruitActivity.RESULT_IMAGE_RESOURCE)
                    val uriImageResource = Uri.parse(imageResource)
                    if (name != null && sumary != null)
                        insertItem(uriImageResource, name, sumary)
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
                        val imageResource = getStringExtra(EditFruitActivity.RESULT_IMAGE_RESOURCE)
                        val uriImageResource = Uri.parse(imageResource)
                        editItem(uriImageResource, name, summary, position)
                    }

                }
            }
        }
    }

//    fun requestResult() {
//
//        startActivityForResult(CreateFruitActivity.newInstance(), MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE)
//    }

    fun insertItem(imageResource: Uri, name: String, summary: String) {
//        val index = Random.nextInt(8)
//        val newItem = Fruit(
//            R.drawable.ic_baseline_widgets_24, "New item at position $index",
//            "Fruta boa, tem gosto de tamarindo, mas tem cor de limão e parece de laranja"
//        )
        val newItem = Fruit(
            imageResource,
            name,
            summary
        )
        fruitList.add(0, newItem)
        adapter.notifyItemInserted(0)
    }

    fun removeItem(index: Int) {
        fruitList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    fun editItem(imageResource: Uri, name: String?, summary: String?, position: Int) {
        val clickedItem = fruitList[position]
        if (name != null)
            clickedItem.name = name
        if (summary != null)
            clickedItem.summary = summary
        if (imageResource != null)
            clickedItem.imageResource = imageResource
        adapter.notifyItemChanged(position)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = fruitList[position]
//        clickedItem.name = "Clicado"
//        adapter.notifyItemChanged(position)
        val editFruitActivity = Intent(this, EditFruitActivity::class.java)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_POSITION_EXTRA, position)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_NAME_EXTRA, clickedItem.name)
        editFruitActivity.putExtra(MAIN_ACTIVITY_EDIT_FRUIT_SUMMARY_EXTRA, clickedItem.summary)
        editFruitActivity.putExtra(
            MAIN_ACTIVITY_EDIT_FRUIT_IMAGE_RESOURCE_EXTRA,
            clickedItem.imageResource.toString()
        )
        startActivityForResult(editFruitActivity, MAIN_ACTIVITY_EDIT_FRUIT_REQUEST_CODE)
    }

//    private fun generateDummyList(size: Int): ArrayList<Fruit> {
//        val list = ArrayList<Fruit>()
//        for (i in 0 until size) {
//            val drawable = when (i % 3) {
//                0 -> R.drawable.ic_baseline_wash_24
//                1 -> R.drawable.ic_baseline_widgets_24
//                else -> R.drawable.ic_baseline_wifi_24
//            }
//            val item = Fruit(
//                drawable,
//                "fruta $i",
//                "É uma fruta ai qualquer uma na vdd é só um teste isso aq ent tanto faz na verdade"
//            )
//            list += item
//        }
//        return list
//    }
    //Use onSaveInstanceState(Bundle) and onRestoreInstanceState

    //Use onSaveInstanceState(Bundle) and onRestoreInstanceState
    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList("FruitList", fruitList)
        // etc.
        super.onSaveInstanceState(savedInstanceState)
    }

//onRestoreInstanceState

    //onRestoreInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        val check: ArrayList<Fruit>? = savedInstanceState.getParcelableArrayList("FruitList")
        if (check != null) {
            fruitList = check
        }

    }
}
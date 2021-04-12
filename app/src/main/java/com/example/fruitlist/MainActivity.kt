package com.example.fruitlist

import android.app.Activity
import android.content.Intent
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
                    val imageResouce = getIntExtra(CreateFruitActivity.RESULT_IMAGE_RESOURCE, 1)
                    if (name != null && sumary != null)
                        insertItem(imageResouce, name, sumary)
                }
            }
        }
    }

//    fun requestResult() {
//
//        startActivityForResult(CreateFruitActivity.newInstance(), MAIN_ACTIVITY_INSERT_FRUIT_REQUEST_CODE)
//    }

    fun insertItem(imageResource: Int, name: String, summary: String) {
//        val index = Random.nextInt(8)
//        val newItem = Fruit(
//            R.drawable.ic_baseline_widgets_24, "New item at position $index",
//            "Fruta boa, tem gosto de tamarindo, mas tem cor de limão e parece de laranja"
//        )
        val newItem = Fruit(
            R.drawable.ic_baseline_widgets_24,
            name,
            summary
        )
        fruitList.add(0, newItem)
        adapter.notifyItemInserted(0)
    }

    fun removeItem(view: View) {
        val index = Random.nextInt(8)
        fruitList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = fruitList[position]
        clickedItem.name = "Clicado"
        adapter.notifyItemChanged(position)
    }

    private fun generateDummyList(size: Int): ArrayList<Fruit> {
        val list = ArrayList<Fruit>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_baseline_wash_24
                1 -> R.drawable.ic_baseline_widgets_24
                else -> R.drawable.ic_baseline_wifi_24
            }
            val item = Fruit(
                drawable,
                "fruta $i",
                "É uma fruta ai qualquer uma na vdd é só um teste isso aq ent tanto faz na verdade"
            )
            list += item
        }
        return list
    }
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
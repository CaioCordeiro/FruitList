package com.example.fruitlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val fruitList = generateDummyList(100)
    private val adapter = FruitAdapter(fruitList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycle_view.adapter = adapter
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.hasFixedSize()
    }

    fun insertItem(view: View) {
        val index = Random.nextInt(8)
        val newItem = Fruit(
            R.drawable.ic_baseline_widgets_24, "New item at position $index",
            "Fruta boa, tem gosto de tamarindo, mas tem cor de limão e parece de laranja"
        )
        fruitList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    fun removeItem(view: View) {
        val index = Random.nextInt(8)
        fruitList.removeAt(index)
        adapter.notifyItemRemoved(index)
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
}
package com.example.fruitlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fruitList = generateDummyList(100)

        recycle_view.adapter = FruitAdapter(fruitList)
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.hasFixedSize()
    }

    private fun generateDummyList(size: Int): List<Fruit> {
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
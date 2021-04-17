package com.example.fruitlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fruit_item.view.*

class FruitAdapter(private val fruitList: List<Fruit>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FruitAdapter.FruitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fruit_item,
            parent, false
        )

        return FruitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        val currentItem = fruitList[position]

        holder.imageView.setImageURI(currentItem.imageResource)
        holder.textViewName.text = currentItem.name
        holder.textViewSummary.text = currentItem.summary
    }

    override fun getItemCount() = fruitList.size

    inner class FruitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.image_view
        val textViewName: TextView = itemView.text_view_name
        val textViewSummary: TextView = itemView.text_view_summary

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
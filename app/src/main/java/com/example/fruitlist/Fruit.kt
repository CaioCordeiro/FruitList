package com.example.fruitlist
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit (val imageResource: Int, val name:String, val summary:String):Parcelable
package com.example.fruitlist
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit (var imageResource: Int, var name:String, var summary:String):Parcelable
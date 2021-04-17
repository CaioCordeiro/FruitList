package com.example.fruitlist
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit (var imageResource: Uri, var name:String, var summary:String):Parcelable
package com.example.fruitlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel : ViewModel() {
    private val fruitsLiveData: MutableLiveData<MutableList<Fruit>>

    init {
        Log.i(TAG, "init")
        fruitsLiveData = MutableLiveData()
        fruitsLiveData.value = mutableListOf<Fruit>()
    }

    fun getFruits(): LiveData<MutableList<Fruit>> {
        return fruitsLiveData
    }

    fun insertFruit(index: Int, fruit: Fruit): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value
        fruits?.add(index, fruit)
        fruitsLiveData.value = fruits
        return fruitsLiveData
    }

    fun removeFruit(index: Int): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value
        fruits?.removeAt(index)
        fruitsLiveData.value = fruits
        return fruitsLiveData
    }

    fun editFruit(index: Int, fruit: Fruit): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value!!
        fruits[index] = fruit
        fruitsLiveData.value = fruits
        return fruitsLiveData
    }
}
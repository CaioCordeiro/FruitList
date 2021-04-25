package com.example.fruitlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel : ViewModel() {
    private val fruitsLiveData: MutableLiveData<MutableList<Fruit>>
    private val fruitsLiveDataAlpha: MutableLiveData<MutableList<Fruit>>

    init {
        Log.i(TAG, "init")
        fruitsLiveData = MutableLiveData()
        fruitsLiveData.value = mutableListOf<Fruit>()
        fruitsLiveDataAlpha = MutableLiveData()
        fruitsLiveDataAlpha.value = mutableListOf<Fruit>()

    }


    fun getFruits(): LiveData<MutableList<Fruit>> {
        return fruitsLiveData
    }

    fun getAlpha(): LiveData<MutableList<Fruit>> {
        return fruitsLiveDataAlpha
    }

    private fun getFruitsValue(): MutableList<Fruit> {
        return fruitsLiveData.value!!
    }

    fun updateAlpha() {
        val nameComparator = Comparator { f1: Fruit, f2: Fruit ->
            ((f1.name.compareTo(f2.name)) * -1)
        }
        fruitsLiveDataAlpha.value = getFruitsValue()
        fruitsLiveDataAlpha.value = (fruitsLiveDataAlpha.value!!.sortedWith(compareBy { it.name }) as MutableList<Fruit>)
        Log.i(TAG, fruitsLiveDataAlpha.value.toString())
    }

    fun insertFruit(index: Int, fruit: Fruit, isAlpha: Boolean): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value
        fruits?.add(index, fruit)
        fruitsLiveData.value = fruits
        this.updateAlpha()
        if (isAlpha){
            return this.getAlpha()
        }
        return fruitsLiveData
    }

    fun removeFruit(index: Int): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value
        fruits?.removeAt(index)
        fruitsLiveData.value = fruits
        this.updateAlpha()
        return fruitsLiveData
    }

    fun editFruit(index: Int, fruit: Fruit, isAlpha: Boolean): LiveData<MutableList<Fruit>> {
        val fruits = fruitsLiveData.value!!
        fruits[index] = fruit
        fruitsLiveData.value = fruits
        this.updateAlpha()
        if (isAlpha){
            return this.getAlpha()
        }
        return fruitsLiveData
    }


}
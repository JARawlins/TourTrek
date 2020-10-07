package com.example.tourtrek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyToursViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the My Tours Fragment"
    }
    val text: LiveData<String> = _text
}
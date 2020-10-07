package com.example.tourtrek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TourMarketViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Tour Market Fragment"
    }
    val text: LiveData<String> = _text
}
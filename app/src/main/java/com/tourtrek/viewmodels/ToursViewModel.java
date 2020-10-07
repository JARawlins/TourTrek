package com.tourtrek.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToursViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ToursViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Tours fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.tourtrek.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TourMaketViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TourMaketViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Tour Market fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
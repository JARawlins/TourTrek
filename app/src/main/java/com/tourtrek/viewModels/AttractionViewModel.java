package com.tourtrek.viewModels;

import com.tourtrek.data.Attraction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttractionViewModel extends ViewModel {

    private MutableLiveData<Attraction> selectedAttraction = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNewAttraction = new MutableLiveData<>();

    public Attraction getSelectedAttraction() {
        return selectedAttraction.getValue();
    }

    public void setSelectedAttraction(Attraction selectedTour) {
        this.selectedAttraction.setValue(selectedTour);
    }

    public Boolean isNewAttraction() {
        if (isNewAttraction.getValue() == null) {
            isNewAttraction.setValue(false);
        }
        return isNewAttraction.getValue();
    }

    public void setIsNewAttraction(Boolean isNewAttraction) {
        this.isNewAttraction.setValue(isNewAttraction);
    }
}
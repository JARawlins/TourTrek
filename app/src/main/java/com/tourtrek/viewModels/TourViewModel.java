package com.tourtrek.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tourtrek.data.Tour;

public class TourViewModel extends ViewModel {

    private MutableLiveData<Tour> selectedTour = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNewTour = new MutableLiveData<>();

    public Tour getSelectedTour() {
        return selectedTour.getValue();
    }

    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour.setValue(selectedTour);
    }

    public Boolean isNewTour() {
        if (isNewTour.getValue() == null) {
            isNewTour.setValue(false);
        }
        return isNewTour.getValue();
    }

    public void setIsNewTour(Boolean isNewTour) {
        this.isNewTour.setValue(isNewTour);
    }
}

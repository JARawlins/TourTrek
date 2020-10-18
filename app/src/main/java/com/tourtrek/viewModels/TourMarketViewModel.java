package com.tourtrek.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tourtrek.data.Tour;

public class TourMarketViewModel extends ViewModel {

    private MutableLiveData<Tour> selectedTour = new MutableLiveData<>();

    public Tour getSelectedTour() {
        return selectedTour.getValue();
    }

    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour.setValue(selectedTour);
    }
}

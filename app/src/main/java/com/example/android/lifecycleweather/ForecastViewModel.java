package com.example.android.lifecycleweather;

import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.data.ForecastRepository;
import com.example.android.lifecycleweather.data.Status;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ForecastViewModel extends ViewModel {
    private ForecastRepository mRepository;
    private LiveData<List<ForecastItem>> mForecastResults;
    private LiveData<Status> mLoadingStatus;

    public ForecastViewModel() {
        mRepository = new ForecastRepository();
        mForecastResults = mRepository.getForecastResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadForecastResults(String location, String units) {
        mRepository.loadForecastResults(location, units);
    }

    public LiveData<List<ForecastItem>> getForecastResults() {
        return mForecastResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}

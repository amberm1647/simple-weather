package com.example.android.lifecycleweather.data;

import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.MultiTransformation;
import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ForecastRepository implements ForecastAsyncTask.Callback {
    private static final String TAG = ForecastRepository.class.getSimpleName();
    private MutableLiveData<List<ForecastItem>> mForecastResults;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentLocation;
    private String mCurrentUnits;

    public ForecastRepository() {
        mForecastResults = new MutableLiveData<>();
        mForecastResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<ForecastItem>> getForecastResults() {
        return mForecastResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onSearchFinished(List<ForecastItem> forecastResults) {
        mForecastResults.setValue(forecastResults);
        if(forecastResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String location, String units) {
        return !TextUtils.equals(location, mCurrentLocation)
                || !TextUtils.equals(units, mCurrentUnits);
    }

    public void loadForecastResults(String location, String units) {
        if(shouldExecuteSearch(location, units)) {
            mCurrentLocation = location;
            mCurrentUnits = units;
            String url = OpenWeatherMapUtils.buildForecastURL(location, units);
            mForecastResults.setValue(null);
            Log.d(TAG, "executing search with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new ForecastAsyncTask(this).execute(url);
            Log.d(TAG, "fetching new search results");
        } else {
            Log.d(TAG, "using cached search results");
        }
    }
}

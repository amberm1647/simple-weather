package com.example.android.lifecycleweather.data;

import android.os.AsyncTask;

import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;
import com.example.android.lifecycleweather.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

public class ForecastAsyncTask extends AsyncTask<String, Void, String> {
    private Callback mCallback;

    public interface Callback {
        void onSearchFinished(List<ForecastItem> forecastResults);
    }

    public ForecastAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String searchResults = null;
        try {
            searchResults = NetworkUtils.doHTTPGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        List<ForecastItem> forecastResults = null;
        if (s != null) {
            forecastResults = OpenWeatherMapUtils.parseForecastJSON(s);
        }
        mCallback.onSearchFinished(forecastResults);
    }
}

package com.example.android.lifecycleweather;

import android.content.Intent;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;


import com.bumptech.glide.Glide;
import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;

import java.text.DateFormat;
import java.lang.String;

public class ForecastItemDetailActivity extends AppCompatActivity {

    private TextView mDateTV;
    private TextView mTempDescriptionTV;
    private TextView mLowHighTempTV;
    private TextView mWindTV;
    private TextView mHumidityTV;
    private ImageView mWeatherIconIV;

    private ForecastItem mForecastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_item_detail);

        mDateTV = findViewById(R.id.tv_date);
        mTempDescriptionTV = findViewById(R.id.tv_temp_description);
        mLowHighTempTV = findViewById(R.id.tv_low_high_temp);
        mWindTV = findViewById(R.id.tv_wind);
        mHumidityTV = findViewById(R.id.tv_humidity);
        mWeatherIconIV = findViewById(R.id.iv_weather_icon);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OpenWeatherMapUtils.EXTRA_FORECAST_ITEM)) {
            mForecastItem = (ForecastItem)intent.getSerializableExtra(
                    OpenWeatherMapUtils.EXTRA_FORECAST_ITEM
            );
            fillInLayout(mForecastItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getLocation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default)
        );
    }

    private String getUnitsAbbr() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String units = preferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default)
        );

        switch(units) {
            case "imperial":
                return "F";
            case "metric":
                return "C";
            case "kelvin":
                return "K";
            default:
                return "";
        }
    }

    public void shareForecast() {
        if (mForecastItem != null) {

            String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(mForecastItem.dateTime);
            String shareText = getString(R.string.forecast_item_share_text,
                    getLocation(), dateString,
                    mForecastItem.temperature, getUnitsAbbr(),
                    mForecastItem.description);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
        }
    }

    private void fillInLayout(ForecastItem forecastItem) {
        String dateString = DateFormat.getDateTimeInstance().format(forecastItem.dateTime);
        String detailString = getString(R.string.forecast_item_details, forecastItem.temperature,
                getUnitsAbbr(), forecastItem.description);
        String lowHighTempString = getString(R.string.forecast_item_low_high_temp,
                forecastItem.temperatureLow, forecastItem.temperatureHigh,
                getUnitsAbbr());

        String windString = getString(R.string.forecast_item_wind, forecastItem.windSpeed,
                forecastItem.windDirection);
        String humidityString = getString(R.string.forecast_item_humidity, forecastItem.humidity);
        String iconURL = OpenWeatherMapUtils.buildIconURL(forecastItem.icon);

        mDateTV.setText(dateString);
        mTempDescriptionTV.setText(detailString);
        mLowHighTempTV.setText(lowHighTempString);
        mWindTV.setText(windString);
        mHumidityTV.setText(humidityString);
        Glide.with(this).load(iconURL).into(mWeatherIconIV);
    }
}

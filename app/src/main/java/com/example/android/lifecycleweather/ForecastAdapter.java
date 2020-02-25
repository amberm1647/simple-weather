package com.example.android.lifecycleweather;

import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private static final String TAG = ForecastAdapter.class.getSimpleName();

    private List<ForecastItem> mForecastItems;
    private OnForecastItemClickListener mForecastItemClickListener;

    public interface OnForecastItemClickListener {
        void onForecastItemClick(ForecastItem forecastItem);
    }

    public ForecastAdapter(OnForecastItemClickListener clickListener) {
        mForecastItemClickListener = clickListener;
    }

    public void updateForecastItems(List<ForecastItem> forecastItems) {
        mForecastItems = forecastItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastItems != null) {
            return mForecastItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        holder.bind(mForecastItems.get(position));
    }


    class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mForecastDateTV;
        private TextView mForecastTempDescriptionTV;
        private ImageView mWeatherIconIV;

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastDateTV = itemView.findViewById(R.id.tv_forecast_date);
            mForecastTempDescriptionTV = itemView.findViewById(R.id.tv_forecast_temp_description);
            mWeatherIconIV = itemView.findViewById(R.id.iv_weather_icon);
            itemView.setOnClickListener(this);
        }

        private String getUnitsAbbr() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mForecastTempDescriptionTV.getContext());
            String units = preferences.getString(mForecastTempDescriptionTV.getContext().getString(R.string.pref_units_key),
                    mForecastDateTV.getContext().getString(R.string.pref_units_default));

            Log.d(TAG, "binding units:"+units);

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

        public void bind(ForecastItem forecastItem) {
            String dateString = DateFormat.getDateTimeInstance().format(forecastItem.dateTime);
            String detailString = mForecastTempDescriptionTV.getContext().getString(
                    R.string.forecast_item_details, forecastItem.temperature,
                    getUnitsAbbr(), forecastItem.description
            );
            String iconURL = OpenWeatherMapUtils.buildIconURL(forecastItem.icon);
            mForecastDateTV.setText(dateString);
            mForecastTempDescriptionTV.setText(detailString);
            Glide.with(mWeatherIconIV.getContext()).load(iconURL).into(mWeatherIconIV);
        }

        @Override
        public void onClick(View v) {
            ForecastItem forecastItem = mForecastItems.get(getAdapterPosition());
            mForecastItemClickListener.onForecastItemClick(forecastItem);
        }
    }
}

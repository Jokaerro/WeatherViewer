package pro.games_box.weatherviewer.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.db.WeatherContract;
import pro.games_box.weatherviewer.db.CityContract;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import pro.games_box.weatherviewer.ui.adapter.CityAdapter;
import pro.games_box.weatherviewer.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TESLA on 16.04.2017.
 */

public class WeatherFragment extends BaseFragment implements Callback{
    private CityAdapter mCityAdapter;
    private Call<WeatherResponce> mWeatherResponseCall;

    @BindView(R.id.relative_ll) RelativeLayout relative_ll;
    @BindView(R.id.city_list) RecyclerView city_recycler;

    public static WeatherFragment newInstance() {
        final WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    // Projection and column indices values
    private static final String[] NOTIFY_CITY_PROJECTION = new String[]{
            CityContract.CityEntry.COLUMN_CITY_SETTING,
            CityContract.CityEntry.COLUMN_CITY_NAME,
            CityContract.CityEntry.COLUMN_COORD_LAT,
            CityContract.CityEntry.COLUMN_COORD_LONG
    };

    private static final int INDEX_CITY_SETTING = 0;
    private static final int INDEX_CITY_NAME = 1;
    private static final int INDEX_COORD_LAT = 2;
    private static final int INDEX_COORD_LONG = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, rootView);

        Toolbar toolbar = ((MainActivity) getActivity()).mToolbar;

        Cursor cursor = getActivity().getContentResolver()
                .query(CityContract.CityEntry.buildCityWithLastWeather(),
                        new String[]{"city."+ CityContract.CityEntry._ID,
                                CityContract.CityEntry.COLUMN_CITY_NAME,
                                WeatherContract.WeatherEntry.COLUMN_DATE,
                                WeatherContract.WeatherEntry.COLUMN_DEGREES,
                                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                                WeatherContract.WeatherEntry.COLUMN_LOC_KEY,
                                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED},
                        null, null, null);
        mCityAdapter = new CityAdapter(getActivity(), cursor, WeatherFragment.this);

        city_recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        city_recycler.setLayoutManager(llm);

        city_recycler.setAdapter(mCityAdapter);

        return rootView;
    }

    public void weatherCall(final WeatherResponce weather) {
        mWeatherResponseCall = Api.getApiService().getWeather(weather.getBdCityName(), "metric", "ru", getString(R.string.APIKEY));
        mWeatherResponseCall.enqueue(new Callback<WeatherResponce>() {
            @Override
            public void onResponse(Call<WeatherResponce> call, Response<WeatherResponce> response) {
                if (call.equals(mWeatherResponseCall)) {
                    WeatherResponce weatherResponse = ((Response<WeatherResponce>) response).body();
                    ContentValues weatherValue = new ContentValues();
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, weather.getBdCityId());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_DATE, weatherResponse.getDatetime());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, weatherResponse.getWeather().get(0).getDescription());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherResponse.getWeather().get(0).getId());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, weatherResponse.getMainConditions().getTempMin());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, weatherResponse.getMainConditions().getTempMax());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, weatherResponse.getMainConditions().getHumidity());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, weatherResponse.getMainConditions().getPressure());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, weatherResponse.getWind().getSpeed());
                    weatherValue.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, weatherResponse.getWind().getDegree());
                    getActivity().getContentResolver()
                            .insert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValue);
                    notifyWeather();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponce> call, Throwable t) {
                showToast("onFail");
            }
        });
    }

    @OnClick(R.id.fab)
    public void addCity() {
        CommonUtils.createInputDialog(getActivity(), InputType.TYPE_CLASS_TEXT,
                new CommonUtils.InputDialogListener() {
                    @Override
                    public void onInput(AlertDialog dialog, String text) {
                        dialog.setOnDismissListener(null);
                        // Insert the new weather information into the database
                        ContentValues cityValue = new ContentValues();
                        cityValue.put(CityContract.CityEntry.COLUMN_CITY_NAME, text);
                        dialog.getContext().getContentResolver()
                                .insert(CityContract.CityEntry.CONTENT_URI, cityValue);
                        notifyWeather();
                    }
                });

    }

    public void notifyWeather() {
        Cursor cursor = getActivity().getContentResolver()
                .query(CityContract.CityEntry.buildCityWithLastWeather(),
                        new String[]{"city."+ CityContract.CityEntry._ID,
                                CityContract.CityEntry.COLUMN_CITY_NAME,
                                WeatherContract.WeatherEntry.COLUMN_DATE,
                                WeatherContract.WeatherEntry.COLUMN_DEGREES,
                                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                                WeatherContract.WeatherEntry.COLUMN_LOC_KEY,
                                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED},
                        null, null, null);
        mCityAdapter.swapCursor(cursor);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
//            if (call.equals(mForecastResponseCall)) {
//            } else {
//                ApiError error = ErrorUtils.parseError(response);
//                String errorStr = "";
//                if (error != null) errorStr = error.getDescription();
//
//            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        showToast("Response fail, check internet please");
    }
}

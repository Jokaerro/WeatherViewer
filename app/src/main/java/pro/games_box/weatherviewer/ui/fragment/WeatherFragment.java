package pro.games_box.weatherviewer.ui.fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import pro.games_box.weatherviewer.api.ApiError;
import pro.games_box.weatherviewer.api.ErrorUtils;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.db.WeatherContract;
import pro.games_box.weatherviewer.db.CityContract;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.service.WeatherSync;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import pro.games_box.weatherviewer.ui.adapter.CityAdapter;
import pro.games_box.weatherviewer.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TESLA on 16.04.2017.
 */

public class WeatherFragment extends BaseFragment implements Callback, LoaderManager.LoaderCallbacks<Cursor>{
    private static final int CITY_LOADER_ID = 10;

    private CityAdapter cityAdapter;
    private Call<WeatherResponce> weatherResponseCall;
    private DataMapper dataMapper = new DataMapper();
    private BroadcastReceiver internetReceiver;

    @BindView(R.id.relative_ll) RelativeLayout relativelayout;
    @BindView(R.id.city_list) RecyclerView cityRecycler;

    public static WeatherFragment newInstance() {
        final WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    // Projection and column indices values
    private static final String[] NOTIFY_CITY_PROJECTION = new String[]{
            "city."+ CityContract.CityEntry._ID,
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
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, rootView);

        Toolbar toolbar = ((MainActivity) getActivity()).toolbar;

        registerInternetReceiver();

        getLoaderManager().initLoader(CITY_LOADER_ID, null, this);
        cityAdapter = new CityAdapter(getActivity(), null, WeatherFragment.this);

        cityRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        cityRecycler.setLayoutManager(llm);

        cityRecycler.setAdapter(cityAdapter);

        notifyWeather();
        return rootView;
    }

    public void weatherCall(final WeatherResponce weather) {
        weatherResponseCall = Api.getApiService().getWeather(weather.getBdCityName(), "metric", "ru", getString(R.string.APIKEY));
        weatherResponseCall.enqueue(new Callback<WeatherResponce>() {
            @Override
            public void onResponse(Call<WeatherResponce> call, Response<WeatherResponce> response) {
                if (call.equals(weatherResponseCall)) {
                    WeatherResponce weatherResponse = ((Response<WeatherResponce>) response).body();
                    if(response.isSuccessful()) {
                        ContentValues weatherValue = dataMapper.fromWeatherResponse(weatherResponse, weather.getBdCityId());
                        getActivity().getContentResolver()
                                .insert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValue);
                    } else {
                        ApiError error = ErrorUtils.parseError(response);

                        getContext().getContentResolver().delete(CityContract.CityEntry.CONTENT_URI,
                                CityContract.CityEntry.COLUMN_CITY_NAME + " == ?",
                                new String[]{weather.getBdCityName()});
                        showToast("Error: " + error.getMessage());
                    }
                    notifyWeather();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponce> call, Throwable t) {
                showToast("Response fail, check internet please");
            }
        });
    }

    private void onSyncWeather() {
        Intent intent = new Intent(getContext(), WeatherSync.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WeatherSync.TABLE, WeatherContract.WeatherEntry.TABLE_NAME);
        bundle.putSerializable(WeatherSync.CITY_ID, "0");
        intent.putExtra(WeatherSync.DATA_PARAM, bundle);
        getContext().startService(intent);
    }

    @OnClick(R.id.fab)
    public void addCity() {
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.action_add_city))
                .content(getString(R.string.action_add_city_msg))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.action_add_city_hint), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Insert the new weather information into the database
                        ContentValues cityValue = new ContentValues();
                        cityValue.put(CityContract.CityEntry.COLUMN_CITY_NAME, input.toString());
                        dialog.getContext().getContentResolver()
                                .insert(CityContract.CityEntry.CONTENT_URI, cityValue);
                        notifyWeather();
                    }
                }).show();
    }

    public void notifyWeather() {
        onSyncWeather();
        getContext().getContentResolver().notifyChange(CityContract.CityEntry.buildCityWithLastWeather(), null, false);
    }

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        showToast("Response fail, check internet please");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CITY_LOADER_ID) {
            return new CursorLoader(getContext(), CityContract.CityEntry.buildCityWithLastWeather(),
                    NOTIFY_CITY_PROJECTION,
                    null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cityAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cityAdapter.swapCursor(null);
    }

    private void registerInternetReceiver()
    {
        if (this.internetReceiver != null) return;
        this.internetReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive (Context context, Intent intent)
            {
                if (isInternetAvailable())
                    notifyWeather();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction (ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver (internetReceiver, filter);
    }

    private boolean isInternetAvailable()
    {
        try
        {
            return (Runtime.getRuntime().exec ("ping -c 1 google.com").waitFor() == 0);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}

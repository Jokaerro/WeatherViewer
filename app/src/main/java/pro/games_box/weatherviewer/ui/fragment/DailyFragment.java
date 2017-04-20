package pro.games_box.weatherviewer.ui.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.api.ApiError;
import pro.games_box.weatherviewer.api.ErrorUtils;
import pro.games_box.weatherviewer.db.DailyContract;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.model.response.DailyForecastResponse;
import pro.games_box.weatherviewer.service.WeatherSync;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import pro.games_box.weatherviewer.ui.adapter.DailyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TESLA on 16.04.2017.
 */

public class DailyFragment extends BaseFragment implements Callback, SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>{
    public static final String WEATHER_PREFERENCES = "weatherSettings";
    public static final String WEATHER_FORECAST_DAYS = "days";
    private static final int DAILY_LOADER_ID = 12;
    public final static String CITY = "CITY";
    public final static String CITY_ID = "CITY_ID";
    private DailyAdapter dailyAdapter;
    private Call<DailyForecastResponse> dailyResponseCall;
    private String city;
    private String cityId;
    private DataMapper dataMapper = new DataMapper();
    private SharedPreferences sharedSettings;

    @BindView(R.id.city) TextView cityTv;
    @BindView(R.id.daily_recycler) RecyclerView dailyRecycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    public static DailyFragment newInstance(int cityId, String cityName) {
        final DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putInt(CITY_ID, cityId);
        args.putString(CITY, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, rootView);

        sharedSettings = getActivity().getSharedPreferences(WEATHER_PREFERENCES, Context.MODE_PRIVATE);

        city = getArguments().getString(CITY);
        cityId = String.format(Locale.US, "%d", getArguments().getInt(CITY_ID));
        cityTv.setText(String.format(Locale.US, getContext().getString(R.string.weather_forecast), city));

        Toolbar toolbar = ((MainActivity) getActivity()).toolbar;
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(DAILY_LOADER_ID, null, this);
        dailyAdapter = new DailyAdapter(getActivity(), null, DailyFragment.this);

        dailyRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        dailyRecycler.setLayoutManager(llm);

        dailyRecycler.setAdapter(dailyAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        notifyWeather(sharedSettings.getInt(WEATHER_FORECAST_DAYS, 5));
        return rootView;
    }

    private void dailyCall(String city) {
        dailyResponseCall = Api.getApiService().getDaily(city, "7", "metric", "ru", getString(R.string.APIKEY));
        dailyResponseCall.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            if (call.equals(dailyResponseCall)) {
                DailyForecastResponse dailyResponse = ((Response<DailyForecastResponse>) response).body();
                for (int i = 0; i < dailyResponse.getDaily().size(); i++) {
                    ContentValues dailyValue = dataMapper.fromDaily(dailyResponse.getDaily().get(i), cityId);

                    if(getActivity() != null)
                        getActivity().getContentResolver()
                            .insert(DailyContract.DailyEntry.CONTENT_URI, dailyValue);
                }
                notifyWeather(sharedSettings.getInt(WEATHER_FORECAST_DAYS, 5));
            }
        } else {
            ApiError error = ErrorUtils.parseError(response);
            if (error != null)
                showToast(error.getMessage());
        }
    }

    private void onSyncWeather() {
        Intent intent = new Intent(getContext(), WeatherSync.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WeatherSync.TABLE, DailyContract.DailyEntry.TABLE_NAME);
        bundle.putSerializable(WeatherSync.CITY_ID, cityId);
        intent.putExtra(WeatherSync.DATA_PARAM, bundle);
        getContext().startService(intent);
    }

    public void notifyWeather(int days) {
        onSyncWeather();
        Calendar c = Calendar.getInstance();
        long startTime = c.getTimeInMillis() / 1000;
        c.add(Calendar.DATE, days);
        long endTime = c.getTimeInMillis() / 1000;
        if(getContext() != null)
            getContext().getContentResolver().notifyChange(DailyContract.DailyEntry.buildDailyWeather (cityId, startTime, endTime), null, false);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        showToast("Response fail, check internet please");
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                swipeRefreshLayout.setRefreshing(false);
                if(isAdded()) {
                    dailyCall(city);
                }
            }
        }, 4000);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == DAILY_LOADER_ID) {
            Calendar c = Calendar.getInstance();
            long startTime = c.getTimeInMillis() / 1000;
            c.add(Calendar.DATE, sharedSettings.getInt(WEATHER_FORECAST_DAYS, 5));
            long endTime = c.getTimeInMillis() / 1000;

            return new CursorLoader(getContext(),
                    DailyContract.DailyEntry.buildDailyWeather(cityId, startTime, endTime),
                    null,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dailyAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dailyAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor editor = sharedSettings.edit();
        switch (id){
            case R.id.action_3_days:
                editor.putInt(WEATHER_FORECAST_DAYS, 3);
                editor.apply();
                getLoaderManager().restartLoader(DAILY_LOADER_ID, null, this);
                notifyWeather(3);
                return true;
            case R.id.action_5_days:
                editor.putInt(WEATHER_FORECAST_DAYS, 5);
                editor.apply();
                getLoaderManager().restartLoader(DAILY_LOADER_ID, null, this);
                notifyWeather(5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

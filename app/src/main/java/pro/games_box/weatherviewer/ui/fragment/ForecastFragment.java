package pro.games_box.weatherviewer.ui.fragment;

import android.content.ContentValues;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.api.ErrorUtils;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.db.ForecastContract;
import pro.games_box.weatherviewer.model.response.ForecastResponse;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import pro.games_box.weatherviewer.ui.adapter.ForecastAdapter;
import pro.games_box.weatherviewer.api.ApiError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TESLA on 16.04.2017.
 */

public class ForecastFragment extends BaseFragment implements Callback, SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final int FORECAST_LOADER_ID = 11;
    public final static String CITY = "CITY";
    public final static String CITY_ID = "CITY_ID";
    private ForecastAdapter forecastAdapter;
    private Call<ForecastResponse> forecastResponseCall;
    private String city;
    private String cityId;
    private DataMapper dataMapper = new DataMapper();

    @BindView(R.id.city) TextView cityTv;
    @BindView(R.id.forecast_list) RecyclerView forecastRecycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    public static ForecastFragment newInstance(int cityId, String cityName) {
        final ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putInt(CITY_ID, cityId);
        args.putString(CITY, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);
        city = getArguments().getString(CITY);
        cityId = String.format(Locale.US, "%d", getArguments().getInt(CITY_ID));
        cityTv.setText(city);
        Toolbar toolbar = ((MainActivity) getActivity()).toolbar;

        getLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
        forecastAdapter = new ForecastAdapter(getActivity(), null, ForecastFragment.this);

        forecastRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        forecastRecycler.setLayoutManager(llm);

        forecastRecycler.setAdapter(forecastAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    private void forecastCall(String city) {
        forecastResponseCall = Api.getApiService().getForecast(city, "38", "metric", "ru", getString(R.string.APIKEY));
        forecastResponseCall.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            if (call.equals(forecastResponseCall)) {
                ForecastResponse forecastResponse = ((Response<ForecastResponse>) response).body();
                for (int i = 0; i < forecastResponse.getForecast().size(); i++) {
                    ContentValues forecastValue = dataMapper.fromForecastItem(forecastResponse.getForecast().get(i), cityId);

                    if(getActivity() != null)
                        getActivity().getContentResolver()
                            .insert(ForecastContract.ForecastEntry.CONTENT_URI, forecastValue);
                }
                notifyWeather();
            }
        } else {
            ApiError error = ErrorUtils.parseError(response);
            if (error != null)
                showToast(error.getMessage());
        }
    }

    public void notifyWeather() {
        Calendar c = Calendar.getInstance();
        long time = c.getTimeInMillis();
        if(getContext() != null)
            getContext().getContentResolver().notifyChange(ForecastContract.ForecastEntry.buildWeatherCityWithStartDate(cityId, time), null, false);
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
                    forecastCall(city);
                }
            }
        }, 4000);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == FORECAST_LOADER_ID) {
            Calendar c = Calendar.getInstance();
            long time = c.getTimeInMillis();

            return new CursorLoader(getContext(),
                    ForecastContract.ForecastEntry.buildWeatherCityWithStartDate(cityId, time),
                    null,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        forecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        forecastAdapter.swapCursor(null);
    }
}

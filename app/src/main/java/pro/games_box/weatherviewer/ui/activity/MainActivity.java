package pro.games_box.weatherviewer.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.ui.adapter.CityAdapter;
import pro.games_box.weatherviewer.utils.ApiError;
import pro.games_box.weatherviewer.api.ErrorUtils;
import pro.games_box.weatherviewer.db.WeatherContract;
import pro.games_box.weatherviewer.model.response.Forecast;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements Callback  {
    private final static String APIKEY = "da2e10fa4e2557831b28f385c2f0f926";
    private Call<Forecast> mForecastResponseCall;
    private Call<WeatherResponce> mWeatherResponseCall;

    private CityAdapter mCityAdapter;

    @BindView(R.id.relative_ll) RelativeLayout relative_ll;
    @BindView(R.id.city_list) RecyclerView city_recycler;

    // Projection and column indices values
    private static final String[] NOTIFY_CITY_PROJECTION = new String[]{
            WeatherContract.CityEntry.COLUMN_CITY_SETTING,
            WeatherContract.CityEntry.COLUMN_CITY_NAME,
            WeatherContract.CityEntry.COLUMN_COORD_LAT,
            WeatherContract.CityEntry.COLUMN_COORD_LONG
    };

    private static final int INDEX_CITY_SETTING = 0;
    private static final int INDEX_CITY_NAME = 1;
    private static final int INDEX_COORD_LAT = 2;
    private static final int INDEX_COORD_LONG = 3;

//    public List<RecyclerAdapterItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Cursor cursor = this.getContentResolver()
                .query(WeatherContract.CityEntry.buildCityWithLastWeather(), null, null, null, null);
        mCityAdapter = new CityAdapter(this, cursor);

        showToast(String.format("%d", cursor.getCount()));
        city_recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        city_recycler.setLayoutManager(llm);

        city_recycler.setAdapter(mCityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void forecastCall(String city) {
        mForecastResponseCall = Api.getApiService().getForecast(city, "38", "metric", "ru", APIKEY);
        mForecastResponseCall.enqueue(this);
    }

    public void weatherCall(final WeatherResponce weather) {
        mWeatherResponseCall = Api.getApiService().getWeather(weather.getBdCityName(), "metric", "ru", APIKEY);
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
                    getContentResolver()
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
//        weatherCall("Mosco");
        CommonUtils.createInputDialog(this, InputType.TYPE_CLASS_TEXT,
                new CommonUtils.InputDialogListener() {
                    @Override
                    public void onInput(AlertDialog dialog, String text) {
                        dialog.setOnDismissListener(null);
                        // Insert the new weather information into the database
                        ContentValues cityValue = new ContentValues();
                        cityValue.put(WeatherContract.CityEntry.COLUMN_CITY_NAME, text);

                        // Update data in database
                        // Insert new data to database
                        dialog.getContext().getContentResolver()
                                .insert(WeatherContract.CityEntry.CONTENT_URI, cityValue);
//                        // Delete old data
//                        dialog.getContext().getContentResolver().delete(
//                                WeatherContract.CityEntry.CONTENT_URI,
//                                WeatherContract.CityEntry.COLUMN_CITY_NAME + " == ?",
//                                new String[]{text});
                        notifyWeather();
                    }
                });

    }

    public void notifyWeather() {
        Cursor cursor = this.getContentResolver()
                .query(WeatherContract.CityEntry.buildCityWithLastWeather(), null, null, null, null);
        mCityAdapter.swapCursor(cursor);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            if (call.equals(mForecastResponseCall)) {


            } else {
                ApiError error = ErrorUtils.parseError(response);
                String errorStr = "";
                if (error != null) errorStr = error.getDescription();

            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        showToast("onFail");
    }
}

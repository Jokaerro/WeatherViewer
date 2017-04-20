package pro.games_box.weatherviewer.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.db.CityContract;
import pro.games_box.weatherviewer.db.DailyContract;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.db.ForecastContract;
import pro.games_box.weatherviewer.db.WeatherContract;
import pro.games_box.weatherviewer.model.response.DailyForecastResponse;
import pro.games_box.weatherviewer.model.response.ForecastResponse;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by TESLA on 18.04.2017.
 */

public class WeatherSync extends IntentService {
    public static final String TABLE = "tableName";
    public static final String CITY_ID = "cityId";
    public static final String DATA_PARAM = "dataParam";

    private Context context;
    private DataMapper dataMapper = new DataMapper();

    public WeatherSync() {
        super("WeatherSync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null ){
            return;
        }
        Bundle data =  intent.getBundleExtra(DATA_PARAM);
        if(data == null){
            return;
        }
        context = getApplicationContext();
        onSync(data);
    }

    public boolean onSync(Bundle bundle) {
        String tableName = (String) bundle.getSerializable(TABLE);
        String cityId = (String) bundle.getSerializable(CITY_ID);
        if (CommonUtils.isEmptyOrNull(tableName)) {
            return false;
        }
        return onSync(tableName, cityId);
    }

    public boolean onSync(String tableName, String cityId) {
        try {
            switch (tableName) {
                case WeatherContract.WeatherEntry.TABLE_NAME:
                    onSyncWeather();
                    context.getContentResolver().notifyChange(CityContract.CityEntry.buildCityWithLastWeather(), null, false);
                    break;
                case ForecastContract.ForecastEntry.TABLE_NAME:
                    onSyncForecast(cityId);
                    Calendar c = Calendar.getInstance();
                    long time = c.getTimeInMillis();
                    context.getContentResolver().notifyChange(ForecastContract
                            .ForecastEntry
                            .buildWeatherCityWithStartDate(cityId, time), null, false);
                    break;
                case DailyContract.DailyEntry.TABLE_NAME:
                    Calendar cd = Calendar.getInstance();
                    long startTime = cd.getTimeInMillis() / 1000;
                    cd.add(Calendar.DATE, 7);
                    long endTime = cd.getTimeInMillis() / 1000;
                    context.getContentResolver().notifyChange(DailyContract
                            .DailyEntry
                            .buildDailyWeather (cityId, startTime, endTime), null, false);
                    onSyncDaily(cityId);
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static final String[] CITY_PROJECTION = new String[]{
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

    private boolean onSyncWeather() throws IOException {
        Cursor cursor;
        cursor = context.getContentResolver().query(CityContract.CityEntry.buildCityWithLastWeather(),
                CITY_PROJECTION,
                null,
                null,
                null);
        while(cursor.moveToNext()) {
            Call<WeatherResponce> call = Api.getApiService().getWeather(dataMapper.fromCursorGetCity(cursor), "metric", "ru", context.getString(R.string.APIKEY));
            Response<WeatherResponce> response = call.execute();
            WeatherResponce weatherResponse = ((Response<WeatherResponce>) response).body();
            int city = dataMapper.fromCursorGetCityId(cursor);
            if(response.isSuccessful()) {
                ContentValues weatherValue = dataMapper.fromWeatherResponse(weatherResponse, city);
                context.getContentResolver()
                        .insert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValue);
            }
        }
        cursor.close();
        return false;
    }

    private boolean onSyncForecast(String cityId) throws IOException {
        Cursor cursor;
        cursor = context.getContentResolver().query(CityContract.CityEntry.buildCityId(),
                null,
                CityContract.CityEntry._ID + "=?",
                new String[]{cityId},
                null);
        if(!cursor.moveToFirst())
            return false;
        Call<ForecastResponse> call = Api.getApiService().getForecast(dataMapper.fromCursorGetCity(cursor), "38", "metric", "ru", context.getString(R.string.APIKEY));
        Response<ForecastResponse> response = call.execute();
        ForecastResponse forecastResponse = ((Response<ForecastResponse>) response).body();

        if(response.isSuccessful()) {
            for (int i = 0; i < forecastResponse.getForecast().size(); i++) {
                ContentValues forecastValue = dataMapper.fromForecastItem(forecastResponse.getForecast().get(i), cityId);

                context.getContentResolver()
                            .insert(ForecastContract.ForecastEntry.CONTENT_URI, forecastValue);
            }
        }

        return false;
    }
    private boolean onSyncDaily(String cityId) throws IOException {
        Cursor cursor;
        cursor = context.getContentResolver().query(CityContract.CityEntry.buildCityId(),
                null,
                CityContract.CityEntry._ID + "=?",
                new String[]{cityId},
                null);
        if(!cursor.moveToFirst())
            return false;
        Call<DailyForecastResponse> call = Api.getApiService().getDaily(dataMapper.fromCursorGetCity(cursor), "7", "metric", "ru", context.getString(R.string.APIKEY));
        Response<DailyForecastResponse> response = call.execute();
        DailyForecastResponse dailyResponse = ((Response<DailyForecastResponse>) response).body();

        if(response.isSuccessful()) {
            for (int i = 0; i < dailyResponse.getDaily().size(); i++) {
                ContentValues dailyValue = dataMapper.fromDaily(dailyResponse.getDaily().get(i), cityId);

                context.getContentResolver()
                            .insert(DailyContract.DailyEntry.CONTENT_URI, dailyValue);
            }
        }
        return false;
    }
}

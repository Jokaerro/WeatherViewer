package pro.games_box.weatherviewer.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.db.CityContract;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.db.ForecastContract;
import pro.games_box.weatherviewer.db.WeatherContract;
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
    public static final String DATA_PARAM = "data_param";

    private Context mContext;
    private DataMapper mDataMapper = new DataMapper();

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
        mContext = getApplicationContext();
        onSync(data);
    }

    public boolean onSync(Bundle bundle) {
        String tableName = (String) bundle.getSerializable(TABLE);
        int cityId = (int) bundle.getSerializable(CITY_ID);
        if (CommonUtils.isEmptyOrNull(tableName)) {
            return false;
        }
        return onSync(tableName, cityId);
    }

    public boolean onSync(String tableName, int cityId) {
        try {
            switch (tableName) {
                case WeatherContract.WeatherEntry.TABLE_NAME:
                    onSyncWeather();
                    mContext.getContentResolver().notifyChange(CityContract.CityEntry.buildCityWithLastWeather(), null, false);
                    break;
                case ForecastContract.ForecastEntry.TABLE_NAME:
                    onSyncForecast(cityId);
                    Calendar c = Calendar.getInstance();
                    long time = c.getTimeInMillis();
                    mContext.getContentResolver().notifyChange(ForecastContract
                            .ForecastEntry
                            .buildWeatherCityWithStartDate(String.format(Locale.US, "%d", cityId), time), null, false);
                    break;
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
        cursor = mContext.getContentResolver().query(CityContract.CityEntry.buildCityWithLastWeather(),
                CITY_PROJECTION,
                null,
                null,
                null);
        while(cursor.moveToNext()) {
            Call<WeatherResponce> call = Api.getApiService().getWeather(mDataMapper.fromCursorGetCity(cursor), "metric", "ru", mContext.getString(R.string.APIKEY));
            Response<WeatherResponce> response = call.execute();
            WeatherResponce weatherResponse = ((Response<WeatherResponce>) response).body();
            int city = mDataMapper.fromCursorGetCityId(cursor);
            ContentValues weatherValue = mDataMapper.fromWeatherResponse(weatherResponse, city);
            mContext.getContentResolver()
                    .insert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValue);
        }
        cursor.close();
        return false;
    }

    private boolean onSyncForecast(int cityId) throws IOException {
        return false;
    }
}

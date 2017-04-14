package pro.games_box.weatherviewer.db;

/**
 * Created by Tesla on 12.04.2017.
 */

import android.database.Cursor;

import java.util.ArrayList;

import pro.games_box.weatherviewer.model.Main;
import pro.games_box.weatherviewer.model.Weather;
import pro.games_box.weatherviewer.model.response.WeatherResponce;

public class DataMapper {

    public WeatherResponce fromCursorJoinCityAndWeather(Cursor cursor){
        WeatherResponce weather = new WeatherResponce();
        Weather currentWeather = new Weather().setDescription(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)));
        weather.setBdCityName(cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_CITY_NAME)));
        weather.setWeather(new ArrayList<Weather>().add(currentWeather));
        weather.setDatetime(cursor.getLong((cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE))));
        weather.setMainConditions(new Main().setHumidity(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY))));
        weather.wind.setDeg(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DEGREES)));
        weather.wind.setSpeed(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)));
        weather.temperature.setMaxTemp(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP)));
        weather.temperature.setMinTemp(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP)));
        weather.currentCondition.setWeatherId(cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID)));
        return weather;
    }
}

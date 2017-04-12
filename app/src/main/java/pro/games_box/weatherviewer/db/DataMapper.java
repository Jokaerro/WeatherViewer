package pro.games_box.weatherviewer.db;

/**
 * Created by Tesla on 12.04.2017.
 */

import android.database.Cursor;
import pro.games_box.weatherviewer.model.response.Weather;
import pro.games_box.weatherviewer.model.response.Forecast;

public class DataMapper {

    public Weather fromCursorJoinCityAndWeather(Cursor cursor){
        Weather weather = new Weather();
        weather.setCityName(cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_CITY_NAME)));
        weather.currentCondition.setDescr(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)));
        weather.setDateTime(cursor.getString((cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE))));
        weather.currentCondition.setHumidity(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
        weather.wind.setDeg(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DEGREES)));
        weather.wind.setSpeed(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)));
        weather.temperature.setMaxTemp(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP)));
        weather.temperature.setMinTemp(cursor.getFloat(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP)));
        weather.currentCondition.setWeatherId(cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID)));
        return weather;
    }
}

package pro.games_box.weatherviewer.db;

/**
 * Created by Tesla on 12.04.2017.
 */

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pro.games_box.weatherviewer.model.Clouds;
import pro.games_box.weatherviewer.model.Coord;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.model.Main;
import pro.games_box.weatherviewer.model.Rain;
import pro.games_box.weatherviewer.model.Snow;
import pro.games_box.weatherviewer.model.Weather;
import pro.games_box.weatherviewer.model.Wind;
import pro.games_box.weatherviewer.model.response.WeatherResponce;

public class DataMapper {

    public WeatherResponce fromCursorJoinCityAndWeather(Cursor cursor){
        WeatherResponce weather = new WeatherResponce();

        weather.setBdCityId(cursor.getInt(cursor.getColumnIndex(CityContract.CityEntry._ID)));
        weather.setBdCityName(cursor.getString(cursor.getColumnIndex(CityContract.CityEntry.COLUMN_CITY_NAME)));

        Weather currentWeather = new Weather();
        List<Weather> weatherList = new ArrayList<>();
        currentWeather.setDescription(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)));
        currentWeather.setId(cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID)));
        weatherList.add(currentWeather);

        Clouds clouds = new Clouds();
        Coord coord = new Coord();

        Main mainConditions = new Main();
        mainConditions.setHumidity(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY)));
        mainConditions.setPressure(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE)));
        mainConditions.setTempMax(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP)));
        mainConditions.setTempMin(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP)));

        Rain rain = new Rain();
        Snow snow = new Snow();

        Wind wind = new Wind();
        wind.setDegree(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID)));
        wind.setSpeed(cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)));

        weather.setBdCityName(cursor.getString(cursor.getColumnIndex(CityContract.CityEntry.COLUMN_CITY_NAME)));
        weather.setWeather(weatherList);
        weather.setDatetime(cursor.getLong((cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE))));
        weather.setMainConditions(mainConditions);
        weather.setWind(wind);
        return weather;
    }

    public ForecastItem fromCursorForecast(Cursor cursor){
        ForecastItem forecast = new ForecastItem();
        forecast.setDt_txt(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_DATETIME)));

        Weather currentWeather = new Weather();
        List<Weather> weatherList = new ArrayList<>();
        currentWeather.setDescription(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_SHORT_DESC)));
        currentWeather.setId(cursor.getInt(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID)));
        currentWeather.setIcon(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_ICON)));
        weatherList.add(currentWeather);

        Rain rain = new Rain();
        rain.setLast3hVolume(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_RAIN)));

        Snow snow = new Snow();
        snow.setLast3hVolume(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_SNOW)));

        Wind wind = new Wind();
        wind.setDegree(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID)));
        wind.setSpeed(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED)));

        Main mainConditions = new Main();
        mainConditions.setHumidity(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_HUMIDITY)));
        mainConditions.setPressure(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_PRESSURE)));
        mainConditions.setTempMax(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MAX_TEMP)));
        mainConditions.setTempMin(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MIN_TEMP)));

        return forecast;
    }
}

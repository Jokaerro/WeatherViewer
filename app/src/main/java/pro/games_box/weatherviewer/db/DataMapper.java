package pro.games_box.weatherviewer.db;

/**
 * Created by Tesla on 12.04.2017.
 */

import android.content.ContentValues;
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
        forecast.setDt_txt(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_DATE)));

        Weather currentWeather = new Weather();
        List<Weather> weatherList = new ArrayList<>();
        currentWeather.setDescription(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_SHORT_DESC)));
        currentWeather.setId(cursor.getInt(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID)));
        currentWeather.setIcon(cursor.getString(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_ICON)));
        weatherList.add(currentWeather);
        forecast.setWeather(weatherList);

        Rain rain = new Rain();
        rain.setLast3hVolume(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_RAIN)));
        forecast.setRainInfo(rain);

        Snow snow = new Snow();
        snow.setLast3hVolume(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_SNOW)));
        forecast.setSnowInfo(snow);

        Wind wind = new Wind();
        wind.setDegree(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID)));
        wind.setSpeed(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED)));
        forecast.setWind(wind);

        Main mainConditions = new Main();
        mainConditions.setHumidity(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_HUMIDITY)));
        mainConditions.setPressure(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_PRESSURE)));
        mainConditions.setTempMax(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MAX_TEMP)));
        mainConditions.setTempMin(cursor.getDouble(cursor.getColumnIndex(ForecastContract.ForecastEntry.COLUMN_MIN_TEMP)));
        forecast.setMainConditions(mainConditions);

        return forecast;
    }

    public ContentValues fromForecastItem(ForecastItem forecast, String cityId){
        ContentValues forecastValue = new ContentValues();
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_LOC_KEY, cityId);
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_DATETIME, forecast.getDatetime());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_DATE, forecast.getDt_txt());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_SHORT_DESC, forecast.getWeather().get(0).getDescription());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_WEATHER_ID, forecast.getWeather().get(0).getId());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_MIN_TEMP, forecast.getMainConditions().getTempMin());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_MAX_TEMP, forecast.getMainConditions().getTempMax());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_HUMIDITY, forecast.getMainConditions().getHumidity());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_PRESSURE, forecast.getMainConditions().getPressure());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED, forecast.getWind().getSpeed());
        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_WIND_DEGREES, forecast.getWind().getDegree());

        Rain currentRain = forecast.getRainInfo();
        if(currentRain!=null)
            forecastValue.put(ForecastContract.ForecastEntry.COLUMN_RAIN, forecast.getRainInfo().getLast3hVolume());
        else
            forecastValue.put(ForecastContract.ForecastEntry.COLUMN_RAIN, " ");

        Snow currentSnow = forecast.getSnowInfo();
        if(currentSnow!=null)
            forecastValue.put(ForecastContract.ForecastEntry.COLUMN_SNOW, forecast.getSnowInfo().getLast3hVolume());
        else
            forecastValue.put(ForecastContract.ForecastEntry.COLUMN_SNOW, " ");

        forecastValue.put(ForecastContract.ForecastEntry.COLUMN_ICON, forecast.getWeather().get(0).getIcon());

        return forecastValue;
    }

    public ContentValues fromWeatherResponse(WeatherResponce weatherResponse, int cityId){
        ContentValues weatherValue = new ContentValues();
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, cityId);
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_DATE, weatherResponse.getDatetime());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, weatherResponse.getWeather().get(0).getDescription());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherResponse.getWeather().get(0).getId());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, weatherResponse.getMainConditions().getTempMin());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, weatherResponse.getMainConditions().getTempMax());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, weatherResponse.getMainConditions().getHumidity());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, weatherResponse.getMainConditions().getPressure());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, weatherResponse.getWind().getSpeed());
        weatherValue.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, weatherResponse.getWind().getDegree());
        return weatherValue;
    }
}

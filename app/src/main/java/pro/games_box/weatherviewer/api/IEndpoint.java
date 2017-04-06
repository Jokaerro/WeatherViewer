package pro.games_box.weatherviewer.api;

import pro.games_box.weatherviewer.model.responce.ForecastResponce;
import pro.games_box.weatherviewer.model.responce.WeatherResponce;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TESLA on 06.04.2017.
 */

public interface IEndpoint {
    final static String TOKEN = "da2e10fa4e2557831b28f385c2f0f926";

    @GET("weather?q={id}&units=metric&lang=ru&APPID=" + TOKEN)
    Call<WeatherResponce> getWeather(@Path("id") String city);

    @GET("forecast?q={id}&units=metric&lang=ru")
    Call<ForecastResponce> getForecast(@Path("id") String city);
}

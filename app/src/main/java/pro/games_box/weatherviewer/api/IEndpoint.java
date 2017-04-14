package pro.games_box.weatherviewer.api;

import pro.games_box.weatherviewer.model.response.Forecast;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by TESLA on 06.04.2017.
 */

public interface IEndpoint {
    @GET("weather")
    Call<WeatherResponce> getWeather(@Query("q") String city,
                                     @Query("units") String units,
                                     @Query("lang") String lang,
                                     @Query("appid") String apikey);

    @GET("forecast")
    Call<Forecast> getForecast(@Query("q") String city,
                               @Query("cnt") String limit,
                               @Query("units") String unit,
                               @Query("lang") String lang,
                               @Query("appid") String apikey);
}

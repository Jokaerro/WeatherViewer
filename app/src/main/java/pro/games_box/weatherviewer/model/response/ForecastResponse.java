package pro.games_box.weatherviewer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pro.games_box.weatherviewer.model.ForecastItem;

/**
 * Created by TESLA on 06.04.2017.
 */

public class ForecastResponse {
    private String bdCityName;
    private int bdCityId;

    public void setBdCityName(String bdCityName) {
        this.bdCityName = bdCityName;
    }

    public void setBdCityId(int bdCityId) {
        this.bdCityId = bdCityId;
    }

    public List<ForecastItem> getForecast() {
        return forecast;
    }

    @SerializedName("list")
    @Expose
    private List<ForecastItem> forecast;
}

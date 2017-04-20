package pro.games_box.weatherviewer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pro.games_box.weatherviewer.model.Daily;
import pro.games_box.weatherviewer.model.ForecastItem;

/**
 * Created by Tesla on 20.04.2017.
 */

public class DailyForecastResponse {
    private String bdCityName;
    private int bdCityId;

    public String getBdCityName() {
        return bdCityName;
    }

    public void setBdCityName(String bdCityName) {
        this.bdCityName = bdCityName;
    }

    public int getBdCityId() {
        return bdCityId;
    }

    public void setBdCityId(int bdCityId) {
        this.bdCityId = bdCityId;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("list")

    @Expose
    private List<Daily> daily;

    @SerializedName("name")
    @Expose
    private String name;
}

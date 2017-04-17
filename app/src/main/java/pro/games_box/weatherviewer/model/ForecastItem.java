package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tesla on 17.04.2017.
 */

public class ForecastItem {

    @SerializedName("dt")
    @Expose
    private long  datetime;

    @SerializedName("main")
    @Expose
    private Main mainConditions;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("rain")
    @Expose
    private Rain rainInfo;

    @SerializedName("snow")
    @Expose
    private Snow snowInfo;

    @SerializedName("dt_txt")
    @Expose
    private String dt_txt;


    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public Main getMainConditions() {
        return mainConditions;
    }

    public void setMainConditions(Main mainConditions) {
        this.mainConditions = mainConditions;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRainInfo() {
        return rainInfo;
    }

    public void setRainInfo(Rain rainInfo) {
        this.rainInfo = rainInfo;
    }

    public Snow getSnowInfo() {
        return snowInfo;
    }

    public void setSnowInfo(Snow snowInfo) {
        this.snowInfo = snowInfo;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}

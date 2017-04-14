package pro.games_box.weatherviewer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pro.games_box.weatherviewer.model.Clouds;
import pro.games_box.weatherviewer.model.Coord;
import pro.games_box.weatherviewer.model.Main;
import pro.games_box.weatherviewer.model.Rain;
import pro.games_box.weatherviewer.model.Snow;
import pro.games_box.weatherviewer.model.Weather;
import pro.games_box.weatherviewer.model.Wind;

/**
 * Created by TESLA on 06.04.2017.
 */

public class WeatherResponce {
    private String bdCityName;
    private String bdCityId;

    public void setBdCityName(String bdCityName) {
        this.bdCityName = bdCityName;
    }

    public void setBdCityId(String bdCityId) {
        this.bdCityId = bdCityId;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setMainConditions(Main mainConditions) {
        this.mainConditions = mainConditions;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public void setSys(float sys) {
        this.sys = sys;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRainInfo(Rain rainInfo) {
        this.rainInfo = rainInfo;
    }

    public void setSnowInfo(Snow snowInfo) {
        this.snowInfo = snowInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBdCityName() {

        return bdCityName;
    }

    public String getBdCityId() {
        return bdCityId;
    }

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMainConditions() {
        return mainConditions;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getDatetime() {
        return datetime;
    }

    public float getSys() {
        return sys;
    }

    public long getId() {
        return id;
    }

    public Rain getRainInfo() {
        return rainInfo;
    }

    public Snow getSnowInfo() {
        return snowInfo;
    }

    public String getName() {
        return name;
    }

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("main")
    @Expose
    private Main mainConditions;

    @SerializedName("visibility")
    @Expose
    private Integer visibility;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt")
    @Expose
    private long  datetime;

    @SerializedName("sys")
    @Expose
    private float sys;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("rain")
    @Expose
    private Rain rainInfo;
    @SerializedName("snow")
    @Expose
    private Snow snowInfo;

    @SerializedName("name")
    @Expose
    private String name;
}

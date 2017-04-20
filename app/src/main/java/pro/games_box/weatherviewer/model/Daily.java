package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tesla on 20.04.2017.
 */

public class Daily {
    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Double getWeendSpeed() {
        return weendSpeed;
    }

    public void setWeendSpeed(Double weendSpeed) {
        this.weendSpeed = weendSpeed;
    }

    public Double getWeendDeg() {
        return weenddeg;
    }

    public void setWeendDeg(Double weenddeg) {
        this.weenddeg = weenddeg;
    }

    public Double getClouds() {
        return clouds;
    }

    public void setClouds(Double clouds) {
        this.clouds = clouds;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }

    @SerializedName("dt")
    @Expose
    private long  datetime;

    @SerializedName("temp")
    @Expose
    private Temperature  temp;

    @SerializedName("pressure")
    @Expose
    private Double  pressure;

    @SerializedName("humidity")
    @Expose
    private Double  humidity;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("speed")
    @Expose
    private Double  weendSpeed;

    @SerializedName("deg")
    @Expose
    private Double  weenddeg;

    @SerializedName("clouds")
    @Expose
    private Double  clouds;

    @SerializedName("rain")
    @Expose
    private Double  rain;

    @SerializedName("snow")
    @Expose
    private Double  snow;
}

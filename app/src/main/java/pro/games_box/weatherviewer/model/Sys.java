package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TESLA on 14.04.2017.
 */

public class Sys {
    public void setCountry(String country) {
        this.country = country;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Long sunrise;
    @SerializedName("sunset")
    @Expose
    private Long sunset;

    /**
     * @return Country code (GB, JP etc.)
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return Sunrise time, unix, UTC
     */
    public Long getSunrise() {
        return sunrise;
    }

    /**
     * @return Sunset time, unix, UTC
     */
    public Long getSunset() {
        return sunset;
    }
}

package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tesla on 14.04.2017.
 */

public class Coord {
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("lat")
    @Expose
    private Double lat;

    /**
     * @return City geo location, longitude
     */
    public Double getLon() {
        return lon;
    }

    /**
     * @return City geo location, latitude
     */
    public Double getLat() {
        return lat;
    }
}

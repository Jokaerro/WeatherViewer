package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tesla on 14.04.2017.
 */

public class Wind {
    @SerializedName("speed")
    @Expose
    private Double speed;
    @SerializedName("deg")
    @Expose
    private Double degree;

    /**
     * @return Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * @return Wind direction, degrees (meteorological)
     */
    public Double getDegree() {
        return degree;
    }
}

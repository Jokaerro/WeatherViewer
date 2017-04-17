package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tesla on 14.04.2017.
 */

public class Snow {
    @SerializedName("3h")
    @Expose
    private Double last3hVolume;

    /**
     * @return Snow volume for the last 3 hours
     */
    public Double getLast3hVolume() {
        return last3hVolume;
    }

    public void setLast3hVolume(Double last3hVolume) {
        this.last3hVolume = last3hVolume;
    }
}

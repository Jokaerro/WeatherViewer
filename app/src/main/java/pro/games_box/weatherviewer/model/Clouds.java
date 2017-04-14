package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tesla on 14.04.2017.
 */

public class Clouds {
    @SerializedName("all")
    @Expose
    private Double cloudiness;

    /**
     * @return Cloudiness in %
     */
    public Double getCloudiness() {
        return cloudiness;
    }
}

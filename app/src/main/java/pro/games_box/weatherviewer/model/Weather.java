package pro.games_box.weatherviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tesla on 14.04.2017.
 */

public class Weather {
//"weather":[{"id":803,"main":"Clouds","description":"пасмурно","icon":"04d"}],
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    /**
     * @return Weather condition id
     */
    public long getId() {
        return id;
    }
    /**
     * @return Group of weather parameters (Rain, Snow, Extreme etc.)
     */
    public String getMain() {
        return main;
    }
    /**
     * @return Weather condition within the group
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return Weather icon id
     */
    public String getIcon() {
        return icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

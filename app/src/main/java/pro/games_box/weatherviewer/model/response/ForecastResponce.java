package pro.games_box.weatherviewer.model.response;

/**
 * Created by TESLA on 06.04.2017.
 */

public class ForecastResponce {
    private String name;
    private String country;
    private String temp;
    private String description;

    public ForecastResponce(String name, String country, String temp, String description) {
        this.name = name;
        this.country = country;
        this.temp = temp;
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}

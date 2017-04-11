package pro.games_box.weatherviewer.utils;

import com.google.gson.annotations.Expose;

/**
 * Created by TESLA on 06.04.2017.
 */

public class ApiError {
    @Expose
    private String message;

    @Expose
    private String description;

    public ApiError() {
    }

    public String getMessage() {
        return message;
    }

    public String getDescription(){
        return description;
    }
}

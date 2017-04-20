package pro.games_box.weatherviewer.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import pro.games_box.weatherviewer.R;

/**
 * Created by Tesla on 07.04.2017.
 */

public class CommonUtils {
    public static boolean isEmptyOrNull(String str) {
        return (str == null) || str.equals("");
    }
    public static interface InputDialogListener {
        void onInput(AlertDialog dialog, String text);
    }

    //Group 2xx: Thunderstorm
    //Group 3xx: Drizzle
    //Group 5xx: Rain
    //Group 6xx: Snow
    //Group 7xx: Atmosphere
    //Group 800: Clear
    //Group 80x: Clouds
    //Group 90x: Extreme
    //Group 9xx: Additional
    public static Drawable getWeatherIcon(Context context, int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return context.getResources().getDrawable(R.drawable.weather_storm_day);
        } else if (weatherId >= 300 && weatherId <= 321) {
            return context.getResources().getDrawable(R.drawable.weather_drizzle_day);
        } else if (weatherId >= 500 && weatherId <= 531) {
            return context.getResources().getDrawable(R.drawable.weather_showers_day);
        } else if (weatherId >= 600 && weatherId <= 622) {
            return context.getResources().getDrawable(R.drawable.weather_snow_scattered_day);
        } else if (weatherId >= 701 && weatherId <= 711) {
            return context.getResources().getDrawable(R.drawable.weather_mist);
        } else if (weatherId == 721) {
            return context.getResources().getDrawable(R.drawable.weather_haze);
        } else if (weatherId >= 731 && weatherId <= 781) {
            return context.getResources().getDrawable(R.drawable.weather_fog);
        } else if (weatherId == 800) {
            return context.getResources().getDrawable(R.drawable.weather_clear);
        } else if (weatherId == 801) {
            return context.getResources().getDrawable(R.drawable.weather_few_clouds);
        } else if (weatherId >= 802 && weatherId <= 804) {
            return context.getResources().getDrawable(R.drawable.weather_clouds);
        } else if (weatherId >= 900 && weatherId <= 962) {
            return context.getResources().getDrawable(R.drawable.weather_wind);
        }
        return context.getResources().getDrawable(R.drawable.weather_none_available);
    }

    public static String getTempWithSign(Double temp){
        String result = "";
        if (temp > 0)
            result = "+" + String.format(Locale.US, "%.0f", temp);
        else
            result = String.format(Locale.US, "%.0f", temp);
        return result;
    }
}

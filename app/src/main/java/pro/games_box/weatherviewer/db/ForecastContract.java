package pro.games_box.weatherviewer.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by TESLA on 17.04.2017.
 */

public class ForecastContract {
    public static final String CONTENT_AUTHORITY = "pro.games_box.weatherviewer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FORECAST = "forecast";

    public static final class ForecastEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORECAST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORECAST;

        public static final String TABLE_NAME = "forecast";
        public static final String COLUMN_LOC_KEY = "city_id";

        public static final String COLUMN_DATETIME = "date";
        public static final String COLUMN_DATE = "dt";

        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_ICON = "icon";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_PRESSURE = "pressure";

        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_DEGREES = "wind_deg";

        public static final String COLUMN_RAIN = "rain";
        public static final String COLUMN_SNOW = "snow";



        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildWeatherCity(String city_setting) {
            return CONTENT_URI.buildUpon().appendPath(city_setting).build();
        }

        public static Uri buildWeatherCityWithStartDate(String cityId, long startDate) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_LOC_KEY, cityId)
                    .appendQueryParameter(COLUMN_DATETIME, String.format("%d", startDate)).build();
        }

        public static String getCityIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_LOC_KEY);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getQueryParameter(COLUMN_DATETIME));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}

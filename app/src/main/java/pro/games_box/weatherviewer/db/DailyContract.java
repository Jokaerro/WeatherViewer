package pro.games_box.weatherviewer.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by Tesla on 20.04.2017.
 */

public class DailyContract {
    public static final String CONTENT_AUTHORITY = "pro.games_box.weatherviewer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DAILY = "daily";

    public static final class DailyEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAILY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAILY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAILY;

        public static final String TABLE_NAME = "daily";
        public static final String COLUMN_LOC_KEY = "city_id";

        public static final String COLUMN_DATE = "dt";

        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_ICON = "icon";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_MOR_TEMP = "mor";
        public static final String COLUMN_DAY_TEMP = "day";
        public static final String COLUMN_EVE_TEMP = "eve";
        public static final String COLUMN_NIGHT_TEMP = "night";

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

        public static Uri buildDailyWeather(String cityId, long startDate, long endDate) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_LOC_KEY, cityId)
                    .appendQueryParameter("startDate", String.valueOf(startDate))
                    .appendQueryParameter("endDate", String.valueOf(endDate)).build();
        }

        public static String getCityIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_LOC_KEY);
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter("startDate");
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

        public static long getEndDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter("endDate");
            if (!TextUtils.isEmpty(dateString))
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}

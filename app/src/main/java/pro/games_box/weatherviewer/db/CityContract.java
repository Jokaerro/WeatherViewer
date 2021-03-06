package pro.games_box.weatherviewer.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by admin on 17.04.2017.
 */

public class CityContract {
    public static final String CONTENT_AUTHORITY = "pro.games_box.weatherviewer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CITY = "city";
    public static final String PATH_CITY_WITH_LAST_WEATHER = PATH_CITY + "/last_weather";

    public static final class CityEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CITY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITY;

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "city";
        public static final String COLUMN_CITY_SETTING = "city_setting";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

        public static Uri buildCityWithLastWeather() {
            return BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CITY_WITH_LAST_WEATHER).build();
        }

        public static Uri buildCityId() {
            return BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CITY).build();
        }

    }
}

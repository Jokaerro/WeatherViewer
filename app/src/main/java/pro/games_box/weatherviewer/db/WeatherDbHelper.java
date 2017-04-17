package pro.games_box.weatherviewer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import pro.games_box.weatherviewer.db.CityContract.CityEntry;
import pro.games_box.weatherviewer.db.WeatherContract.WeatherEntry;
import pro.games_box.weatherviewer.db.ForecastContract.ForecastEntry;

/**
 * Created by TESLA on 07.04.2017.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 10;
    static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + CityEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CityEntry.COLUMN_CITY_SETTING + " TEXT, " +
                CityEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                CityEntry.COLUMN_COORD_LAT + " REAL, " +
                CityEntry.COLUMN_COORD_LONG + " REAL " +
                ");";

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +

                WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                CityEntry.TABLE_NAME + " (" + CityEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
                WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FORECAST_TABLE = "CREATE TABLE " + ForecastEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                ForecastEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                ForecastEntry.COLUMN_DATETIME + " INTEGER NOT NULL, " +
                ForecastEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ForecastEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                ForecastEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +

                ForecastEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                ForecastEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                ForecastEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                ForecastEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                ForecastEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                ForecastEntry.COLUMN_WIND_DEGREES + " REAL NOT NULL, " +

                ForecastEntry.COLUMN_ICON + " TEXT NOT NULL, " +
                ForecastEntry.COLUMN_RAIN + " REAL, " +
                ForecastEntry.COLUMN_SNOW + " REAL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + ForecastEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                CityEntry.TABLE_NAME + " (" + CityEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + ForecastEntry.COLUMN_DATE + ", " +
                ForecastEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FORECAST_TABLE);
        sqLiteDatabase.execSQL("insert into " + CityEntry.TABLE_NAME +" values (1,'','Москва','','');");
        sqLiteDatabase.execSQL("insert into " + CityEntry.TABLE_NAME +" values (2,'','Санкт-Петербург','','');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CityEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ForecastEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

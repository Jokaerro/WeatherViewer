package pro.games_box.weatherviewer.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pro.games_box.weatherviewer.model.Daily;

/**
 * Created by TESLA on 07.04.2017.
 */

public class WeatherProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private WeatherDbHelper openHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_CITY = 101;
    static final int WEATHER_WITH_CITY_AND_DATE = 102;
    static final int CITY = 300;
    static final int CITY_WITH_LAST_WEATHER = 301;
    static final int FORECAST = 400;
    static final int DAILY = 500;

    private static final SQLiteQueryBuilder weatherByLocationSettingQueryBuilder;

    static{
        weatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        /** weather INNER JOIN location ON weather.location_id = location._id */
        weatherByLocationSettingQueryBuilder.setTables(
                ForecastContract.ForecastEntry.TABLE_NAME + " INNER JOIN " +
                        CityContract.CityEntry.TABLE_NAME +
                        " ON " + ForecastContract.ForecastEntry.TABLE_NAME +
                        "." + ForecastContract.ForecastEntry.COLUMN_LOC_KEY +
                        " = " + CityContract.CityEntry.TABLE_NAME +
                        "." + CityContract.CityEntry._ID);
    }

    private static final SQLiteQueryBuilder dailyWeatherByLocationQueryBuilder;
    static{
        dailyWeatherByLocationQueryBuilder = new SQLiteQueryBuilder();

        /** weather INNER JOIN location ON weather.location_id = location._id */
        dailyWeatherByLocationQueryBuilder.setTables(
                DailyContract.DailyEntry.TABLE_NAME + " INNER JOIN " +
                        CityContract.CityEntry.TABLE_NAME +
                        " ON " + DailyContract.DailyEntry.TABLE_NAME +
                        "." + DailyContract.DailyEntry.COLUMN_LOC_KEY +
                        " = " + CityContract.CityEntry.TABLE_NAME +
                        "." + CityContract.CityEntry._ID);
    }

    private static final SQLiteQueryBuilder citiesWithWeatherSettingQueryBuilder;
    static{
        citiesWithWeatherSettingQueryBuilder = new SQLiteQueryBuilder();
    /* SELECT * from city left JOIN weather ON  weather._LOC_KEY = city._id GROUP BY city_name order by _DATE DESC*/
        /** weather INNER JOIN location ON weather.location_id = location._id */
        citiesWithWeatherSettingQueryBuilder.setTables(
                CityContract.CityEntry.TABLE_NAME + " LEFT JOIN " +
                        WeatherContract.WeatherEntry.TABLE_NAME +
                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                        "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + CityContract.CityEntry.TABLE_NAME +
                        "." + CityContract.CityEntry._ID);
    }

    private static final String citySelection =
        CityContract.CityEntry.TABLE_NAME +
                        "." + CityContract.CityEntry._ID + " = ? ";

    private static final String cityWithStartDateSelection =
            CityContract.CityEntry.TABLE_NAME+
                    "." + CityContract.CityEntry._ID + " = ? AND " +
                    ForecastContract.ForecastEntry.COLUMN_DATE + " >= ? ";

    private static final String cityWithStartDateEndDateSelection =
            CityContract.CityEntry.TABLE_NAME+
                    "." + CityContract.CityEntry._ID + " = ? AND " +
                    DailyContract.DailyEntry.COLUMN_DATE + " >= ? AND " +
                    DailyContract.DailyEntry.COLUMN_DATE + " <= ?";

    private static final String locationSettingAndDaySelection =
            CityContract.CityEntry.TABLE_NAME +
                    "." + CityContract.CityEntry.COLUMN_CITY_SETTING + " = ? AND " +
                    WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";

    private static final String cityWithLastWeatherSelection =
            CityContract.CityEntry.TABLE_NAME + "." + CityContract.CityEntry.COLUMN_CITY_NAME
                    + "";

    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = ForecastContract.ForecastEntry.getCityIdFromUri(uri);
        long startDate = ForecastContract.ForecastEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;
        selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
        selection = cityWithStartDateSelection;

        return weatherByLocationSettingQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDailyWeather(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = DailyContract.DailyEntry.getCityIdFromUri(uri);
        long startDate = DailyContract.DailyEntry.getStartDateFromUri(uri);
        long endDate = DailyContract.DailyEntry.getEndDateFromUri(uri);

        String[] selectionArgs;
        String selection;
        selectionArgs = new String[]{locationSetting, Long.toString(startDate), Long.toString(endDate)};
        selection = cityWithStartDateEndDateSelection;

        return dailyWeatherByLocationQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCitiesWithLastWeather(Uri uri, String[] projection,  String selection,
                                            String[] selectionArgs,  String sortOrder){
        return citiesWithWeatherSettingQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                CityContract.CityEntry.COLUMN_CITY_NAME,
                null,
                sortOrder);
    }

    private Cursor getWeatherByCitySettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.WeatherEntry.getCitySettingFromUri(uri);
        long date = WeatherContract.WeatherEntry.getDateFromUri(uri);

        return weatherByLocationSettingQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                locationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = WeatherContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, WeatherContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_CITY);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*/#", WEATHER_WITH_CITY_AND_DATE);

        matcher.addURI(authority, CityContract.PATH_CITY, CITY);
        matcher.addURI(authority, CityContract.PATH_CITY_WITH_LAST_WEATHER, CITY_WITH_LAST_WEATHER);

        matcher.addURI(authority, ForecastContract.PATH_FORECAST, FORECAST);

        matcher.addURI(authority, DailyContract.PATH_DAILY, DAILY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            // "weather/*/*"
            case WEATHER_WITH_CITY_AND_DATE:
            {
                retCursor = getWeatherByCitySettingAndDate(uri, projection, sortOrder);
                break;
            }
            case CITY_WITH_LAST_WEATHER:
            {
                retCursor = getCitiesWithLastWeather(uri, projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "forecast/*"
            case FORECAST: {
                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                break;
            }
            // "daily/*"
            case DAILY: {
                retCursor = getDailyWeather(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case WEATHER: {
                retCursor = openHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "city"
            case CITY: {
                retCursor = openHelper.getReadableDatabase().query(
                        CityContract.CityEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case WEATHER_WITH_CITY_AND_DATE:
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_CITY:
            case WEATHER:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case CITY:
                return CityContract.CityEntry.CONTENT_TYPE;
            case CITY_WITH_LAST_WEATHER:
                return CityContract.CityEntry.CONTENT_ITEM_TYPE;
            case FORECAST:
                return ForecastContract.ForecastEntry.CONTENT_ITEM_TYPE;
            case DAILY:
                return DailyContract.DailyEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WEATHER: {
//                normalizeDate(contentValues);
                long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = WeatherContract.WeatherEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CITY: {
                long _id = db.insert(CityContract.CityEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = CityContract.CityEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FORECAST: {
                long _id = db.insert(ForecastContract.ForecastEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = ForecastContract.ForecastEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DAILY: {
                long _id = db.insert(DailyContract.DailyEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = DailyContract.DailyEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // This makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case WEATHER:
                rowsDeleted = db.delete(
                        WeatherContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CITY:
                rowsDeleted = db.delete(
                        CityContract.CityEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FORECAST:
                rowsDeleted = db.delete(
                        ForecastContract.ForecastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DAILY:
                rowsDeleted = db.delete(
                        DailyContract.DailyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        // Normalize the date value
        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WEATHER:
//                normalizeDate(contentValues);
                rowsUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case CITY:
                rowsUpdated = db.update(CityContract.CityEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case FORECAST:
                rowsUpdated = db.update(ForecastContract.ForecastEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case DAILY:
                rowsUpdated = db.update(DailyContract.DailyEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    // If we do not set the transaction to be successful,
                    // the records will not be committed when we call endTransaction()
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

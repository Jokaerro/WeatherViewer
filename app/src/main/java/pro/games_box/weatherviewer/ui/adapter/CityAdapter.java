package pro.games_box.weatherviewer.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.WeatherApplication;
import pro.games_box.weatherviewer.db.CityContract;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.db.WeatherContract;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import pro.games_box.weatherviewer.ui.adapter.holder.CityHolder;
import pro.games_box.weatherviewer.ui.fragment.ForecastFragment;
import pro.games_box.weatherviewer.ui.fragment.WeatherFragment;

/**
 * Created by Tesla on 07.04.2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private final Context context;
    private Cursor cursor;
    private boolean dataValid;
    private int rowIdColumn;
    private ChangeObservable dataSetObserver;
    private DataMapper dataMapper = new DataMapper();
    private WeatherApplication application;
    private WeatherFragment fragment;

    public CityAdapter(Context context, Cursor data, WeatherFragment fragment) {
        this.context = context;
        cursor = data;
        dataValid = data != null;
        rowIdColumn = dataValid ? cursor.getColumnIndex(BaseColumns._ID) : -1;
        dataSetObserver = new ChangeObservable();
        if (cursor != null) {
            cursor.registerDataSetObserver(dataSetObserver);
        }
        application = WeatherApplication.getInstance();
        this.fragment = fragment;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.city_card, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, cursor);
    }

    public void onBindViewHolder(CityHolder viewHolder, Cursor cursor) {
        final CityHolder holder = viewHolder;
        final WeatherResponce weather = dataMapper.fromCursorJoinCityAndWeather(cursor);
        viewHolder.fill(weather);
        viewHolder.weatherDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().getContentResolver().delete(CityContract.CityEntry.CONTENT_URI,
                        CityContract.CityEntry.COLUMN_CITY_NAME + " == ?",
                        new String[]{weather.getBdCityName()});

                Cursor cursor = view.getContext().getContentResolver()
                        .query(CityContract.CityEntry.buildCityWithLastWeather(),
                                new String[]{"city."+ CityContract.CityEntry._ID,
                                        CityContract.CityEntry.COLUMN_CITY_NAME,
                                        WeatherContract.WeatherEntry.COLUMN_DATE,
                                        WeatherContract.WeatherEntry.COLUMN_DEGREES,
                                        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                                        WeatherContract.WeatherEntry.COLUMN_LOC_KEY,
                                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                                        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                                        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                                        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                                        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED},
                                null, null, null);
                swapCursor(cursor);
            }
        });
        viewHolder.weatherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    fragment.weatherCall(weather);
                }
            }
        });
        viewHolder.weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.root_layout, ForecastFragment.newInstance(weather.getBdCityId(), weather.getBdCityName()), "forecast")
                            .addToBackStack("weather")
                            .commit();
                }
            }
        });
    }

    protected Cursor getCursor() {
        return cursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            dataValid = true;
            notifyDataSetChanged();
            notifyItemInserted(getItemCount());
        } else {
            rowIdColumn = -1;
            dataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    protected Cursor getItemCursor(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public class ChangeObservable extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }
}

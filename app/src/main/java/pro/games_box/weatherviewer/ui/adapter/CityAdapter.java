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
    private final Context mContext;
    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private ChangeObservable mDataSetObserver;
    private DataMapper mDataMapper = new DataMapper();
    private WeatherApplication mApplication;
    private WeatherFragment fragment;

    public CityAdapter(Context context, Cursor data, WeatherFragment fragment) {
        // Конструктор адаптера, если прилетает не иницилизированный список инициализруем его
        mContext = context;
        mCursor = data;
        mDataValid = data != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        mDataSetObserver = new ChangeObservable();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        mApplication = WeatherApplication.getInstance();
        this.fragment = fragment;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.city_card, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    public void onBindViewHolder(CityHolder viewHolder, Cursor cursor) {
        final CityHolder holder = viewHolder;
        final WeatherResponce weather = mDataMapper.fromCursorJoinCityAndWeather(cursor);
        viewHolder.fill(weather);
        viewHolder.weather_delete.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.weather_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity) {
                    fragment.weatherCall(weather);
//                    ((MainActivity) mContext).weatherCall(weather);
                }
            }
        });
        viewHolder.weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.root_layout, ForecastFragment.newInstance(weather.getBdCityId(), weather.getBdCityName()), "forecast")
                            .addToBackStack("weather")
                            .commit();
                }
            }
        });
    }

    protected Cursor getCursor() {
        return mCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataValid = true;
            notifyDataSetChanged();
            notifyItemInserted(getItemCount());
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    protected Cursor getItemCursor(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        // Не забываем указать количество айтемов в списке
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
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

package pro.games_box.weatherviewer.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.model.response.Weather;

/**
 * Created by Tesla on 07.04.2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder>{
//    private static List<WeatherResponce> mData;
    private final Context mContext;
    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private ChangeObservable mDataSetObserver;
    private DataMapper mDataMapper = new DataMapper();

    public CityAdapter(Context context, Cursor data) {
        // Конструктор адаптера, если прилетает не иницилизированный список инициализруем его
        mContext = context;
        mCursor = data;
        mDataValid = data != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;

    }

    public class CityHolder extends RecyclerView.ViewHolder {
        // Холдер который отображает данные одного айтема из списка
        public final ImageView weather_icon;
        public final LinearLayout weather_delete;
        public final TextView weather_temp;
        public final TextView weather_city;
        public final TextView weather_description;
        public final TextView weather_datetime;
        public final TextView weather_humidity;
        public final TextView weather_wind;
        public final TextView weather_pressure;

        public CityHolder(View itemView) {
            super(itemView);
//            Context context = itemView.getContext();
            weather_icon = (ImageView) itemView.findViewById(R.id.weather_icon);
            weather_delete = (LinearLayout) itemView.findViewById(R.id.weather_delete);
            weather_temp = (TextView) itemView.findViewById(R.id.weather_temp);
            weather_city = (TextView) itemView.findViewById(R.id.weather_city);
            weather_description = (TextView) itemView.findViewById(R.id.weather_description);
            weather_datetime = (TextView) itemView.findViewById(R.id.weather_datetime);
            weather_humidity = (TextView) itemView.findViewById(R.id.weather_humidity);
            weather_wind = (TextView) itemView.findViewById(R.id.weather_wind);
            weather_pressure = (TextView) itemView.findViewById(R.id.weather_pressure);
        }

        public void fill(final Weather weather) {
            weather_temp.setText(String.format(Locale.US, "%f..%f", weather.temperature.getMinTemp(),
                    weather.temperature.getMaxTemp()));
            weather_city.setText(weather.getCityName());
            weather_description.setText(weather.currentCondition.getDescr());
            weather_datetime.setText(weather.getDateTime());
            weather_humidity.setText(String.format(Locale.US, "%f", weather.currentCondition.getHumidity()));
            weather_wind.setText(String.format(Locale.US, "%f", weather.wind.getSpeed()));
            weather_pressure.setText(String.format(Locale.US, "%f", weather.currentCondition.getPressure()));


//            weather_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });
//            weather_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });
        }
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
        viewHolder.fill(mDataMapper.fromCursorJoinCityAndWeather(cursor));
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

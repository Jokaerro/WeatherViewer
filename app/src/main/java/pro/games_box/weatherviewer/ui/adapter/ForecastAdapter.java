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
import pro.games_box.weatherviewer.db.DataMapper;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.model.response.ForecastResponse;
import pro.games_box.weatherviewer.ui.adapter.holder.ForecastHolder;
import pro.games_box.weatherviewer.ui.fragment.ForecastFragment;

/**
 * Created by Tesla on 17.04.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastHolder>{
    private final Context mContext;
    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private ForecastAdapter.ChangeObservable mDataSetObserver;
    private DataMapper mDataMapper = new DataMapper();
    private WeatherApplication mApplication;
    private ForecastFragment fragment;

    public ForecastAdapter(Context context, Cursor data, ForecastFragment fragment) {
        // Конструктор адаптера, если прилетает не иницилизированный список инициализруем его
        mContext = context;
        mCursor = data;
        mDataValid = data != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        mDataSetObserver = new ForecastAdapter.ChangeObservable();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        mApplication = WeatherApplication.getInstance();
        this.fragment = fragment;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_card, parent, false);
        return new ForecastHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }
    public void onBindViewHolder(ForecastHolder viewHolder, Cursor cursor) {
        final ForecastHolder holder = viewHolder;
        final ForecastItem forecast = mDataMapper.fromCursorForecast(cursor);
        viewHolder.fill(forecast);
    }

    @Override
    public int getItemCount() {
        // Не забываем указать количество айтемов в списке
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
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

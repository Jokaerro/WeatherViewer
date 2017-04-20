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
import pro.games_box.weatherviewer.model.Daily;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.ui.adapter.holder.DailyHolder;
import pro.games_box.weatherviewer.ui.adapter.holder.ForecastHolder;
import pro.games_box.weatherviewer.ui.fragment.DailyFragment;
import pro.games_box.weatherviewer.ui.fragment.ForecastFragment;

/**
 * Created by Tesla on 17.04.2017.
 */

public class DailyAdapter extends RecyclerView.Adapter<DailyHolder>{
    private final Context context;
    private Cursor cursor;
    private boolean dataValid;
    private int rowIdColumn;
    private DailyAdapter.ChangeObservable dataSetObserver;
    private DataMapper dataMapper = new DataMapper();
    private WeatherApplication application;
    private DailyFragment fragment;

    public DailyAdapter(Context context, Cursor data, DailyFragment fragment) {
        this.context = context;
        cursor = data;
        dataValid = data != null;
        rowIdColumn = dataValid ? cursor.getColumnIndex(BaseColumns._ID) : -1;
        dataSetObserver = new DailyAdapter.ChangeObservable();
        if (cursor != null) {
            cursor.registerDataSetObserver(dataSetObserver);
        }
        application = WeatherApplication.getInstance();
        this.fragment = fragment;
    }

    @Override
    public DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.daily_card, parent, false);
        return new DailyHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyHolder holder, int position) {
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, cursor);
    }
    public void onBindViewHolder(DailyHolder viewHolder, Cursor cursor) {
        final DailyHolder holder = viewHolder;
        final Daily daily = dataMapper.fromCursorDaily(cursor);
        viewHolder.fill(daily);
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
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

package pro.games_box.weatherviewer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.response.WeatherResponce;

/**
 * Created by Tesla on 07.04.2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder>{
    private static List<WeatherResponce> mData;
    private final Context mContext;

    public WeatherAdapter(Context context, List<WeatherResponce> data) {
        // Конструктор адаптера, если прилетает не иницилизированный список инициализруем его
        mContext = context;
        if (data != null)
            mData = new ArrayList<>(data);
        else mData = new ArrayList<>();
    }

    public class WeatherHolder extends RecyclerView.ViewHolder {
        // Холдер который отображает данные одного айтема из списка
        public final ImageView weather_icon;
        public final TextView weather_temp;
        public final TextView weather_city;
        public final TextView weather_description;
        public final TextView weather_datetime;
        public final TextView weather_humidity;
        public final TextView weather_wind;
        public final TextView weather_pressure;

        public WeatherHolder(View itemView) {
            super(itemView);
//            Context context = itemView.getContext();
            weather_icon = (ImageView) itemView.findViewById(R.id.weather_icon);
            weather_temp = (TextView) itemView.findViewById(R.id.weather_temp);
            weather_city = (TextView) itemView.findViewById(R.id.weather_city);
            weather_description = (TextView) itemView.findViewById(R.id.weather_description);
            weather_datetime = (TextView) itemView.findViewById(R.id.weather_datetime);
            weather_humidity = (TextView) itemView.findViewById(R.id.weather_humidity);
            weather_wind = (TextView) itemView.findViewById(R.id.weather_wind);
            weather_pressure = (TextView) itemView.findViewById(R.id.weather_pressure);
        }
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.city_card, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
//        holder.title.setText(mData.get(position).getTitle());
        holder.weather_temp.setText(Float.toString(mData.get(position).temperature.getTemp()));
//        holder.weather_city.setText(mData.get(position).)
        holder.weather_description.setText(mData.get(position).currentCondition.getDescr());
        holder.weather_humidity.setText(Float.toString(mData.get(position).currentCondition.getHumidity()));
        holder.weather_wind.setText(Float.toString(mData.get(position).wind.getSpeed()));
        holder.weather_pressure.setText(Float.toString(mData.get(position).currentCondition.getPressure()));
//        holder.weather_datetime.setText(mData.get(position).);

//        final String title =  holder.title.getText().toString();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(mContext, "Clicked:" + title, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // Не забываем указать количество айтемов в списке
        return mData.size();
    }
}

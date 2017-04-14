package pro.games_box.weatherviewer.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.response.WeatherResponce;

/**
 * Created by Tesla on 14.04.2017.
 */

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

    public void fill(final WeatherResponce weather) {
        weather_temp.setText(String.format(Locale.US, "%.1f-%.1f", weather.getMainConditions().getTempMin(),
                weather.getMainConditions().getTempMax()));
        weather_city.setText(weather.getBdCityName());
        weather_description.setText(weather.getWeather().get(0).getDescription());
        String dateString = DateFormat.format("dd/MM/yy HH:mm", new Date(weather.getDatetime())).toString();
        weather_datetime.setText(dateString);
        weather_humidity.setText(String.format(Locale.US, "%.0f", weather.getMainConditions().getHumidity()));
        weather_wind.setText(String.format(Locale.US, "%.1f", weather.getWind().getSpeed()));
        weather_pressure.setText(String.format(Locale.US, "%.0f", weather.getMainConditions().getPressure()));


//            weather_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });

    }
}

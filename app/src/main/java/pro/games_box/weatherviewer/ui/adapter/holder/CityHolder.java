package pro.games_box.weatherviewer.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.utils.CommonUtils;

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
    public Context mContext;

    public CityHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
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
        String signMin, signMax = "";
        if (weather.getMainConditions().getTempMin() > 0)
            signMin = "+" + String.format(Locale.US, "%.0f", weather.getMainConditions().getTempMin());
        else
            signMin = String.format(Locale.US, "%.0f", weather.getMainConditions().getTempMin());
        if (weather.getMainConditions().getTempMax() > 0)
            signMax = "+"+ String.format(Locale.US, "%.0f", weather.getMainConditions().getTempMax());
        else
            signMax = String.format(Locale.US, "%.0f", weather.getMainConditions().getTempMax());
        weather_temp.setText(signMin + " .. " + signMax);

        weather_temp.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        weather_temp.setSingleLine(true);
        weather_temp.setSelected(true);
        weather_temp.requestFocus();

        weather_city.setText(weather.getBdCityName());
        weather_description.setText(weather.getWeather().get(0).getDescription());
//        String dateString = DateFormat.format("dd/MM/yy HH:mm", new Date(weather.getDatetime())).toString();

        Date date = new Date(weather.getDatetime()*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3")); // give a timezone reference
        String formattedDate = sdf.format(date);

        weather_datetime.setText(formattedDate);

        weather_humidity.setText(String.format(Locale.US,  mContext.getString(R.string.weather_humidity),
                weather.getMainConditions().getHumidity()) + "%");
        weather_wind.setText(String.format(Locale.US,  mContext.getString(R.string.weather_wind),
                weather.getWind().getSpeed()));
        weather_pressure.setText(String.format(Locale.US,  mContext.getString(R.string.weather_pressure),
                weather.getMainConditions().getPressure()));
        weather_pressure.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        weather_pressure.setSingleLine(true);
        weather_pressure.setSelected(true);
        weather_pressure.requestFocus();

        weather_icon.setBackground(CommonUtils.getWeatherIcon(mContext, weather.getWeather().get(0).getId()));
//            weather_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });

    }
}

package pro.games_box.weatherviewer.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import pro.games_box.weatherviewer.utils.CommonUtils;

/**
 * Created by Tesla on 14.04.2017.
 */

public class CityHolder extends RecyclerView.ViewHolder {
    // Холдер который отображает данные одного айтема из списка
    @BindView(R.id.weather_icon) public ImageView weatherIcon;
    @BindView(R.id.weather_delete) public LinearLayout weatherDelete;
    @BindView(R.id.weather_temp) TextView weatherTemp;
    @BindView(R.id.weather_city) TextView weatherCity;
    @BindView(R.id.weather_description) TextView weatherDescription;
    @BindView(R.id.weather_datetime) TextView weatherDatetime;
    @BindView(R.id.weather_humidity) TextView weatherHumidity;
    @BindView(R.id.weather_wind) TextView weatherWind;
    @BindView(R.id.weather_pressure) TextView weatherPressure;
    @BindView(R.id.weather) public LinearLayout weather;
    public Context context;

    public CityHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
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
        weatherTemp.setText(signMin + " .. " + signMax);

        weatherTemp.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        weatherTemp.setSingleLine(true);
        weatherTemp.setSelected(true);
        weatherTemp.requestFocus();

        weatherCity.setText(weather.getBdCityName());
        weatherDescription.setText(weather.getWeather().get(0).getDescription());

        Date date = new Date(weather.getDatetime()*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3")); // give a timezone reference
        String formattedDate = sdf.format(date);

        weatherDatetime.setText(formattedDate);

        weatherHumidity.setText(String.format(Locale.US,  context.getString(R.string.weather_humidity),
                weather.getMainConditions().getHumidity()) + "%");
        weatherWind.setText(String.format(Locale.US,  context.getString(R.string.weather_wind),
                weather.getWind().getSpeed()));
        weatherPressure.setText(String.format(Locale.US,  context.getString(R.string.weather_pressure),
                weather.getMainConditions().getPressure()));
        weatherPressure.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        weatherPressure.setSingleLine(true);
        weatherPressure.setSelected(true);
        weatherPressure.requestFocus();

        weatherIcon.setBackground(CommonUtils.getWeatherIcon(context, weather.getWeather().get(0).getId()));
    }
}

package pro.games_box.weatherviewer.ui.adapter.holder;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.Daily;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.utils.CommonUtils;

import static pro.games_box.weatherviewer.R.id.weather;

/**
 * Created by Tesla on 17.04.2017.
 */

public class DailyHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.daily_icon) ImageView dailytIcon;
    @BindView(R.id.daily_morn_temperature) TextView dailyMornTemperature;
    @BindView(R.id.daily_day_temperature) TextView dailyDayTemperature;
    @BindView(R.id.daily_eve_temperature) TextView dailyEveTemperature;
    @BindView(R.id.daily_night_temperature) TextView dailyNightTemperature;
    @BindView(R.id.daily_temperature) TextView dailyTemperature;
    @BindView(R.id.daily_description) TextView dailyDescription;
    @BindView(R.id.daily_datetime) TextView dailyDatetime;
    @BindView(R.id.daily_humidity) TextView dailyHumidity;
    @BindView(R.id.daily_wind_speed) TextView dailyWindSpeed;
    @BindView(R.id.daily_pressure) TextView dailyPressure;
    @BindView(R.id.daily_rain) TextView dailyRain;
    @BindView(R.id.daily_snow) TextView dailySnow;

    public Context context;
    public DailyHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void fill(final Daily daily) {
        dailyTemperature.setText(CommonUtils.getTempWithSign(daily.getTemp().getMin()) + " .. " + CommonUtils.getTempWithSign(daily.getTemp().getMax()));
        dailyMornTemperature.setText(CommonUtils.getTempWithSign(daily.getTemp().getMorn()));
        dailyDayTemperature.setText(CommonUtils.getTempWithSign(daily.getTemp().getDay()));
        dailyEveTemperature.setText(CommonUtils.getTempWithSign(daily.getTemp().getEve()));
        dailyNightTemperature.setText(CommonUtils.getTempWithSign(daily.getTemp().getNight()));

        dailyDescription.setText(daily.getWeather().get(0).getDescription());

        Date date = new Date(daily.getDatetime()*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3")); // give a timezone reference
        String formattedDate = sdf.format(date);

        dailyDatetime.setText(formattedDate);

        String strHumidity = String.format(Locale.US,  context.getString(R.string.weather_humidity) + "%",
                daily.getHumidity());
        dailyHumidity.setText(strHumidity);
        dailyWindSpeed.setText(String.format(Locale.US,  context.getString(R.string.weather_wind),
                daily.getWeendSpeed()));
        dailyPressure.setText(String.format(Locale.US,  context.getString(R.string.weather_pressure),
                daily.getPressure()));

        if(!CommonUtils.isEmptyOrNull(daily.getRain().toString()))
            dailyRain.setText(String.format(context.getString(R.string.weather_rain), daily.getRain()));
        else
            dailyRain.setVisibility(View.GONE);

        if(!CommonUtils.isEmptyOrNull(daily.getSnow().toString()))
            dailySnow.setText(String.format(context.getString(R.string.weather_snow), daily.getSnow()));
        else
            dailySnow.setVisibility(View.GONE);

        String internetUrl = "http://openweathermap.org/img/w/" + daily.getWeather().get(0).getIcon() + ".png";
        Picasso.with(context).load(internetUrl).into(dailytIcon);
    }
}

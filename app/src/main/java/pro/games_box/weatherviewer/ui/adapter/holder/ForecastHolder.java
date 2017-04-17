package pro.games_box.weatherviewer.ui.adapter.holder;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.model.response.ForecastResponse;
import pro.games_box.weatherviewer.utils.CommonUtils;

/**
 * Created by Tesla on 17.04.2017.
 */

public class ForecastHolder extends RecyclerView.ViewHolder{
    public final ImageView forecast_icon;
    public final TextView forecast_temperature;
    public final TextView forecast_description;
    public final TextView forecast_datetime;
    public final TextView forecast_humidity;
    public final TextView forecast_windspeed;
    public final TextView forecast_pressure;
    public final TextView forecast_rain;
    public final TextView forecast_snow;
    public final TextView forecast_winddegree;
    public Context mContext;
    public ForecastHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        forecast_icon = (ImageView) itemView.findViewById(R.id.forecast_icon);
        forecast_temperature = (TextView) itemView.findViewById(R.id.forecast_temperature);
        forecast_description = (TextView) itemView.findViewById(R.id.forecast_description);
        forecast_datetime = (TextView) itemView.findViewById(R.id.forecast_datetime);
        forecast_humidity = (TextView) itemView.findViewById(R.id.forecast_humidity);
        forecast_windspeed = (TextView) itemView.findViewById(R.id.forecast_windspeed);
        forecast_pressure = (TextView) itemView.findViewById(R.id.forecast_pressure);
        forecast_rain = (TextView) itemView.findViewById(R.id.forecast_rain);
        forecast_snow = (TextView) itemView.findViewById(R.id.forecast_snow);
        forecast_winddegree = (TextView) itemView.findViewById(R.id.forecast_winddegree);
    }

    public void fill(final ForecastItem forecastItem) {
        String signMin, signMax = "";
        if (forecastItem.getMainConditions().getTempMin() > 0)
            signMin = "+" + String.format(Locale.US, "%.0f", forecastItem.getMainConditions().getTempMin());
        else
            signMin = String.format(Locale.US, "%.0f", forecastItem.getMainConditions().getTempMin());
        if (forecastItem.getMainConditions().getTempMax() > 0)
            signMax = "+"+ String.format(Locale.US, "%.0f", forecastItem.getMainConditions().getTempMax());
        else
            signMax = String.format(Locale.US, "%.0f", forecastItem.getMainConditions().getTempMax());
        forecast_temperature.setText(signMin + " .. " + signMax);
        forecast_description.setText(forecastItem.getWeather().get(0).getDescription());
        forecast_datetime.setText(forecastItem.getDt_txt().toString());
        forecast_humidity.setText(String.format(Locale.US,  mContext.getString(R.string.weather_humidity),
                forecastItem.getMainConditions().getHumidity()) + "%");
        forecast_windspeed.setText(String.format(Locale.US,  mContext.getString(R.string.weather_wind),
                forecastItem.getWind().getSpeed()));
        forecast_pressure.setText(String.format(Locale.US,  mContext.getString(R.string.weather_pressure),
                forecastItem.getMainConditions().getPressure()));

        if(!CommonUtils.isEmptyOrNull(forecastItem.getRainInfo().getLast3hVolume().toString()))
            forecast_rain.setText(String.format("Дождь %f мм", forecastItem.getRainInfo().getLast3hVolume()));
        else
            forecast_rain.setVisibility(View.GONE);

        if(!CommonUtils.isEmptyOrNull(forecastItem.getSnowInfo().getLast3hVolume().toString()))
            forecast_rain.setText(String.format("Снег %f мм", forecastItem.getRainInfo().getLast3hVolume()));
        else
            forecast_rain.setVisibility(View.GONE);

        String internetUrl = "http://openweathermap.org/img/w/" + forecastItem.getWeather().get(0).getIcon() + ".png";
        Picasso.with(mContext).load(internetUrl).into(forecast_icon);
    }
}

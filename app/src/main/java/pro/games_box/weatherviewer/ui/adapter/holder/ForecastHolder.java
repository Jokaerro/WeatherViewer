package pro.games_box.weatherviewer.ui.adapter.holder;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.model.ForecastItem;
import pro.games_box.weatherviewer.utils.CommonUtils;

/**
 * Created by Tesla on 17.04.2017.
 */

public class ForecastHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.forecast_icon) ImageView forecastIcon;
    @BindView(R.id.forecast_temperature) TextView forecastTemperature;
    @BindView(R.id.forecast_description) TextView forecastDescription;
    @BindView(R.id.forecast_datetime) TextView forecastDatetime;
    @BindView(R.id.forecast_humidity) TextView forecastHumidity;
    @BindView(R.id.forecast_windspeed) TextView forecastWindspeed;
    @BindView(R.id.forecast_pressure) TextView forecastPressure;
    @BindView(R.id.forecast_rain) TextView forecastRain;
    @BindView(R.id.forecast_snow) TextView forecastSnow;
    @BindView(R.id.forecast_winddegree) TextView forecastWinddegree;
    public Context context;
    public ForecastHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
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
        forecastTemperature.setText(signMin + " .. " + signMax);
        forecastDescription.setText(forecastItem.getWeather().get(0).getDescription());
        forecastDatetime.setText(forecastItem.getDt_txt().toString());
        forecastHumidity.setText(String.format(Locale.US,  context.getString(R.string.weather_humidity),
                forecastItem.getMainConditions().getHumidity()) + "%");
        forecastWindspeed.setText(String.format(Locale.US,  context.getString(R.string.weather_wind),
                forecastItem.getWind().getSpeed()));
        forecastPressure.setText(String.format(Locale.US,  context.getString(R.string.weather_pressure),
                forecastItem.getMainConditions().getPressure()));

        if(!CommonUtils.isEmptyOrNull(forecastItem.getRainInfo().getLast3hVolume().toString()))
            forecastRain.setText(String.format("Дождь %f мм", forecastItem.getRainInfo().getLast3hVolume()));
        else
            forecastRain.setVisibility(View.GONE);

        if(!CommonUtils.isEmptyOrNull(forecastItem.getSnowInfo().getLast3hVolume().toString()))
            forecastRain.setText(String.format("Снег %f мм", forecastItem.getRainInfo().getLast3hVolume()));
        else
            forecastRain.setVisibility(View.GONE);

        String internetUrl = "http://openweathermap.org/img/w/" + forecastItem.getWeather().get(0).getIcon() + ".png";
        Picasso.with(context).load(internetUrl).into(forecastIcon);
    }
}

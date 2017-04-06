package pro.games_box.weatherviewer.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.api.ApiError;
import pro.games_box.weatherviewer.api.ErrorUtils;
import pro.games_box.weatherviewer.model.response.ForecastResponce;
import pro.games_box.weatherviewer.model.response.WeatherResponce;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements Callback {
    private final static String APIKEY = "da2e10fa4e2557831b28f385c2f0f926";
    private Call<ForecastResponce> mForecastResponceCall;
    private Call<WeatherResponce> mWeatherResponceCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void forecastCall(String city) {
        mForecastResponceCall = Api.getApiService().getForecast(city, "38", "metric","ru", APIKEY);
        mForecastResponceCall.enqueue(this);
    }

    private void weatherCall(String city) {
        mWeatherResponceCall = Api.getApiService().getWeather(city, "metric", "ru", APIKEY);
        mWeatherResponceCall.enqueue(this);
    }

    @OnClick(R.id.fab)
    public void weather() {
        forecastCall("Moscow");
    }

    @OnClick(R.id.fab2)
    public void forecast() {
        weatherCall("Moscow");
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            if (call.equals(mForecastResponceCall)) {


            } else if (call.equals(mWeatherResponceCall)) {

            } else {
                ApiError error = ErrorUtils.parseError(response);
                String errorStr = "";
                if (error != null) errorStr = error.getDescription();

            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        showToast("onFail");
    }
}

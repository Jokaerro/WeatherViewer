package pro.games_box.weatherviewer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.api.Api;
import pro.games_box.weatherviewer.model.response.Forecast;
import pro.games_box.weatherviewer.ui.activity.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TESLA on 16.04.2017.
 */

public class ForecastFragment extends BaseFragment implements Callback {
    public final static String CITY = "CITY";
    private Call<Forecast> mForecastResponseCall;
    private String _city;

    @BindView(R.id.city) TextView city_tv;

    private void forecastCall(String city) {
        mForecastResponseCall = Api.getApiService().getForecast(city, "38", "metric", "ru", getString(R.string.APIKEY));
        mForecastResponseCall.enqueue(this);
    }

    public static ForecastFragment newInstance(String city) {
        final ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);
        city_tv.setText(getArguments().getString(CITY));
        Toolbar toolbar = ((MainActivity) getActivity()).mToolbar;

        return rootView;
    }

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}

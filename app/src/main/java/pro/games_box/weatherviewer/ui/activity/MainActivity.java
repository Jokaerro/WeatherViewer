package pro.games_box.weatherviewer.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import pro.games_box.weatherviewer.R;
import pro.games_box.weatherviewer.ui.fragment.WeatherFragment;
public class MainActivity extends BaseActivity {

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, WeatherFragment.newInstance(), "weather")
                    .commit();
        }
    }
}
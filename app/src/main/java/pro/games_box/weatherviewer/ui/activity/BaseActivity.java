package pro.games_box.weatherviewer.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by TESLA on 06.04.2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

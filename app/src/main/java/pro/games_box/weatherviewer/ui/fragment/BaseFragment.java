package pro.games_box.weatherviewer.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import pro.games_box.weatherviewer.ui.activity.BaseActivity;

/**
 * Created by TESLA on 16.04.2017.
 */

public class BaseFragment extends Fragment {

    protected BaseActivity getParentCompat() {
        return (BaseActivity) getActivity();
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

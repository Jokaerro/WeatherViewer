package pro.games_box.weatherviewer.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import pro.games_box.weatherviewer.R;

/**
 * Created by Tesla on 07.04.2017.
 */

public class CommonUtils {
    public static boolean isEmptyOrNull(String str) {
        return (str == null) || str.equals("");
    }
    public static interface InputDialogListener {
        void onInput(AlertDialog dialog, String text);
    }
    public static AlertDialog createInputDialog(Context context,
                                                int inputType,
                                                final InputDialogListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_add_city, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialDialogSheet);
        final AlertDialog dialog = builder.create();
        final EditText et_input = (EditText) view.findViewById(R.id.et_input);
        TextView tv_button = (TextView) view.findViewById(R.id.tv_button);

        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onInput(dialog, et_input.getText().toString());
                dialog.cancel();
            }
        });
        dialog.setView(view);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        et_input.setInputType(inputType);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_input, InputMethodManager.SHOW_FORCED);

        return dialog;
    }
}

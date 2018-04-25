package com.example.frost.testapplication.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import com.example.frost.testapplication.R;
import java.util.Calendar;

public class DataPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String LOG_TAG = "TestApp " + DataPicker.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Дата рождения");
        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year,
                          int month, int day) {
        Button button = getActivity().findViewById(R.id.button_birthday);
        button.setText("Дата рождения: " + day + "." + ++month + "." + year);
    }
}

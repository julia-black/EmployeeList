package com.example.frost.testapplication.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frost.testapplication.R;
import com.example.frost.testapplication.dialogs.DataPicker;
import com.example.frost.testapplication.model.Employee;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class EmployeeFragment extends Fragment {

    private static final String LOG_TAG = "TestApp " + EmployeeFragment.class.getSimpleName();
    @BindView(R.id.button_birthday)
    Button mButtonBirthday;

    @BindView(R.id.button_photo)
    Button mButtonPhoto;

    private static int idx;
    @BindView(R.id.button_ok)
    Button mButtonOk;

    @BindView(R.id.photo)
    ImageView mPhoto;

    @BindView(R.id.surname_text)
    EditText mSurnameText;

    @BindView(R.id.name_text)
    EditText mNameText;

    @BindView(R.id.patronymic_text)
    EditText mPatronymicText;

    private boolean mFlagNew = true;
    private Employee mEmployee = new Employee();
    private Bitmap thumbnailBitmap;

    private final int CAMERA_RESULT = 0;

    public interface Listener {
        void onUpdateEmployee(Employee newEmployee);

        void onAddEmployee(Employee newEmployee);

        Employee getCurrentEmployee();

        boolean getCurrentFlagNew();
    }

    public EmployeeFragment() {

    }

    @SuppressLint("ValidFragment")
    public EmployeeFragment(Employee employee, boolean flagNew) {  //flagNew - новый сотрудник или обновляем старого
        this.mFlagNew = flagNew;
        this.mEmployee = employee;
        setArguments(new Bundle());
        Log.i(LOG_TAG, mEmployee.getFullName());
        thumbnailBitmap = mEmployee.getmPhoto();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        Listener l = (Listener) getActivity();
        mEmployee = l.getCurrentEmployee();
        mFlagNew = l.getCurrentFlagNew();

        View view = inflater.inflate(R.layout.employee_fragment,
                container, false);
        ButterKnife.bind(this, view);

        mButtonBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPicker dateDialog = new DataPicker();
                dateDialog.show(getFragmentManager(), "DatePicker");
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "OK");
                Listener l = (Listener) getActivity();
                if (!mFlagNew) {

                    Employee newEmployee = new Employee(mEmployee.getId(), mSurnameText.getText().toString(),
                            mNameText.getText().toString(), mPatronymicText.getText().toString(),
                            mButtonBirthday.getText().toString(), thumbnailBitmap);
                    l.onUpdateEmployee(newEmployee);
                }
                //если добавляем нового
                else {
                    Employee newEmployee = new Employee(idx, mSurnameText.getText().toString(),
                            mNameText.getText().toString(), mPatronymicText.getText().toString(),
                            mButtonBirthday.getText().toString(), thumbnailBitmap);
                    idx++;
                    l.onAddEmployee(newEmployee);
                }

                ListEmployeeFragment fragment = new ListEmployeeFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        mButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Photo");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });

        //если мы хотим обновить сотрудника
        if (!mFlagNew) {
            mSurnameText.setText(mEmployee.getmSurName());
            mNameText.setText(mEmployee.getmName());
            mPatronymicText.setText(mEmployee.getmPatronymic());
            mButtonBirthday.setText(mEmployee.getStringBirthday());
            mPhoto.setImageBitmap(mEmployee.getmPhoto());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == CAMERA_RESULT) {
            thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            mPhoto.setImageBitmap(thumbnailBitmap);
        }
    }
}

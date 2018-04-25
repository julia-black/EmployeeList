package com.example.frost.testapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.frost.testapplication.R;
import com.example.frost.testapplication.adapters.ViewAdapter;
import com.example.frost.testapplication.model.Employee;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListEmployeeFragment extends Fragment {

    private static final String LOG_TAG = "TestApp " + ListEmployeeFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.button_add)
    Button buttonAdd;

    private Listener mListener;

    public interface Listener {
        void onEmployeeClicked(Employee employee);

        List<Employee> getEmployeeList();

        void deleteEmployee(Employee employee);

       // Employee getCurrentEmployee();
//
       // boolean getCurrentFlagNew();
//
        void setCurrentEmployee(Employee employee);

        void setCurrentFlagNew(boolean flag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (Listener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment,
                container, false);
        ButterKnife.bind(this, view);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEmployeeFragment();
            }
        });
        initRecycleView();
        return view;
    }

    private void toEmployeeFragment() {
        Listener l = (Listener) getActivity();
        l.setCurrentEmployee(new Employee());
        l.setCurrentFlagNew(true);
        EmployeeFragment fragment = new EmployeeFragment(new Employee(), true);
        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new ViewAdapter(this.getActivity(), mListener.getEmployeeList());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Listener l = (Listener) getActivity();
                    l.onEmployeeClicked(mListener.getEmployeeList().get(position));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.i(LOG_TAG,"1");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

    }
}

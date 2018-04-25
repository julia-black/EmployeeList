package com.example.frost.testapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frost.testapplication.R;
import com.example.frost.testapplication.fragments.ListEmployeeFragment;
import com.example.frost.testapplication.model.Employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "TestApp " + ViewAdapter.class.getSimpleName();
    private List<Employee> mEmployeeList;
    private Context mContext;

    public ViewAdapter(Context context, List<Employee> employeeList) {
        mContext = context;
        mEmployeeList = employeeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.mBackLayout.setBackgroundColor(Color.rgb(224, 255, 255));
        } else {
            holder.mBackLayout.setBackgroundColor(Color.rgb(255, 250, 205));
        }
        holder.mNameTextView.setText(mEmployeeList.get(position).getFullName());
//
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.context_menu);
                popup.setOnMenuItemClickListener(holder);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.i(LOG_TAG, "Delete " + position + " ");
                        ListEmployeeFragment.Listener l = (ListEmployeeFragment.Listener) mContext;
                        l.deleteEmployee(mEmployeeList.get(position));
                        return true;
                    }
                });
                return true;
            }
        });
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        holder.mBirthdayTextView.setText(format.format(mEmployeeList.get(position).getmBirthday()));
    }

    @Override
    public int getItemCount() {
        return mEmployeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.name)
        TextView mNameTextView;

        @BindView(R.id.birthday)
        TextView mBirthdayTextView;

        @BindView(R.id.card_layout)
        ConstraintLayout mBackLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    }
}

package com.Pccoe.Teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class DailyDataAdapter extends RecyclerView.Adapter {
    MainModel[] data;
    public DailyDataAdapter(MainModel[] data) {
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_theory_view, parent, false);
        return new ViewHolderOne(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
        viewHolderOne.End.setText(String.valueOf(data[position].getEnd()));
        viewHolderOne.Start.setText(String.valueOf(data[position].getStart()));
        viewHolderOne.Subject.setText(String.valueOf(data[position].getSubject()));
        viewHolderOne.Classroom.setText(String.valueOf(data[position].getClassroom()));
        viewHolderOne.Teacher.setText(String.valueOf(data[position].getTeacher()));
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
    static class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView Classroom, Subject, Teacher, End, Start;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);

            Classroom = itemView.findViewById(R.id.classroom);
            Subject = itemView.findViewById(R.id.subject);
            Teacher = itemView.findViewById(R.id.teacher);
            Start = itemView.findViewById(R.id.start);
            End = itemView.findViewById(R.id.end);
        }
    }
}

package com.Pccoe.Teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NoticeAdapter extends FirebaseRecyclerAdapter<NoticeModel,NoticeAdapter.myViewHolder> {

    public NoticeAdapter(@NonNull FirebaseRecyclerOptions<NoticeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull NoticeModel model) {

        holder.name.setText(model.getName());
        holder.message.setText(model.getMessage());
        holder.time.setText(model.getTime());
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notice_layout,parent,false );
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,message,time;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.MessageName);
            message= itemView.findViewById(R.id.Message);
            time= itemView.findViewById(R.id.NoticeTime);
        }
    }
}

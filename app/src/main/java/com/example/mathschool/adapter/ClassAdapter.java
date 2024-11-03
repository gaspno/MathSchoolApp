package com.example.mathschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathschool.LessonActivity;
import com.example.mathschool.R;
import com.example.mathschool.adapter.model.ProgressClass;

import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.Screen> {


    private List<ProgressClass> list;
    private Context context;
    private String year;



    @NonNull
    @Override
    public ClassAdapter.Screen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_option_view, parent, false);
        return new ClassAdapter.Screen(view);
    }

    public ClassAdapter(List<ProgressClass> list, Context context,String year) {
        this.list = list;
        this.context=context;
        this.year=year;

    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.Screen holder, int position) {
        holder.isWatched.setClickable(false);
        holder.isWatched.setBackgroundColor(Color.BLUE);
        holder.isWatched.setTextColor(Color.WHITE);
        holder.isWatched.setChecked(list.get(position).isWatched());
        holder.nameClass.setText(list.get(position).getClassName());
        holder.nameClass.setTextColor(Color.BLACK);
        String className=list.get(position).getClassName();
        Integer classID=list.get(position).getId();
        String medias=list.get(position).getMedias();
        String subject=list.get(position).getSubject();
        holder.container.setOnClickListener(v -> {
            Intent intent=new Intent(context, LessonActivity.class);
            intent.putExtra("year",year);
            intent.putExtra("subject",subject);
            intent.putExtra("className",className);
            intent.putExtra("classID",classID);
            intent.putExtra("medias",medias);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Screen extends RecyclerView.ViewHolder{

        TextView nameClass;
        CheckBox isWatched;
        LinearLayout container;


        public Screen(@NonNull View itemView) {
            super(itemView);
            itemView.setBackgroundColor(Color.WHITE);
            nameClass=itemView.findViewById(R.id.className);
            isWatched=itemView.findViewById(R.id.isWatched);
            container=itemView.findViewById(R.id.containerClass);
        }
    }
}

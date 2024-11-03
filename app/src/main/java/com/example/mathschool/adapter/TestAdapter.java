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

import com.example.mathschool.R;
import com.example.mathschool.TestActivity;
import com.example.mathschool.adapter.model.ProgressTest;
import com.example.mathschool.model.Question;

import java.util.ArrayList;
import java.util.List;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.Screen> {


    private List<ProgressTest> list;
    private Context context;
    private String year;



    @NonNull
    @Override
    public TestAdapter.Screen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_option_view, parent, false);
        return new TestAdapter.Screen(view);
    }

    public TestAdapter(List<ProgressTest> list, Context context, String year) {
        this.list = list;
        this.context=context;
        this.year =year;

    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.Screen holder, int position) {
        holder.isCompleted.setClickable(false);
        holder.isCompleted.setText("isCompleted");
        holder.isCompleted.setBackgroundColor(Color.BLUE);
        holder.isCompleted.setTextColor(Color.WHITE);
        holder.isCompleted.setChecked(list.get(position).isCompleted());
        holder.nameClass.setText("Teste : "+position);
        holder.nameClass.setTextColor(Color.BLACK);
        Integer testID=list.get(position).getId();
        String subject=list.get(position).getSubject();
        List<Question> questions=list.get(position).getQuestionEntitySet();
        holder.container.setOnClickListener(v -> {
            Intent intent=new Intent(context, TestActivity.class);
            intent.putExtra("year", year);
            intent.putExtra("testID",testID);
            intent.putExtra("subject",subject);
            intent.putExtra("questions",new ArrayList<>(questions));
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
        CheckBox isCompleted;
        LinearLayout container;


        public Screen(@NonNull View itemView) {
            super(itemView);
            itemView.setBackgroundColor(Color.WHITE);
            nameClass=itemView.findViewById(R.id.className);
            isCompleted =itemView.findViewById(R.id.isWatched);
            container=itemView.findViewById(R.id.containerClass);
        }
    }
}

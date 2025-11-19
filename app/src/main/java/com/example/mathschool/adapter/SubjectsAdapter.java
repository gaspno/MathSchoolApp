package com.example.mathschool.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathschool.R;
import com.example.mathschool.adapter.model.ProgressSubject;
import com.example.mathschool.interfaces.OnAdapterItemClicked;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.Screen> {

    private List<ProgressSubject> list;
    private Activity context;
    private String year;
    private OnAdapterItemClicked onAdapterItemClicked;

    public void setOnAdapterItemClicked(OnAdapterItemClicked onAdapterItemClicked) {
        this.onAdapterItemClicked = onAdapterItemClicked;
    }

    public SubjectsAdapter(List<ProgressSubject> list, Activity context, String year) {
        this.list = list;
        this.context=context;
        this.year=year;

    }

    @NonNull
    @Override
    public Screen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_progress_view, parent, false);

        return new Screen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Screen holder, int position) {
        ProgressBar bar1=holder.progressBar1;
        ProgressBar bar2=holder.progressBar2;
        bar1.setMax(list.get(position).getTotalLessons());
        bar1.setProgress(list.get(position).getLessonsConclude());
        bar2.setMax(list.get(position).getTotalTest());
        bar2.setProgress(list.get(position).getTestConclude());
        ImageView imageView=holder.imageView;
        TextView classCount=holder.classCount;
        TextView testCount=holder.testCount;
        TextView subjectText=holder.subjectName;
        FirebaseStorage.getInstance().getReference().child(list.get(position).getUrlImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                CountDownLatch latch = new CountDownLatch(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        try {
                            inputStream = new java.net.URL(uri.toString()).openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            context.runOnUiThread(() -> {
                            imageView.setImageBitmap(bitmap);
                            latch.countDown();
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
        Drawable progressDrawable1 = bar1.getProgressDrawable().mutate();
        progressDrawable1.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        Drawable progressDrawable2 = bar2.getProgressDrawable().mutate();
        progressDrawable2.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        bar1.setMax(list.get(position).getTotalLessons());
        bar2.setMax(list.get(position).getTotalTest());
        bar1.setProgress(list.get(position).getLessonsConclude());
        bar2.setProgress(list.get(position).getTestConclude());
        bar1.setProgressDrawable(progressDrawable1);
        bar2.setProgressDrawable(progressDrawable2);
        classCount.setText(list.get(position).getLessonsConclude()+"/"+list.get(position).getTotalLessons());
        testCount.setText(list.get(position).getTestConclude()+"/"+list.get(position).getTotalTest());
        subjectText.setText(list.get(position).getSubjectName());
        int positionValue=position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onAdapterItemClicked.onItemClicked(positionValue,list.get(positionValue).getSubjectName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Screen extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;
        ProgressBar progressBar2;
        ImageView imageView;
        TextView classCount;
        TextView testCount;
        TextView subjectName;

        public Screen(@NonNull View itemView) {
            super(itemView);
            progressBar1=itemView.findViewById(R.id.progressBarClass);
            progressBar2=itemView.findViewById(R.id.progressBarTest);
            imageView=itemView.findViewById(R.id.imageView);
            classCount=itemView.findViewById(R.id.className);
            testCount=itemView.findViewById(R.id.textTestCompleted);
            subjectName=itemView.findViewById(R.id.textSubjectName);
        }
    }


}

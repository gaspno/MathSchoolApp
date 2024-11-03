package com.example.mathschool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mathschool.fragments.LessonView;

public class LessonActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        new Thread(() -> {
            FragmentManager fm = getSupportFragmentManager();
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            FragmentTransaction ft = fm.beginTransaction();
            LessonView l=new LessonView();
            Bundle bundle=new Bundle();
            bundle.putInt("lessonId",getIntent().getIntExtra("classID",1));
            bundle.putString("year",getIntent().getStringExtra("year"));
            bundle.putString("subject",getIntent().getStringExtra("subject"));
            bundle.putString("medias",getIntent().getStringExtra("medias"));
            bundle.putString("className",getIntent().getStringExtra("className"));
            l.setArguments(bundle);
            ft.add(R.id.containerLesson, l);
            ft.commit();
        }).start();
    }
}
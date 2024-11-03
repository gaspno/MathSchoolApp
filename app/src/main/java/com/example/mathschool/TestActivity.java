package com.example.mathschool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mathschool.fragments.TestView;

public class TestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        new Thread(() -> {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            TestView t=new TestView();
            Bundle bundle=new Bundle();
            bundle.putInt("testID",getIntent().getIntExtra("testID",1));
            bundle.putString("subject",getIntent().getStringExtra("subject"));
            bundle.putString("year",getIntent().getStringExtra("year"));
            bundle.putSerializable("questions",getIntent().getSerializableExtra("questions"));
            t.setArguments(bundle);
            ft.add(R.id.containerLesson, t);
            ft.commit();
        }).start();
    }
}
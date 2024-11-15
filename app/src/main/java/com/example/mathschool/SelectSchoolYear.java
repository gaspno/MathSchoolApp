package com.example.mathschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mathschool.model.SchoolYear;
import com.example.mathschool.services.ContentService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//Essa activy gerencia a escolha do ano escolar
public class SelectSchoolYear extends AppCompatActivity {

    private Thread buttonThread=new Thread(() -> {
        try {
            setButtons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    private ContentService contentService=new ContentService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school_year);
        buttonThread.start();
    }

    private void setButtons() throws IOException {
        LinearLayout linearLayout=findViewById(R.id.yearsList);
        //Obtém lista de "anos" escolares do back-end.
        List<SchoolYear> years= contentService.getYears().stream().collect(Collectors.toList());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0,16);
        //criar um botão para cada ano passando as informações ao ser clicado para o proxima activity
        years.forEach(y->{
            Button b=new Button(this);
            b.setText(y.getName());
            b.setTextColor(Color.BLACK);
            b.setOnClickListener(v -> {
                Intent intent=new Intent(getApplicationContext(), ProgressActivity.class);
                intent.putExtra("year",y.getName());
                startActivity(intent);
            });
            b.setBackground(getDrawable(R.drawable.bg_select));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.addView(b,params);
                }
            });

        });
    }


}
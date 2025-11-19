package com.example.mathschool.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mathschool.R;
import com.example.mathschool.model.Question;
import com.example.mathschool.model.StudentRegister;
import com.example.mathschool.services.ProgressService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class TestView extends Fragment {
    private TextView alternativa1;
    private TextView alternativa2;
    private TextView alternativa3;
    private TextView alternativa4;
    private TextView questionText;
    private Button confirm;
    private int questionIndex=0;
    private List<Question> questions;
    private CardView cardView;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private TextView titleText;

    public TestView() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questions = (ArrayList<Question>) getArguments().getSerializable("questions");
        return inflater.inflate(R.layout.fragment_test_view, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alternativa1=view.findViewById(R.id.option1);
        alternativa2=view.findViewById(R.id.option2);
        alternativa3=view.findViewById(R.id.option3);
        alternativa4=view.findViewById(R.id.option4);
        questionText=view.findViewById(R.id.questionTEXT);
        confirm=view.findViewById(R.id.buttonCONFIRM_ASNWER);
        cardView=view.findViewById(R.id.cardViewQuestion);
        titleText = getActivity().findViewById(R.id.textYear);
        titleText.setText(getArguments().getString("year")+" :  "+getArguments().getInt("testID"));
        setupQuestion();
    }

    private void setupQuestion() {
        final int[] checkableID = {0};
        cardView.setCardBackgroundColor(Color.WHITE);
        alternativa1.setText(questions.get(questionIndex).getAlternativa1());
        alternativa2.setText(questions.get(questionIndex).getAlternativa2());
        alternativa3.setText(questions.get(questionIndex).getAlternativa3());
        alternativa4.setText(questions.get(questionIndex).getAlternativa4());
        alternativa1.setOnClickListener(v ->{
            checkableID[0] = 1;
            setColorOptions(alternativa1.getId());
        });
        alternativa2.setOnClickListener(v ->{
            checkableID[0] = 2;
            setColorOptions(alternativa2.getId());
        });
        alternativa3.setOnClickListener(v ->{
            checkableID[0] = 3;
            setColorOptions(alternativa3.getId());
        });
        alternativa4.setOnClickListener(v ->{
            checkableID[0] = 4;
            setColorOptions(alternativa4.getId());
        });
        questionText.setText(questions.get(questionIndex).getEnunciado());
        questionText.setTextColor(Color.BLACK);
        confirm.setOnClickListener(v -> {
            if (questions.get(questionIndex).getIndexRespostaCorreta()==(checkableID[0])) {
                cardView.setCardBackgroundColor(Color.GREEN);
                questionText.setTextColor(Color.BLACK);
                if (questionIndex == questions.size() - 1) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        CompletableFuture<String> tokenFuture = new CompletableFuture<>();
                        user.getIdToken(true).addOnSuccessListener(getTokenResult -> tokenFuture.complete(getTokenResult.getToken()));

                        CompletableFuture<Integer> studentIdFuture = new CompletableFuture<>();
                        firebaseDatabase.getReference()
                                .child("users").child(user.getUid()).child("id_back-end").get()
                                .addOnSuccessListener(dataSnapshot -> {
                                    Long l = (Long) dataSnapshot.getValue();
                                    studentIdFuture.complete(l.intValue());
                                });

                        CompletableFuture.allOf(tokenFuture, studentIdFuture).thenAccept(voidResult -> {
                            String firebaseToken = tokenFuture.join();
                            int studentId = studentIdFuture.join();
                            try {
                                StudentRegister register = ProgressService.registerCompletedTest(studentId, getArguments().getInt("testID"), firebaseToken);
                                Log.d("IsRegistered", register.toString());
                                getActivity().runOnUiThread(() -> {
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.registered_dialog);
                                    Button button = dialog.findViewById(R.id.returnBUTTON);
                                    TextView textViewCongratulation = dialog.findViewById(R.id.textCongratulations);
                                    textViewCongratulation.setText(getString(R.string.completedCongratulationsTest));
                                    button.setOnClickListener(v1 -> {
                                        try {
                                            getActivity().finish();
                                        } catch (Throwable e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                    TextView testNameText = dialog.findViewById(R.id.classNAMETextView);
                                    testNameText.setText(getArguments().getString("subject"));
                                    dialog.setCancelable(false);
                                    dialog.show();
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.registered_dialog);
                            Button button = dialog.findViewById(R.id.returnBUTTON);
                            TextView textViewCongratulation = dialog.findViewById(R.id.textCongratulations);
                            textViewCongratulation.setText(getString(R.string.completedCongratulationsTest));
                            button.setOnClickListener(v1 -> {
                                try {
                                    getActivity().finish();
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            TextView testNameText = dialog.findViewById(R.id.classNAMETextView);
                            testNameText.setText(getArguments().getString("subject"));
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        });
                    }
                } else {
                    questionIndex++;
                    setupQuestion();
                }
            }else {
                cardView.setCardBackgroundColor(Color.RED);

            }});
    }
    private void setColorOptions(int id) {
        List<TextView> list=Arrays.asList(alternativa1,alternativa2,alternativa3,alternativa4);
        list.forEach(e->{
            if (e.getId()!=id){
                e.setBackgroundColor(Color.WHITE);
            }else {
                e.setBackgroundColor(Color.GREEN);
            }
        });

    }
}
package com.example.mathschool.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mathschool.R;
import com.example.mathschool.custom.CustomSchoolView;
import com.example.mathschool.minigameviews.Quiz;
import com.example.mathschool.minigameviews.RelationalValues;
import com.example.mathschool.minigameviews.ResolutionGuided;
import com.example.mathschool.minigameviews.data.QuizData;
import com.example.mathschool.minigameviews.data.RelationalValuesData;
import com.example.mathschool.minigameviews.data.ResolutionValuesData;
import com.example.mathschool.model.ClassData;
import com.example.mathschool.model.StudentRegister;
import com.example.mathschool.services.ProgressService;
import com.example.mathschool.utils.CreateViewElement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class LessonView extends Fragment {

    private FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    private volatile ClassData lesson;
    private CustomSchoolView scrollView;
    private final HashMap<String, Boolean> isWatchedGames = new HashMap<>();
    private final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private TextView titleText;

    Runnable r = () -> {
        while (!getView().isEnabled()) ;
        createView();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_text, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.findViewById(R.id.containerElements);
        int lessonId=getArguments().getInt("lessonId");
        String nameClass=getArguments().getString("className");
        String medias=getArguments().getString("medias");
        this.lesson=new ClassData(nameClass, Arrays.stream(medias.substring(1,medias.length()-1).split(", ")).collect(Collectors.toList()), lessonId);
        titleText = getActivity().findViewById(R.id.textYear);
        titleText.setText(getArguments().getString("year")+" :  "+lesson.getName());
        new Thread(r).start();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void createView() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 25, 25, 25);
        getActivity().runOnUiThread(() -> {
            scrollView.addView(layout);
            scrollView.post(() -> {
            new android.os.Handler().postDelayed(() -> {
                lesson.getContent_urls().forEach(value -> {
            View v;
            String type = value.substring(0, value.indexOf(" "));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getView().getWidth(), getView().getWidth());
            layoutParams.setMargins(0, 25, 0, 25);
            switch (type) {
                case "text":
                    v = CreateViewElement.getTextView(getContext(), value.substring(value.indexOf(" ")));
                    View finalV = v;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0,10,0,10);
                    finalV.setLayoutParams(params);
                    getActivity().runOnUiThread(() -> {
                        layout.addView(finalV);
                    });


                    break;
                case "video":
                    getActivity().runOnUiThread(() -> {
                        VideoView videoView = CreateViewElement.getVideoView(getContext(), value.substring(value.indexOf(" ") + 1), (getView().getWidth() * 9) / 16);
                        layout.addView(videoView);
                        MediaController mediaController = new MediaController(getContext());
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.setOnPreparedListener(mediaPlayer -> mediaController.setAnchorView(videoView));
                        RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(getView().getWidth(), getView().getWidth());
                        videoParams.setMargins(0, 100, 0, 100);
                        videoView.setLayoutParams(videoParams);
                        scrollView.getViewTreeObserver().addOnScrollChangedListener(mediaController::hide);
                    });
                    break;
                case "image":
                    v = CreateViewElement.getImageView(getContext(), value.substring(value.indexOf(" ") + 1));
                    v.setLayoutParams(layoutParams);
                    View finalV1 = v;
                    getActivity().runOnUiThread(() -> layout.addView(finalV1));
                    break;
                case "quiz":
                    String dataQuiz=value.substring(value.indexOf("guided")+5);
                    try {
                        JSONObject jsonObject = new JSONObject(dataQuiz);
                        String element1 = jsonObject.getString("alternativa1");
                        String element2 = jsonObject.getString("alternativa2");
                        String element3 = jsonObject.getString("alternativa3");
                        String element4 = jsonObject.getString("alternativa4");
                        String correct = jsonObject.getString("correctnswer");
                        String enunciado=jsonObject.getString("enunciado");
                        Quiz q = CreateViewElement.getGameQuiz(getContext(), getView().getWidth() - 50, getView().getWidth(),
                                new QuizData(new String[]{element1, element2, element3, element4}, Integer.parseInt(correct),enunciado));
                        isWatchedGames.put(value, false);
                        q.setOnResultCorrect(() -> isWatchedGames.put(value, true));
                        q.setLayoutParams(layoutParams);
                        getActivity().runOnUiThread(() -> layout.addView(q));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "guided":
                    String dataGuided=value.substring(value.indexOf("guided")+7);
                    try {
                        JSONObject jsonObject=new JSONObject(dataGuided);
                        String options1[]=new String[]{jsonObject.getString("element1"),jsonObject.getString("element2"),jsonObject.getString("element3"),jsonObject.getString("element4")};
                        String options2[]=new String[]{jsonObject.getString("element5"),jsonObject.getString("element6"),jsonObject.getString("element7"),jsonObject.getString("element8")};
                        String options3[]=new String[]{ jsonObject.getString("element9"),jsonObject.getString("element10"),jsonObject.getString("element11"),jsonObject.getString("element12")};
                        String operations[]=new String[]{
                                jsonObject.getString("operation1"),jsonObject.getString("operation2"),"="};
                        String equation[]=new String[]
                                {jsonObject.getString("equation1"),jsonObject.getString("equation2"),jsonObject.getString("equation3"),jsonObject.getString("equation4")};
                        String correct=jsonObject.getString("correct");
                        int position=0;
                        for (int i=0;i<equation.length;i++){
                            if (equation[i].equals("")){
                                position=i;
                                break;
                            }
                        }
                        ResolutionGuided rg = CreateViewElement.getResolutionGuided(getContext(), getView().getWidth() - 50, getView().getWidth(), scrollView,new ResolutionValuesData(
                                options1,
                                options2,
                                options3,
                                operations,
                                equation,
                                correct,
                                position
                        ));
                        isWatchedGames.put(value,false);
                        rg.setOnResultCorrect(() -> isWatchedGames.put(value,true));
                        rg.setLayoutParams(layoutParams);
                        getActivity().runOnUiThread(() -> layout.addView(rg));
                        break;


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                case "relational":
                    String dataRelational=value.substring(value.indexOf("relational")+11);
                    try {
                        JSONObject jsonObject=new JSONObject(dataRelational);
                        JSONObject column1Json=jsonObject.getJSONObject("column1");
                        JSONObject column2Json=jsonObject.getJSONObject("column2");
                        String[] sequenceString=jsonObject.getString("column3").replaceAll("[\\[\\]\"]","").split(",");
                        String column1[]=new String[]
                                {column1Json.getString("element1Coluna1"),column1Json.getString("element2Coluna1"),column1Json.getString("element3Coluna1"),column1Json.getString("element4Coluna1")};
                        String column2[]=new String[]
                                {column2Json.getString("element1Coluna2"),column2Json.getString("element2Coluna2"),column2Json.getString("element3Coluna2"),column2Json.getString("element4Coluna2")};
                        int sequence[]=new int[]{Integer.parseInt(sequenceString[0])-1,Integer.parseInt(sequenceString[1])-1,Integer.parseInt(sequenceString[2])-1,Integer.parseInt(sequenceString[3])-1};
                        RelationalValues r = CreateViewElement.getRelativeValues(getContext(), getView().getWidth() - 50, getView().getWidth(),
                                new RelationalValuesData(column1,column2, sequence));
                        isWatchedGames.put(value, false);
                        r.setOnResultCorrect(() -> isWatchedGames.put(value, true));
                        r.setLayoutParams(layoutParams);
                        getActivity().runOnUiThread(() -> layout.addView(r));
                        break;
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                default:
            }
        });
            });
            }, 100);
        });
        Button finish = getButton();
        finish.setBackground(getContext().getDrawable(R.drawable.text_back));
        finish.setTextColor(Color.WHITE);
        getActivity().runOnUiThread(() -> layout.addView(finish));
        while (!finish.isShown());
        //directs the activity to the top of the scrollview
        getActivity().runOnUiThread(()->scrollView.fullScroll(View.FOCUS_UP));
    }
    private @NonNull View getButton() {
        View finish = LayoutInflater.from(getContext()).inflate(R.layout.finish_button, null);
        DatabaseReference base= firebaseDatabase.getReference()
                .child("users").child(user.getUid());
        finish.setOnClickListener(v -> {
            boolean isFinish=false;
            for (Map.Entry<String, Boolean> entry : isWatchedGames.entrySet()) {
                if (entry.getValue()) {
                    isFinish = true;
                } else {
                    isFinish = false;
                    break;
                }
            }
            if (isWatchedGames.isEmpty())
                isFinish=true;
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            if (isFinish) {
                getView().setBackgroundColor(Color.GREEN);
                CompletableFuture<String> tokenFuture = new CompletableFuture<>();
                user.getIdToken(true).addOnSuccessListener(getTokenResult -> tokenFuture.complete(getTokenResult.getToken()));

                CompletableFuture<Integer> studentIdFuture = new CompletableFuture<>();
                base.child("id_back-end").get().addOnSuccessListener(dataSnapshot -> {
                    Long l = (Long) dataSnapshot.getValue();
                    studentIdFuture.complete(l.intValue());
                });

                CompletableFuture.allOf(tokenFuture, studentIdFuture).thenAccept(voidResult -> {
                    String firebaseToken = tokenFuture.join();
                    int studentId = studentIdFuture.join();
                    try {
                        StudentRegister register = ProgressService.registerWatchedClass(studentId, lesson.getId(), firebaseToken);
                        Log.d("IsRegistered", register.toString());
                        getActivity().runOnUiThread(() -> {
                            Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.registered_dialog);
                                Button button=dialog.findViewById(R.id.returnBUTTON);
                                TextView textViewCongratulation=dialog.findViewById(R.id.textCongratulations);
                                textViewCongratulation.setText(getString(R.string.completedCongratulationsClass));
                                button.setOnClickListener(v12 -> {
                                    try {
                                        getActivity().finish();
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                TextView classNameText=dialog.findViewById(R.id.classNAMETextView);
                                classNameText.setText(getArguments().getString("subject"));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }else {
                Toast.makeText(getContext(),"You need complete all quest",Toast.LENGTH_LONG).show();
            }
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog=new Dialog(getContext());
                        dialog.setContentView(R.layout.registered_dialog);
                        Button button=dialog.findViewById(R.id.returnBUTTON);
                        TextView textViewCongratulation=dialog.findViewById(R.id.textCongratulations);
                        textViewCongratulation.setText(getString(R.string.completedCongratulationsClass));
                        button.setOnClickListener(v1 -> {
                            try {
                                getActivity().finish();
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                        });
                        TextView classNameText=dialog.findViewById(R.id.classNAMETextView);
                        classNameText.setText(getArguments().getString("subject"));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
            }

        });
        return finish;
    }

}
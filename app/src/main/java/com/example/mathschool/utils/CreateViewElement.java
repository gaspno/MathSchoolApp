package com.example.mathschool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.example.mathschool.R;
import com.example.mathschool.custom.CustomSchoolView;
import com.example.mathschool.minigameviews.Quiz;
import com.example.mathschool.minigameviews.RelationalValues;
import com.example.mathschool.minigameviews.ResolutionGuided;
import com.example.mathschool.minigameviews.data.QuizData;
import com.example.mathschool.minigameviews.data.RelationalValuesData;
import com.example.mathschool.minigameviews.data.ResolutionValuesData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//classe para criar views para aulas
public class CreateViewElement {


    public static TextView getTextView( Context context,String text){
        TextView textView=new TextView(context);
        textView.setBackground(context.getDrawable(R.drawable.text_back));
        textView.setTextSize(18);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(15,15,15,15);
        textView.setText(text);
        return textView;
    }
    public static ImageView getImageView(Context context,String url){
        ImageView imageView=new ImageView(context);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference=storage.getReference().child(url);
        reference.getBytes(1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imageView.setImageBitmap(bitmap);
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Log.d("Error na stream",e.getMessage()+"authenticate "+ FirebaseAuth.getInstance().getCurrentUser());
        });
        return imageView;
    }
    public static VideoView getVideoView(Context context, String url,int width){
        VideoView videoView=new VideoView(context);
        videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (width*16)/10));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference=storage.getReference().child(url);
        reference.getDownloadUrl().addOnSuccessListener(uri -> {
            videoView.setVideoURI(uri);
            videoView.requestFocus();
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
         });
        return videoView;
    }



    public static Quiz getGameQuiz(Context context, int width, int width1,QuizData data) {
        Quiz q=new Quiz(context,width,width1,data);
        return q;
    }

    public static ResolutionGuided getResolutionGuided(Context context, int width, int width1, CustomSchoolView customSchoolView, ResolutionValuesData data) {
        ResolutionGuided r= new ResolutionGuided(context,width,width1,customSchoolView,data);
        return r;
    }

    public static RelationalValues getRelativeValues(Context context, int width, int height,RelationalValuesData data) {
        RelationalValues r= new RelationalValues(context,width,height,data);
        return r;
    }
}

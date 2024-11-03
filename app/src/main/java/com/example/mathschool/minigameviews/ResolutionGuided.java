package com.example.mathschool.minigameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.mathschool.custom.CustomSchoolView;
import com.example.mathschool.interfaces.OnResultCorrect;
import com.example.mathschool.minigameviews.data.ResolutionValuesData;

public class ResolutionGuided extends View {
    private final Paint textPaint;
    private Paint paint=new Paint();
    boolean[] isPressedLine1 ={false,false,false,false};
    boolean[] isPressedLine2 ={false,false,false,false};
    boolean[] isPressedLine3 ={false,false,false,false};
    float[][] line1Directions ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    float[][] line2Directions ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    float[][] line3Directions ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    private boolean actualPress=false;
    int columnChoose=-1;
    int lineChoose=-1;
    float xInit=-1;
    float yInit=-1;
    private CustomSchoolView customSchoolView;
    private OnResultCorrect onResultCorrect;
    private ResolutionValuesData data;
    private int colorBackground=Color.BLUE;
    private boolean isFinish=false;

    public ResolutionGuided(Context context, int width, int height, CustomSchoolView customSchoolView,ResolutionValuesData data) {

        super(context);
        this.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        this.customSchoolView=customSchoolView;
        this.data=data;
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        setClickable(true);
    }
    public void setOnResultCorrect(OnResultCorrect onResultCorrect) {
        this.onResultCorrect = onResultCorrect;
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        textPaint.setTextSize(getWidth()/20);
        canvas.drawColor(colorBackground);
        paint.setColor(Color.BLACK);
        canvas.drawRect(((float) (getWidth() - 50) /40),2*((getHeight()/3))+(getHeight()/10),-((float) (getWidth() - 50) /40)+(float) (getWidth()-50),getHeight()-((float) getHeight() /40),paint);
        for (int i = 0; i <4 ; i++) {
            float x1=(float) (getWidth() - 50) /24+ (float) (i* ((getWidth()-50)/4));
            float y1=2*((getHeight()/3))+(getHeight()/10+(getHeight()/40));
            float x2=-((float) (getWidth() - 50) /24)+(float) ((i+1)* ((getWidth()-50)/4));
            float y2=(float) (getHeight())-(getHeight()/24);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(new RectF(x1,y1,x2,y2 ),10,10,paint);
            if (i<3)
                canvas.drawText(data.getEquations()[i],x2+(getWidth() - 50) /24,getWidth()/40+(y1+y2)/2,textPaint);
            textPaint.setColor(Color.BLACK);
            canvas.drawText(data.getSolutions()[i], (x1+x2)/2,  y2,textPaint);
            textPaint.setColor(Color.WHITE);
        }

        for (int i = 0; i <4 ; i++) {
            if (isPressedLine1[i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            float x1;
            float y1;
            float x2;
            float y2;
            if (!isPressedLine1[i]){
                x1=(float) (getWidth() - 50) /20+ (float) (i* ((getWidth()-50)/4));
                y1=(float) (getHeight()/12);
                x2=-((float) (getWidth() - 50) /20)+(float) ((i+1)* ((getWidth()-50)/4));
                y2=(float) (getHeight()/3)-((float) getHeight() /12);
            }
            else {
                x1=line1Directions[i][0];
                y1= line1Directions[i][1];
                x2=line1Directions[i][2];
                y2=line1Directions[i][3];
            }
            canvas.drawRoundRect(new RectF(x1, y1,x2, y2),10,10,paint);
            canvas.drawText(data.getColumn1()[i], (x1+x2)/2,  y2,textPaint);
            line1Directions[i][0]=x1;
            line1Directions[i][1]=y1;
            line1Directions[i][2]=x2;
            line1Directions[i][3]=y2;
        }
        paint.setColor(Color.BLACK);
        for (int i = 0; i <4 ; i++) {
            if (isPressedLine2[i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            float x1;
            float y1;
            float x2;
            float y2;
            if (!isPressedLine2[i]){
                x1=(float) (getWidth() - 50) /20+ (float) (i* ((getWidth()-50)/4));
                y1=(float) (getHeight()/3);
                x2=-((float) (getWidth() - 50) /20)+(float) ((i+1)* ((getWidth()-50)/4));
                y2=(float) 2*(getHeight()/3)-2*(getHeight()/12);
            }
            else {
                x1=line2Directions[i][0];
                y1= line2Directions[i][1];
                x2=line2Directions[i][2];
                y2=line2Directions[i][3];
            }
            canvas.drawRoundRect(new RectF(x1,y1,x2,y2) ,10,10,paint);
            canvas.drawText(data.getColumn2()[i], (x1+x2)/2,  y2,textPaint);
            line2Directions[i][0]=x1;
            line2Directions[i][1]=y1;
            line2Directions[i][2]=x2;
            line2Directions[i][3]=y2;
        }
        for (int i = 0; i <4 ; i++) {
            if (isPressedLine3[i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            float x1;
            float y1;
            float x2;
            float y2;
            if (!isPressedLine3[i]){
                x1=(float) (getWidth() - 50) /20+ (float) (i* ((getWidth()-50)/4));
                y1=2*((getHeight()/3))-((float) getHeight() /12);
                x2=-((float) (getWidth() - 50) /20)+(float) ((i+1)* ((getWidth()-50)/4));
                y2=(float) 2*((getHeight()/3))+(getHeight()/12);
            }
            else {
                x1=line3Directions[i][0];
                y1=line3Directions[i][1];
                x2=line3Directions[i][2];
                y2=line3Directions[i][3];
            }
            canvas.drawRoundRect(new RectF(x1,y1,x2,y2 ),10,10,paint);
            canvas.drawText(data.getColumn3()[i], (x1+x2)/2,  y2,textPaint);
            line3Directions[i][0]=x1;
            line3Directions[i][1]=y1;
            line3Directions[i][2]=x2;
            line3Directions[i][3]=y2;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isFinish){
        float x=event.getX();
        float y=event.getY();
        if (!actualPress) {
            for (int i = 0; i < 4; i++) {
                if (x > line1Directions[i][0] && x < line1Directions[i][2] && y > line1Directions[i][1] && y < line1Directions[i][3] && event.getAction() == MotionEvent.ACTION_DOWN) {
                    isPressedLine1[i] = !isPressedLine1[i];
                    actualPress = true;
                    lineChoose = 0;
                    columnChoose = i;
                    xInit = x;
                    yInit = y;
                    invalidate();
                }
            }
            for (int i = 0; i <4 ; i++) {
                if (x>line2Directions[i][0]&&x<line2Directions[i][2]&&y>line2Directions[i][1]&&y<line2Directions[i][3]&&event.getAction()==MotionEvent.ACTION_DOWN) {
                    isPressedLine2[i]=!isPressedLine2[i];
                    actualPress=true;
                    lineChoose=1;
                    columnChoose=i;
                    xInit=x;
                    yInit=y;
                    invalidate();
                }
            }
            for (int i = 0; i <4 ; i++) {
                if (x>line3Directions[i][0]&&x<line3Directions[i][2]&&y>line3Directions[i][1]&&y<line3Directions[i][3]&&event.getAction()==MotionEvent.ACTION_DOWN) {
                    isPressedLine3[i]=!isPressedLine3[i];
                    actualPress=true;
                    lineChoose=2;
                    columnChoose=i;
                    xInit=x;
                    yInit=y;
                    invalidate();
                }
            }
        }
        else{
            customSchoolView.setScrollable(false);
            float dX=x-xInit;
            float dY=y-yInit;
            xInit=x;
            yInit=y;
            Log.d( " dY: "," "+y);
            Log.d( "onTouchEvent dX: "," "+dX);
            switch (lineChoose){
                case 0:
                    line1Directions[columnChoose][0]+=(dX);
                    line1Directions[columnChoose][1]+=(dY);
                    line1Directions[columnChoose][2]+=(dX);
                    line1Directions[columnChoose][3]+=(dY);

                    break;
                case 1:
                    line2Directions[columnChoose][0]+=(dX);
                    line2Directions[columnChoose][1]+=(dY);
                    line2Directions[columnChoose][2]+=(dX);
                    line2Directions[columnChoose][3]+=(dY);
                    break;
                case 2:
                    line3Directions[columnChoose][0]+=(dX);
                    line3Directions[columnChoose][1]+=(dY);
                    line3Directions[columnChoose][2]+=(dX);
                    line3Directions[columnChoose][3]+=(dY);
                    break;
            }if (event.getAction()==MotionEvent.ACTION_UP){
               switch (lineChoose){
                   case 0:
                       isPressedLine1[columnChoose]=false;
                       for (int i = 0; i <3 ; i++) {
                           float x1=(float) (getWidth() - 50) /24+ (float) (i* ((getWidth()-50)/4));
                           float y1=2*((getHeight()/3))+(getHeight()/10);
                           float x2=-((float) (getWidth() - 50) /24)+(float) ((i+1)* ((getWidth()-50)/4));
                           float y2=getHeight()-((float) getHeight() /40);
                           if (x1<line1Directions[columnChoose][0]&&x2>line1Directions[columnChoose][2]&&y1<line1Directions[columnChoose][1]&&y2>line1Directions[columnChoose][3]) {
                               if (Integer.valueOf(data.getColumn1()[columnChoose]).equals(Integer.valueOf(data.getCorrect())) &&i==data.getPosition()){
                                   colorBackground=Color.GREEN;
                                   finish();
                               }else {
                                   colorBackground=Color.RED;
                               }
                           }
                       }
                       break;
                   case 1:
                       isPressedLine2[columnChoose]=false;
                       for (int i = 0; i <3 ; i++) {
                           float x1=(float) (getWidth() - 50) /24+ (float) (i* ((getWidth()-50)/4));
                           float y1=2*((getHeight()/3))+(getHeight()/10);
                           float x2=-((float) (getWidth() - 50) /24)+(float) ((i+1)* ((getWidth()-50)/4));
                           float y2=getHeight()-((float) getHeight() /40);
                           if (x1<line2Directions[columnChoose][0]&&x2>line2Directions[columnChoose][2]&&y1<line2Directions[columnChoose][1]&&y2>line2Directions[columnChoose][3]) {
                               if (Integer.valueOf(data.getColumn2()[columnChoose]).equals(Integer.valueOf(data.getCorrect())) &&i==data.getPosition()){
                                   colorBackground=Color.GREEN;
                                   finish();
                               }else {
                                   colorBackground=Color.RED;
                               }
                           }
                       }
                       break;
                   case 2:
                       isPressedLine3[columnChoose]=false;
                       for (int i = 0; i <3 ; i++) {
                           float x1=(float) (getWidth() - 50) /24+ (float) (i* ((getWidth()-50)/4));
                           float y1=2*((getHeight()/3))+(getHeight()/10);
                           float x2=-((float) (getWidth() - 50) /24)+(float) ((i+1)* ((getWidth()-50)/4));
                           float y2=getHeight()-((float) getHeight() /40);
                           if (x1<line3Directions[columnChoose][0]&&x2>line3Directions[columnChoose][2]&&y1<line3Directions[columnChoose][1]&&y2>line3Directions[columnChoose][3]) {
                               if (Integer.valueOf(data.getColumn3()[columnChoose]).equals(Integer.valueOf(data.getCorrect())) &&i==data.getPosition()){
                                   colorBackground=Color.GREEN;
                                   finish();
                               }else {
                                   colorBackground=Color.RED;
                               }
                           }
                       }
                       break;
               }
                   actualPress=false;
                   lineChoose=-1;
                   columnChoose=-1;
                   customSchoolView.setScrollable(true);
               }
               invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    private void finish() {
        onResultCorrect.OnCorrect();
        isFinish=true;
        setClickable(false);
    }
}

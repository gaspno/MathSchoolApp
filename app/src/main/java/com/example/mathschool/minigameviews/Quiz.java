package com.example.mathschool.minigameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.mathschool.interfaces.OnResultCorrect;
import com.example.mathschool.minigameviews.data.QuizData;

public class Quiz extends View {

    private Paint paint=new Paint();
    private QuizData data;
    private TextPaint textPaint;
    float direction[][] ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0,0}};
    private boolean[] isPress=new boolean[]{false,false,false,false,false};
    private boolean isCorrect=false;
    private OnResultCorrect onResultCorrect;
    int choseAlternative =-1;
    private boolean isWrong=false;
    private boolean isFinishedAnswer=false;
    private StaticLayout staticLayout;

    public Quiz(Context context, int width, int height, QuizData quizData) {
        super(context);
        this.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        this.data=quizData;
        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);

        setClickable(true);
    }

    public void setOnResultCorrect(OnResultCorrect onResultCorrect) {
        this.onResultCorrect = onResultCorrect;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTextSize(getWidth()/20);
        if (isCorrect)
            paint.setColor(Color.GREEN);
        else if(isWrong)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.BLACK);
        canvas.drawColor(Color.BLUE);
        canvas.drawRoundRect(new RectF(25,25,getWidth()-75, (float) getHeight() /2),50,50,paint);
        staticLayout = new StaticLayout(
                data.getStatement(),
                textPaint,
                getWidth()-50,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0,
                false
        );
        canvas.save();
        canvas.translate((float) (25 + getWidth() - 75) /2, (getHeight()/2+25)/2);
        staticLayout.draw(canvas);
        canvas.restore();


        for (int i = 0; i <4 ; i++) {
            float x1=(float) (getWidth() - 50) /20+ (float) (i* ((getWidth()-50)/4));
            float x2=-((float) (getWidth() - 50) /20)+(float) ((i+1)* ((getWidth()-50)/4));
            float y1=(float) (getHeight()/2)+((float) getHeight() /8);
            float y2= (getHeight()-((float) getHeight() /6));
            direction[i][0]=x1;
            direction[i][1]=y1;
            direction[i][2]=x2;
            direction[i][3]=y2;
            if (isPress[i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            canvas.drawRoundRect(new RectF(x1,y1,x2,y2),10,10,paint);
            canvas.drawText(data.getQuestion()[i],(x1+x2)/2,  y2,textPaint);
        }
        if (isPress[4]){
            paint.setColor(Color.GREEN);
        }else {
            paint.setColor(Color.BLACK);
        }
        float bX1= (float) ((getWidth() - 50) /2)-(float) (getWidth() - 50)/4;
        float bY1= getHeight()-((float) getHeight() /8);
        float bX2= ((float) (getWidth() - 50) /2)+((float) (getWidth() - 50) /4);
        float bY2=getHeight()-((float) getHeight() /20);
        direction[4][0]=bX1;
        direction[4][1]=bY1;
        direction[4][2]=bX2;
        direction[4][3]=bY2;
        canvas.drawRect(bX1,bY1,bX2,bY2,paint);
        canvas.drawText("Confirm",((float) ((getWidth() - 50) /2)-((float) (getWidth() - 50) /4)+((float) (getWidth() - 50) /2)+((float) (getWidth() - 50) /4))/2,  getHeight()-((float) getHeight() /20),textPaint);

        //create count

        //generate alternatives

        //detects chooses
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFinishedAnswer){
        float x=event.getX();
        float y=event.getY();
        //Log.d("isTouched Event Dir"," x:"+x+"        y: "+y);
        //Log.d("isTouched Button dir","x1: "+direction[0][2]+" x2: "+direction[0][0]+" y1 :"+direction[0][3]+" y2: "+direction[0][1]);
        for (int i = 0; i <4; i++) {
            if (x>direction[i][0]&&x<direction[i][2]&&y>direction[i][1]&&y<direction[i][3]&&event.getAction()==MotionEvent.ACTION_DOWN){
                isPress[i]=!isPress[i];
                choseAlternative =i+1;
                for (int j = 0; j <4 ; j++) {
                    if (j!=i){
                        isPress[j]=false;
                    }
                }
                invalidate();
            }
        }
        if (x>direction[4][0]&&x<direction[4][2]&&y>direction[4][1]&&y<direction[4][3]&&event.getAction()==MotionEvent.ACTION_DOWN){
            isPress[4]=true;
            invalidate();
        }
        if (x>direction[4][0]&&x<direction[4][2]&&y>direction[4][1]&&y<direction[4][3]&&event.getAction()==MotionEvent.ACTION_UP){
            isPress[4]=false;
            if ((choseAlternative)==data.getIndexAnswer()){
                isCorrect=true;
                isWrong=false;
                isFinishedAnswer=true;
                onResultCorrect.OnCorrect();
            }
            else{
                isCorrect=false;
                isWrong=true;
            }
            invalidate();
        }}
        return super.onTouchEvent(event);
    }


}

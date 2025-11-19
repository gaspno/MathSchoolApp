package com.example.mathschool.minigameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.mathschool.interfaces.OnResultCorrect;
import com.example.mathschool.minigameviews.data.RelationalValuesData;

public class RelationalValues extends View {

    private Paint paint=new Paint();
    private OnResultCorrect onResultCorrect;
    float column1Directions[][] ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    float column2Directions[][] ={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    private Paint textPaint;
    private RelationalValuesData data;
    private boolean isWrong=false;
    private boolean isFinishedAnswer=false;
    private boolean isCorrect=false;
    private boolean[][] isPressOrConfirmedColumn1 =new boolean[][]{{false,false,false,false},{false,false,false,false}};
    private boolean[][] isPressOrConfirmedColumn2 =new boolean[][]{{false,false,false,false},{false,false,false,false}};
    private boolean isConfirmPressed=false;
    private int[]correctRelation;
    private int firstSelect=-1;
    private int secondSelect=-1;
    private boolean allTrue=false;
    private int backGroundColor=Color.BLUE;

    public void setOnResultCorrect(OnResultCorrect onResultCorrect) {
        this.onResultCorrect = onResultCorrect;
    }


    public RelationalValues(Context context, int width, int height,RelationalValuesData data ) {
        super(context);
        this.setLayoutParams(new LinearLayout.LayoutParams(width, height ));
        correctRelation=data.getSecondColumnRelation();
        this.data=data;
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        setClickable(true);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        textPaint.setTextSize(getWidth()/20);
        canvas.drawColor(backGroundColor);
        if (isCorrect)
            paint.setColor(Color.GREEN);
        else if(isWrong)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.BLACK);
        int marginLateral=(getWidth()-50)/10;
        int marginHeight=getHeight()/20;
        float heightItem=getHeight()/5-marginHeight*2;
        float widthItem=getWidth()/2-marginLateral*2;
        for (int i = 0; i <4 ; i++) {
            float x1=marginLateral;
            float x2=widthItem+marginLateral;
            float y1=i*(heightItem+2*marginHeight)+marginHeight;
            float y2= i*(heightItem+2*marginHeight)+marginHeight+heightItem;
            if (isPressOrConfirmedColumn1[1][i]){
                paint.setColor(Color.YELLOW);
            }
            else if (isPressOrConfirmedColumn1[0][i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            column1Directions[i][0]=x1;
            column1Directions[i][1]=y1;
            column1Directions[i][2]=x2;
            column1Directions[i][3]=y2;
            canvas.drawRoundRect(new RectF(x1,y1,x2, y2),10,10,paint);
            canvas.drawText(data.getColumn1()[i], (x1+x2)/2,  y2,textPaint);
        }
        for (int i = 0; i <4 ; i++) {
            float x1=marginLateral+getWidth()/2-50;
            float x2=widthItem+marginLateral+getWidth()/2-50;
            float y1=i*(heightItem+2*marginHeight)+marginHeight;
            float y2= i*(heightItem+2*marginHeight)+marginHeight+heightItem;
            if (isPressOrConfirmedColumn2[1][i]){
                paint.setColor(Color.YELLOW);
            }
            else if (isPressOrConfirmedColumn2[0][i]){
                paint.setColor(Color.GREEN);
            }else {
                paint.setColor(Color.BLACK);
            }
            column2Directions[i][0]=x1;
            column2Directions[i][1]=y1;
            column2Directions[i][2]=x2;
            column2Directions[i][3]=y2;
            canvas.drawRoundRect(new RectF(x1,y1,x2,y2 ),10,10,paint);
            canvas.drawText(data.getColumn2()[i], (x1+x2)/2,  y2,textPaint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!allTrue){
        float x=event.getX();
        float y=event.getY();
        for (int i = 0; i <4; i++) {
            if (x>column1Directions[i][0]&&x<column1Directions[i][2]&&y>column1Directions[i][1]&&y<column1Directions[i][3]&&event.getAction()==MotionEvent.ACTION_DOWN&&!isPressOrConfirmedColumn1[1][i]){
                if (firstSelect!=-1){
                    isPressOrConfirmedColumn1[0][firstSelect]=false;
                }
                isPressOrConfirmedColumn1[0][i]=!isPressOrConfirmedColumn1[0][i];
                firstSelect=i;
                invalidate();
            }
        }
        for (int i = 0; i <4; i++) {
            if (x>column2Directions[i][0]&&x<column2Directions[i][2]&&y>column2Directions[i][1]&&y<column2Directions[i][3]&&event.getAction()==MotionEvent.ACTION_DOWN&&!isPressOrConfirmedColumn2[1][i]){
                if (secondSelect!=-1){
                    isPressOrConfirmedColumn2[0][secondSelect]=false;
                }
                isPressOrConfirmedColumn2[0][i]=!isPressOrConfirmedColumn2[0][i];
                secondSelect = i;
                invalidate();
            }
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            if (firstSelect!=-1&&secondSelect!=-1){
                verifyCombination();
            }
            invalidate();
        }}

       return super.onTouchEvent(event);
    }

    private void verifyCombination() {
        if(correctRelation[secondSelect]==firstSelect){
            isPressOrConfirmedColumn1[1][firstSelect]=true;
            isPressOrConfirmedColumn2[1][secondSelect]=true;
        }
        else {
            isPressOrConfirmedColumn2[0][secondSelect]=false;
            isPressOrConfirmedColumn1[0][firstSelect]=false;
        }
        firstSelect=-1;
        secondSelect=-1;
        for (int i=0;i<4;i++){
            if(isPressOrConfirmedColumn1[1][i]==true&&isPressOrConfirmedColumn2[1][i]==true){
                allTrue=true;
            }else{
                allTrue=false;
                break;
            }
        }
        if (allTrue){
            setClickable(false);
            backGroundColor=Color.GREEN;
            onResultCorrect.OnCorrect();
        }
    }
}
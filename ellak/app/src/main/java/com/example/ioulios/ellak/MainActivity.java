package com.example.ioulios.ellak;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity  implements View.OnClickListener{


    ImageView ivEikona ;
    TextView tvaAithmosErwtisis ;
    TextView tvaErwtisi ;

    Button ans[]=new Button[5];
    Button confirm,next;


    ProgressBar pb;
    Timer myTimer;
    TimerTask myTask;
    int ecount=0,count=0;

    int corAns=-1;

    ManageQuestions manQuestions ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivEikona = (ImageView) findViewById(R.id.Eikona) ;
        tvaAithmosErwtisis = (TextView) findViewById(R.id.arithmosErwtisis) ;
        tvaErwtisi = (TextView) findViewById(R.id.Erwtisi) ;

        ans[0] = (Button) findViewById(R.id.b0) ;
        ans[1] = (Button) findViewById(R.id.b1) ;
        ans[2] = (Button) findViewById(R.id.b2) ;
        ans[3] = (Button) findViewById(R.id.b3) ;
        ans[4] = (Button) findViewById(R.id.b4) ;
        confirm = (Button) findViewById(R.id.confirm) ;
        next = (Button) findViewById(R.id.next) ;



        for (int i=0;i<5;i++) {
            ans[i].setOnClickListener(this);
        }

        confirm.setOnClickListener(this);
        next.setOnClickListener(this);


        pb = (ProgressBar) findViewById(R.id.pb);


        /// ManageQuestions
        manQuestions = new ManageQuestions(this) ;
        ///


        viewInfo();




        System.out.println("CurrentType: " + manQuestions.getCurrentType().toString());
        if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EDUCATION) {
            System.out.println("Edu!");
            pb.setVisibility(View.INVISIBLE);
        }
        else {
            System.out.println("Exam!");
            pb.setVisibility(View.VISIBLE);


            myTask = new TimerTask() {
                @Override
                public void run() {
                    count++;
                    ShowMessage(count);
                }
            };
            if (myTimer == null) {
                myTimer = new Timer();
            }
            myTimer.schedule(myTask, 100, 1000);
            myTask = new TimerTask() {
                @Override
                public void run() {
                    if (count >= 100) {

                            Intent nAct = new Intent(MainActivity.this,NextA.class);
                            startActivity(nAct);

                        ShowMessage(-1);
                        myTimer.cancel();
                        myTimer.purge();
                        myTimer = null;

                    }
                }
            };
            myTimer.schedule(myTask, 0, 100);

            viewInfo();
        }
    }

    public void ShowMessage (int Val)
    {
        Message msg = new Message ();
        Bundle bun = new Bundle ();
        bun.putInt ("Value", Val);
        msg.setData(bun);
        MyHandler.sendMessage(msg);
    }


    Handler MyHandler = new Handler ()
    {
        @Override
        public void handleMessage (Message Mess)
        {
            Bundle b = Mess.getData ();
            int tbp = b.getInt("Value");

                pb.setProgress(tbp);
        }
    };

    @Override
    public void onClick(View view) {


        if(view==confirm && corAns>=0) {
            ecount++;
            if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EDUCATION && ecount > 20) {
                Intent nAct = new Intent(MainActivity.this, NextA.class);

                startActivity(nAct);
            }
            manQuestions.setUserResponseToCurQuestion(corAns);
            manQuestions.next() ;
            for(int i=0;i<5;i++)
                ans[i].setBackgroundResource(android.R.drawable.btn_default);
            viewInfo() ;
            corAns=-1;

        }else if(view==next){
            manQuestions.next() ;
            for(int i=0;i<5;i++)
                ans[i].setBackgroundResource(android.R.drawable.btn_default);
            viewInfo() ;
            corAns=-1;

        } else if(view!=confirm) {
            view.setBackgroundColor(Color.YELLOW);
            for(int i=0;i<5;i++)
                if(ans[i]!=view)
                    ans[i].setBackgroundResource(android.R.drawable.btn_default);
                else
                    corAns=i;

        }

    }

    private void viewInfo()
    {

        tvaErwtisi.setText( "" + manQuestions.getCurQuestionText());
        tvaAithmosErwtisis.setText("" + manQuestions.getCurQuestionNumber()) ;
        ivEikona.setImageBitmap(manQuestions.getCurQuestionImage());
        String cans[]=manQuestions.getCurAnswerTexts();
        int ans_no=cans.length;
        for (int i=0;i<5;i++) {
            ans[i].setVisibility(View.VISIBLE);

        }
        for (int i=0;i<ans_no;i++) {
            //ans[i].setVisibility(View.VISIBLE);
            ans[i].setText(cans[i]);
        }
        for (int i=4;i>=ans_no;i--){
            ans[i].setVisibility(View.INVISIBLE);
        }
        int tmpCounter = 0 ;
        for (String answer : manQuestions.getCurAnswerTexts())
            System.out.println("AnswerText " + (tmpCounter++) + " : " + answer);
        tmpCounter = 0 ;
        for (boolean answer : manQuestions.getCurCorrectAnswers())
            System.out.println("CorrectAnswer " + (tmpCounter++) + " : " + answer);

        System.out.println("Score: " + manQuestions.getScore());
    }
}

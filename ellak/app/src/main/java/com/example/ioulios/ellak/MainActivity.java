package com.example.ioulios.ellak;


import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity  implements View.OnClickListener{


    ImageView ivEikona ;
    TextView tvaAithmosErwtisis ;
    TextView tvaErwtisi ;

    Button ans[]=new Button[5];
    ImageButton confirm,next;


    LinearLayout llAnswers ;
    ScrollView svAnswers ;
    Toast toastScore;

    ProgressBar pb;
    Timer myTimer;
    TimerTask myTask;
    int ecount=0,count=0;

    int corAns=-1;

    int secondsTest ;
    int seventyFivePercentSecond ;
    ManageQuestions manQuestions ;
    CountDownTimer cdtNextQuestion ;
    //Bundle resulsB=new ;

    @Override
    public void onBackPressed() {
    }

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivEikona = (ImageView) findViewById(R.id.Eikona) ;
        tvaAithmosErwtisis = (TextView) findViewById(R.id.arithmosErwtisis) ;
        tvaErwtisi = (TextView) findViewById(R.id.Erwtisi) ;
        tvaErwtisi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
/*
        ans[0] = (Button) findViewById(R.id.b0) ;
        ans[1] = (Button) findViewById(R.id.b1) ;
        ans[2] = (Button) findViewById(R.id.b2) ;
        ans[3] = (Button) findViewById(R.id.b3) ;
        ans[4] = (Button) findViewById(R.id.b4) ;
        */
        svAnswers = (ScrollView) findViewById(R.id.scrollView) ;


        confirm = (ImageButton) findViewById(R.id.confirm) ;
        confirm.setImageDrawable(getResources().getDrawable(R.drawable.confirmdisable));
        confirm.setEnabled(false);
        next = (ImageButton) findViewById(R.id.next) ;

        llAnswers = (LinearLayout)findViewById(R.id.linearLayout2);


        for (int i=0;i<5;i++) {
            ans[i] = new Button(this) ;
            ans[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            ans[i].setOnClickListener(this);
        }

        confirm.setOnClickListener(this);
        next.setOnClickListener(this);


        pb = (ProgressBar) findViewById(R.id.pb);

        //
        if (new Random().nextInt(4) == 0)
        {
            secondsTest = 2100 /*35 * 60 */;
            seventyFivePercentSecond = 1575 ;
            ManageQuestions.ALLQUESTIONS = 20 ;
        }
        else
        {
            secondsTest = 600 /* 10 * 60 */ ;
            seventyFivePercentSecond = 450 ;
            ManageQuestions.ALLQUESTIONS = 10 ;
        }


        //
        /// ManageQuestions
        manQuestions = new ManageQuestions(this) ;
        if (type ==1 )
            manQuestions.setCurrentType(ManageQuestions.CustomType.EXAM);
        else
            manQuestions.setCurrentType(ManageQuestions.CustomType.EDUCATION);


        System.out.println("CurrentType: " + manQuestions.getCurrentType().toString());
        Toast.makeText(getApplicationContext(), ManageQuestions.ALLQUESTIONS + " Questions !", Toast.LENGTH_LONG).show() ;
        if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EDUCATION) {
            System.out.println("Edu!");
            pb.setVisibility(View.INVISIBLE);

            toastScore = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
            toastScore.setGravity(Gravity.CENTER, 0, 0);

            cdtNextQuestion = new CountDownTimer(2000, 50) {

                @Override
                public void onTick(long arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {

                    corAns = -1;
                    manQuestions.next() ;
                    viewInfo() ;
                }
            } ;

            //toastScore.setView(tvaAithmosErwtisis);

        }
        else {
            System.out.println("Exam!");

            Toast.makeText(getApplicationContext(), "ProgressBar Minutes : " + (secondsTest/60), Toast.LENGTH_LONG).show() ;

            pb.setVisibility(View.VISIBLE);
            pb.setMax(secondsTest);

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
                    if (count >= secondsTest) {

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


        }
        viewInfo();
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
                if (tbp == seventyFivePercentSecond)
                    pb.setProgressDrawable(getResources().getDrawable(R.drawable.redpb));
        }
    };

    @Override
    public void onClick(View view) {


        if(view==confirm) {
                         /*
                ecount++;
                if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EDUCATION && ecount > 20) {
                    Intent nAct = new Intent(MainActivity.this, NextA.class);

                    startActivity(nAct);
                }
                */
                manQuestions.setUserResponseToCurQuestion(corAns);

                if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EDUCATION)
                {
                   /*
                    for (Button but : ans)
                        but.refreshDrawableState();
*/

                    if (manQuestions.getCurCorrectAnswers()[corAns])
                        ans[corAns].setBackgroundColor(Color.GREEN);
                    else
                    {
                        ans[corAns].setBackgroundColor(Color.RED);
                        for (int i = 0; i<manQuestions.getCurCorrectAnswers().length;i++)
                            if (manQuestions.getCurCorrectAnswers()[i])
                                ans[i].setBackgroundColor(Color.GREEN);
                    }

                    cdtNextQuestion.start();

                    llAnswers.refreshDrawableState();
                    svAnswers.refreshDrawableState();

                    toastScore.setText(manQuestions.getScoreEducation());
                    toastScore.show();
                }
                if (manQuestions.haveFinishedQuestions())
                {
                    Intent nAct = new Intent(MainActivity.this, NextA.class);
                    startActivity(nAct);
                    this.finish();
                }

            confirm.setImageDrawable(getResources().getDrawable(R.drawable.confirmdisable));
            confirm.setEnabled(false);
            if (manQuestions.getCurrentType() == ManageQuestions.CustomType.EXAM)
            {
                corAns = -1;
                manQuestions.next() ;
                viewInfo() ;
            }



        }else if(view==next){
            manQuestions.next() ;
            for(int i=0;i<5;i++)
                ans[i].setBackgroundResource(android.R.drawable.btn_default);
            corAns=-1;
            confirm.setImageDrawable(getResources().getDrawable(R.drawable.confirmdisable));
            confirm.setEnabled(false);
            viewInfo() ;


        } else if(view!=confirm) {
            view.setBackgroundColor(Color.YELLOW);
            for(int i=0;i<5;i++)
                if(ans[i]!=view)
                    ans[i].setBackgroundResource(android.R.drawable.btn_default);
                else
                    corAns=i;
            confirm.setImageDrawable(getResources().getDrawable(R.drawable.confirm));
            confirm.setEnabled(true);
        }

    }

    private void viewInfo()
    {
        tvaErwtisi.setText("" + manQuestions.getCurQuestionText());
        tvaAithmosErwtisis.setText("" + manQuestions.getCurQuestionNumber() + "/" + ManageQuestions.ALLQUESTIONS ) ;
        ivEikona.setImageBitmap(manQuestions.getCurQuestionImage());
        String cans[]=manQuestions.getCurAnswerTexts();
        int ans_no=cans.length;

        /*
        for (int i=0;i<5;i++) {
            ans[i].setVisibility(View.VISIBLE);
        }
        */
        llAnswers.removeAllViews();
        for (int i=0;i<ans_no;i++) {
            //ans[i].setVisibility(View.VISIBLE);
            ans[i].setText(cans[i]);
            llAnswers.addView(ans[i]);
        }
        /*for (int i=4;i>=ans_no;i--){
            ans[i].setVisibility(View.INVISIBLE);
        }*/

        int tmpCounter = 0 ;
        for (String answer : manQuestions.getCurAnswerTexts())
            System.out.println("AnswerText " + (tmpCounter++) + " : " + answer);
        tmpCounter = 0 ;
        for (boolean answer : manQuestions.getCurCorrectAnswers())
            System.out.println("CorrectAnswer " + (tmpCounter++) + " : " + answer);

        System.out.println("Score: " + manQuestions.getScore());
        for(int i=0;i<5;i++)
            ans[i].setBackgroundResource(android.R.drawable.btn_default);
        svAnswers.scrollTo(0,0);
    }
}

package com.example.ioulios.ellak;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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



public class MainActivity extends ActionBarActivity implements View.OnClickListener
{
    LinearLayout llAnswers ;
    ScrollView svAnswers ;

    ImageView ivImage ;
    TextView tvaNumOfQuestion ;
    TextView tvaQuestion ;
    ImageButton ibConfirm,ibNext;
    ProgressBar pb;

    Button bAns[] ;

    Timer myTimer;
    TimerTask myTask;
    int count=0;

    int corAns=-1 ; //-1 σημαίνει ότι δεν έχει απαντήσει ο χρήστης!
    int sumOfButtons ;

    ManageQuestions manQuestions ;
    CountDownTimer cdtNextQuestion ;
    boolean onClickIsNotBusy = true ;
    Toast toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int testType ,subject ;
        Bundle bu = getIntent().getExtras();

        if (bu != null)
        {
           testType = bu.getInt("TestType") ;
           subject = bu.getInt("Subject") ;
        }
        else
        {
            testType = new Random().nextInt(2) ;
            subject = 0 ;
        }

        manQuestions = new ManageQuestions(this,subject,testType) ;

        ivImage = (ImageView) findViewById(R.id.Eikona) ;
        tvaNumOfQuestion = (TextView) findViewById(R.id.arithmosErwtisis) ;
        tvaQuestion = (TextView) findViewById(R.id.Erwtisi) ;
        tvaQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        svAnswers = (ScrollView) findViewById(R.id.scrollView) ;
        ibConfirm = (ImageButton) findViewById(R.id.confirm) ;
        pb = (ProgressBar) findViewById(R.id.pb);
        ibNext = (ImageButton) findViewById(R.id.next) ;
        llAnswers = (LinearLayout)findViewById(R.id.linearLayout2);

        sumOfButtons = manQuestions.getMaxAnswers() ;
        bAns = new Button[sumOfButtons] ;
        for (int i=0;i<sumOfButtons;i++) {
            bAns[i] = new Button(this) ;
            bAns[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            bAns[i].setOnClickListener(this);
        }

        ibConfirm.setOnClickListener(this);
        ibNext.setOnClickListener(this);

        disableConfirm();

        prepareToastMessage() ;

        showToastMessage(manQuestions.getAllTotalQuestions() + " Questions !");
        if (manQuestions.getCurrentType() == ManageQuestions.TestType.EDUCATION) {
            pb.setVisibility(View.INVISIBLE);
            prepareCountDownTimer() ;
        }
        else
        {
            pb.setVisibility(View.VISIBLE);
            prepareProgressBar() ;

            showToastMessage("Exam time : " + (manQuestions.getExamTime()));
        }
        viewInfo();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view)
    {
        if (onClickIsNotBusy)
        {
            onClickIsNotBusy = false ;
            if(view==ibConfirm) {
                manQuestions.setUserResponseToCurQuestion(corAns);

                if (manQuestions.getCurrentType() == ManageQuestions.TestType.EDUCATION)
                {
                    if (manQuestions.getCurCorrectAnswer() == corAns)
                        bAns[corAns].setBackgroundColor(Color.GREEN);
                    else
                    {
                        bAns[corAns].setBackgroundColor(Color.RED);
                        bAns[manQuestions.getCurCorrectAnswer()].setBackgroundColor(Color.GREEN);
                    }

                    showToastMessage(manQuestions.getScoreEducation()) ;

                    cdtNextQuestion.start();
                }
                else
                {
                    if (manQuestions.haveFinishedQuestions())
                        completelyFininsh() ;
                    else
                        nextQuestion() ;
                }
            }
            else if(view==ibNext)
            {
                nextQuestion() ;
            }
            else
            {
                view.setBackgroundColor(Color.YELLOW);
                for(int i=0;i<sumOfButtons;i++)
                    if(bAns[i]!=view)
                        bAns[i].setBackgroundResource(android.R.drawable.btn_default);
                    else
                        corAns=i;
                enableConfirm() ;
                onClickIsNotBusy = true ;
            }
        }
    }

    private void viewInfo()
    {
        tvaQuestion.setText("" + manQuestions.getCurQuestionText());
        tvaNumOfQuestion.setText("" + manQuestions.getCurQuestionNumber() + "/" + manQuestions.getAllTotalQuestions()) ;
        ivImage.setImageBitmap(manQuestions.getCurQuestionImage());
        String cans[]=manQuestions.getCurAnswerTexts();

        llAnswers.removeAllViews();
        for (int i=0;i<cans.length;i++) {
            bAns[i].setText(cans[i]);
            llAnswers.addView(bAns[i]);
        }

        for(int i=0;i<sumOfButtons;i++)
            bAns[i].setBackgroundResource(android.R.drawable.btn_default);
        svAnswers.scrollTo(0, 0);
    }

    //Για πιο ολοκληρωμένο κλείσιμο του activity (Απελευθέρωση μνήμης)
    private void completelyFininsh()
    {
        onClickIsNotBusy = false ; /*Σε περίπτωση που τερματίσει το activity να μην μπορεί να πατηθεί
                                     κάποιο κουμπί και κατά επέκταση να χρησιμοποιηθεί η manQuestions
                                     (Διότι γίνεται null ...) (Νομίζω είναι απίθανο αλλά για καλό και για κακό..)*/

        Intent nAct = new Intent(MainActivity.this, NextA.class);
        nAct.putExtras(manQuestions.getBundleOfResults()) ;
        startActivity(nAct);

        toastMessage = null ;
        if (myTimer != null)
        {
            myTimer.cancel();
            myTimer.purge();
            myTimer = null;
        }
        if (cdtNextQuestion != null)
        {
            cdtNextQuestion.cancel(); ;
            cdtNextQuestion = null ;
        }
        manQuestions = null ;
        this.finish();
    }

    //Prepare :
    private void prepareToastMessage()
    {
        toastMessage = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
        toastMessage.setGravity(Gravity.CENTER, 0, 0);
    }

    private void prepareProgressBar()
    {
        pb.setMax(manQuestions.getSecondsForTest());
        myTask = new TimerTask() {
            @Override
            public void run() {
                if (count++ > manQuestions.getSecondsForTest()) count = manQuestions.getSecondsForTest() ;
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
                if (count >= manQuestions.getSecondsForTest()) {
                    ShowMessage(manQuestions.getSecondsForTest());
                    completelyFininsh();
                }
            }
        };
        myTimer.schedule(myTask, 0, 100);
    }
    public void ShowMessage (int Val)
    {
        Message msg = new Message ();
        Bundle bun = new Bundle ();
        bun.putInt("Value", Val);
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
            if (tbp == manQuestions.getSeventyFivePercentOfsecondsForTest())
                pb.setProgressDrawable(getResources().getDrawable(R.drawable.redpb));
        }
    };

    private void prepareCountDownTimer()
    {
        cdtNextQuestion = new CountDownTimer(2000, 50) {

            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                if (manQuestions.haveFinishedQuestions())
                    completelyFininsh() ;
                else
                    nextQuestion() ;

            }
        } ;
    }
    //Prepare : END

    //Tools :
    private void nextQuestion()
    {
        disableConfirm();
        corAns=-1;
        if (manQuestions.next())
            viewInfo() ;
        onClickIsNotBusy = true ;
    }

    private void showToastMessage(String message)
    {
        toastMessage.setText(message);
        toastMessage.show();
    }

    private void disableConfirm()
    {
        ibConfirm.setImageDrawable(getResources().getDrawable(R.drawable.confirmdisable));
        ibConfirm.setEnabled(false);
    }
    private void enableConfirm()
    {
        ibConfirm.setImageDrawable(getResources().getDrawable(R.drawable.confirm));
        ibConfirm.setEnabled(true);
    }
    //Tools : END
}


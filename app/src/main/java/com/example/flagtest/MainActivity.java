package com.example.flagtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    class downloadtask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url;
            URLConnection url_connection;
            try {
                url = new URL(strings[0]);
                url_connection = (URLConnection) url.openConnection();
                url_connection.connect();
                InputStream inputStream = url_connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    Button start,button0,button1,button2,button3;
    TextView score, final_result;
    ArrayList<String> url,answers,wrong_answers,options;
    ImageView flag;
    LinearLayout linear, linear1,linear2;
    int questionnumber,correctanswers,answeredquestions,correct_answer_location;
    Random rn;
    Bitmap img_flag;
    downloadtask downloadtask;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url=new ArrayList<String>();
        answers=new ArrayList<String>();
        wrong_answers=new ArrayList<String>();
        options= new ArrayList<String>();
        url.addAll(Arrays.asList("https://www.worldometers.info/img/flags/small/tn_as-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_ba-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_be-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_ca-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_ch-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_da-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_pk-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_gm-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_fr-flag.gif",
                "https://www.worldometers.info/img/flags/small/tn_ir-flag.gif"));
        answers.addAll(Arrays.asList("Australia","Bahrain","Belgium","Canada","China","Denmark","Pakistan","Germany","France","Iran"));
        wrong_answers.addAll(Arrays.asList("Italy","Japan","Kenya","Malaysia","Maldives","Nepal","Norway","Oman","Qatar","Russia","Siberia"
                ,"Spain","Sweden","Turkey","UAE"));
        init();
        score.setVisibility(View.INVISIBLE);
        linear.setVisibility(View.INVISIBLE);
        linear1.setVisibility(View.INVISIBLE);

        questionnumber = 0;
        rn=new Random();
        downloadtask=new downloadtask();
        img_flag=null;
        answeredquestions=0;
        correctanswers=0;
        handler= new Handler();
    }

    private void init() {
        score = findViewById(R.id.score);
        final_result = findViewById(R.id.final_result);
        flag = findViewById(R.id.flag);
        start=findViewById(R.id.start);
        linear = findViewById(R.id.linear);
        linear1 = findViewById(R.id.linear1);
        linear2=findViewById(R.id.linear2);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
    }

    public void start(View view) {
        start.setVisibility(View.INVISIBLE);
        score.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.INVISIBLE);
        generate_questions();
    }

    public void play_again(View view) {
        questionnumber=0;
        answeredquestions=0;
        correctanswers=0;
        linear1.setVisibility(View.INVISIBLE);
        linear2.setVisibility(View.INVISIBLE);
        generate_questions();
    }

    private void generate_questions() {
        if(questionnumber>=10)
        {
            //show final result
            linear.setVisibility(View.INVISIBLE);
            linear1.setVisibility(View.VISIBLE);
            score.setVisibility(View.INVISIBLE);
            final_result.setText("Your Final score is "+String.valueOf(correctanswers)+"/"+String.valueOf(answeredquestions));
        }
        else {
            downloadtask=new downloadtask();
            try {
                img_flag = downloadtask.execute(url.get(questionnumber)).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            flag.setImageBitmap(img_flag);
            correct_answer_location = rn.nextInt(4);
            int x = rn.nextInt(11);
            for (int i = 0; i < 4; i++) {
                if (i == correct_answer_location) {
                    options.add(answers.get(questionnumber));
                }
                else
                {
                    options.add(wrong_answers.get(x));
                    x++;
                }
            }
            button0.setText(options.get(0));
            button1.setText(options.get(1));
            button2.setText(options.get(2));
            button3.setText(options.get(3));
            linear.setVisibility(View.VISIBLE);
            //shuffel the wrong answer array
            Collections.shuffle(wrong_answers);
            options.clear();
            questionnumber++;
            downloadtask=null;
            img_flag=null;
        }
    }

    public void check_answer(View view) {
        if(Integer.toString(correct_answer_location).equals(view.getTag().toString()))
        {
            correctanswers++;
            answeredquestions++;
            score.setText(String.valueOf(correctanswers)+"/"+String.valueOf(answeredquestions));
        }
        else
        {
            answeredquestions++;
            score.setText(String.valueOf(correctanswers)+"/"+String.valueOf(answeredquestions));
        }
        generate_questions();
    }

}
package com.example.cravisundaram.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    long st = 10000;
    long i = 1000;
    long l=10;
    TextView t, t1;
    Thread thread;
    HttpClient httpclient;
    HttpPost httppost;
    CountDownTimer c,c1;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.textView);
        t1 = (TextView) findViewById(R.id.textView2);
        b=(Button)findViewById(R.id.button);
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost("http://spider.nitt.edu/~vishnu/time.php");



        c = new Mycount(st, i);
        c.start();
        t.setText("0");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
    }

    public class Mycount extends CountDownTimer {
        public Mycount(long st, long i) {
            super(st, i);
        }

        public void onFinish() {
            t.setText("" + st / 1000);
            thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String response = httpclient.execute(httppost, responseHandler);
                        l=Character.getNumericValue(response.charAt(response.length()-1));
                        st=l*1000;
                        setText(t1,response);
                        setTime(c1,st);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        public void onTick(long millisUntilFinished) {
            long a = (l*1000-millisUntilFinished) / 1000;
            t.setText(String.valueOf(a));
        }

    }
    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setTime(final CountDownTimer c,final long value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                c1= new Mycount(value,i);
                c1.start();

            }
        });
    }
}
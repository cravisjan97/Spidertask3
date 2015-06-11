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

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


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
            new extract().execute();
        }

        public void onTick(long millisUntilFinished) {
            long a = (l*1000-millisUntilFinished) / 1000;
            t.setText(String.valueOf(a));
        }

    }
    public class extract extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params) {
            try {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);
                l = Character.getNumericValue(response.charAt(response.length() - 1));
                st = l * 1000;
                return response;
            }catch (Exception e)
            {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            t1.setText(s);

            c1= new Mycount(st,i);
            c1.start();

        }
    }

}

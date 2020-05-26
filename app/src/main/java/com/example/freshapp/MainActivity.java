package com.example.freshapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    public static final int LOAD_SUCCESS = 101;

    private String SEARCH_URL = "http://175.208.85.188:8421/ARgetRecentData/";
    private String REQUEST_URL = SEARCH_URL;

    private ProgressDialog progressDialog;
    private TextView lux, temp, hum, g_hum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lux = findViewById(R.id.main_lux);
        temp = findViewById(R.id.main_temp);
        hum = findViewById(R.id.main_hum);
        g_hum = findViewById(R.id.main_g_hum);
        Spinner spinner = findViewById(R.id.MainSpin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    REQUEST_URL = SEARCH_URL;
                    REQUEST_URL = REQUEST_URL + Integer.toString(position);

                    if(position != 0) {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Please wait.....");
                        progressDialog.show();

                        getJSON();
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {

            MainActivity mainActivity = weakReference.get();

            if (mainActivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainActivity.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        int index1_1 = jsonString.indexOf(":");
                        int index1_2 = jsonString.indexOf(":", index1_1+1);
                        int index1_3 = jsonString.indexOf(":", index1_2+1);
                        int index1_4 = jsonString.indexOf(":", index1_3+1);
                        int index2_1 = jsonString.indexOf(",");
                        int index2_2 = jsonString.indexOf(",", index2_1+1);
                        int index2_3 = jsonString.indexOf(",", index2_2+1);
                        int index2_4 = jsonString.indexOf("}");
                        String lux = jsonString.substring(index1_1+1, index2_1);
                        String temp = jsonString.substring(index1_2+1, index2_2);
                        String hum = jsonString.substring(index1_3+1, index2_3);
                        String g_hum = jsonString.substring(index1_4+1, index2_4);
                        int num_lux = Integer.parseInt(lux);
                        String s_lux = "";

                        if(num_lux == 0) {
                            s_lux = "꺼짐";
                        }
                        else if(num_lux == 1) {
                            s_lux = "어두움";
                        }
                        else if(num_lux == 2) {
                            s_lux = "밝음";
                        }

                        mainActivity.lux.setText("조도 : " + s_lux);
                        mainActivity.temp.setText("온도 : " + temp);
                        mainActivity.hum.setText("습도 : " + hum);
                        mainActivity.g_hum.setText("토양 습도 : " + g_hum);
                        break;
                }
            }
        }
    }

    public void  getJSON() {

        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;

                try {
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoOutput(false);

                    StringBuilder sb = new StringBuilder();
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        System.out.println("" + sb.toString());
                    } else {
                        System.out.println(httpURLConnection.getResponseMessage());
                    }

                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }


                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                mHandler.sendMessage(message);
            }

        });
        thread.start();
    }

    public void onGetButtonClick(View v) {
        Intent GetIntent = new Intent(getApplicationContext(), Getdata.class);
        startActivity(GetIntent);
    }

    public void onPussButtonClick(View v) {
        Intent PostIntent = new Intent(getApplicationContext(), Postdata.class);
        startActivity(PostIntent);
    }
}

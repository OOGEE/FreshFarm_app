package com.example.freshapp;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class Getdata extends AppCompatActivity {

    public static final int LOAD_SUCCESS = 101;

    private String SEARCH_URL = "http://175.208.85.188:8421/ANgetRecentData/";
    private String REQUEST_URL = SEARCH_URL;

    private ProgressDialog progressDialog;
    private TextView temp_text, hum_text, g_hum_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);

        Button buttonRequestJSON = (Button)findViewById(R.id.button_main_requestjson);
        temp_text = findViewById(R.id.temp_text);
        hum_text = findViewById(R.id.hum_text);
        g_hum_text = findViewById(R.id.g_hum_text);
        Spinner spinner = findViewById(R.id.GetSpin);

        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog( Getdata.this );
                progressDialog.setMessage("Please wait.....");
                progressDialog.show();

                getJSON();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                REQUEST_URL = SEARCH_URL;
                REQUEST_URL = REQUEST_URL + Integer.toString(position+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<Getdata> weakReference;

        public MyHandler(Getdata getdata) {
            weakReference = new WeakReference<Getdata>(getdata);
        }

        @Override
        public void handleMessage(Message msg) {

            Getdata getdata = weakReference.get();

            if (getdata != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        getdata.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        int index1_1 = jsonString.indexOf(":");
                        int index1_2 = jsonString.indexOf(":", index1_1+1);
                        int index1_3 = jsonString.indexOf(":", index1_2+1);
                        int index2_1 = jsonString.indexOf(",");
                        int index2_2 = jsonString.indexOf(",", index2_1+1);
                        int index2_3 = jsonString.indexOf("}");
                        String temp = jsonString.substring(index1_1+1, index2_1);
                        String hum = jsonString.substring(index1_2+1, index2_2);
                        String g_hum = jsonString.substring(index1_3+1, index2_3);

                        getdata.temp_text.setText("온도 : " + temp);
                        getdata.hum_text.setText("습도 : " + hum);
                        getdata.g_hum_text.setText("토양 습도 : " + g_hum);
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

                    Log.d("hh", REQUEST_URL);
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

}
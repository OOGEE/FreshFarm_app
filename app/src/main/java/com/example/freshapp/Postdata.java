package com.example.freshapp;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Postdata extends AppCompatActivity {

    ArrayAdapter<CharSequence> adspin1, adspin2;
    private EditText et_temperature, et_humidity, et_ground_humidty;
    int machine_num = 0;
    int setillu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdata);

        et_temperature = findViewById( R.id.editTemp);
        et_humidity = findViewById( R.id.editHumi );
        et_ground_humidty = findViewById( R.id.editGround );
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.설정, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

                if(adspin1.getItem(i).equals("상추")) {
                    setillu = 2;
                    et_temperature.setText("18");
                    et_humidity.setText("80");
                    et_ground_humidty.setText("80");


                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_상추, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            if(position == 0) {
                                setillu = 2;

                            }
                            else if(position == 1) {
                                setillu = 1;
                            }
                            else if(position == 2){
                                setillu = 0;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

                else if(adspin1.getItem(i).equals("고사리")) {
                    setillu = 1;
                    et_temperature.setText("20");
                    et_humidity.setText("85");
                    et_ground_humidty.setText("85");
                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_고사리, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            if(position == 0) {
                                setillu = 1;
                            }
                            else if(position == 1) {
                                setillu = 0;
                            }
                            else if(position == 2){
                                setillu = 2;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

                else if(adspin1.getItem(i).equals("사용자설정")) {
                    et_temperature.setText("");
                    et_humidity.setText("");
                    et_ground_humidty.setText("");
                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_사용자, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            if(position == 0) {
                            }
                            else if(position == 1) {
                                setillu = 0;
                            }
                            else if(position == 2){
                                setillu = 1;
                            }
                            else if(position == 3){
                                setillu = 2;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {

                    machine_num = 0;
                }
                else if(position == 1) {

                    machine_num = 1;
                }
                else if(position == 2){

                    machine_num = 2;
                }
                else if(position == 3){

                    machine_num = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button postButton = (Button) findViewById(R.id.button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

    }

    public void post () {

        final String temp = et_temperature.getText().toString();
        final String humi = et_humidity.getText().toString();
        final String g_humi = et_ground_humidty.getText().toString();
        final String machine = Integer.toString(machine_num);
        final String illu = Integer.toString(setillu);

        final String body1 = "illuminatation="+illu+"&"+"temperature="+temp+"&" + "humidity="+humi+"&" + "g_humidity="+g_humi+ "&" + "machine_num=" +machine;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

               String body = body1;

                try {

                    URL u = new URL("http://175.208.85.188:8421/ANuploadData/");

                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();

                    huc.setRequestMethod("POST");

                    huc.setDoInput(true);

                    huc.setDoOutput(true);

                    huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream os = huc.getOutputStream();

                    os.write(body.getBytes("UTF-8"));

                    os.flush();

                    os.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"), huc.getContentLength());

                    String buf;

                    while ((buf = br.readLine()) != null) {

                        System.out.println(buf);

                    }

                    br.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
        thread.start();
        Toast.makeText(getApplicationContext(), "upload complete", Toast.LENGTH_SHORT).show();
    }

}

package com.example.freshapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.freshapp.R;

import com.example.freshapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Postdata extends AppCompatActivity {

    ArrayAdapter<CharSequence> adspin1, adspin2;
    private Button postButton;
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
        //  et_machine_num = findViewById( R.id.editMachine );
        //textView = findViewById(R.id.textView);
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);


        adspin1 = ArrayAdapter.createFromResource(this, R.array.설정, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

                if(adspin1.getItem(i).equals("양지식물")) {
                    //et_machine_num.setText("2");
                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_양지, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            //et_machine_num.setText(""+4); //두번쨰 선택된 값
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
                else if(adspin1.getItem(i).equals("음지식물")) {
                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_음지, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            //et_machine_num.setText(""+4); // 두번쨰꺼 고르면
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
                    adspin2 = ArrayAdapter.createFromResource(Postdata.this, R.array.spinner_do_사용자, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            //et_machine_num.setText(""+4); // 두번쨰꺼 고르면
                            if(position == 0) {
                            }
                            else if(position == 1) {
                                setillu = 0;
                            }
                            else if(position == 2){
                                setillu = 0;
                            }
                            else if(position == 3){
                                setillu = 1;
                            }
                            else if(position == 4){
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

               /* String temp = et_temperature.getText().toString();
                String humidity = et_humidity.getText().toString();
                String g_humidity = et_ground_humidty.getText().toString();
                String machine = et_machine_num.getText().toString();
                */

                // 요청할 파라미터의 정보를 입력한다.

                //   String body = "temperature=55&humidity=33&g_humidity=100&machine_num=2"; //온도 습도 땅습도, 머신넘버, 조도
                //    String body = "temperature="+et_temperature.getText()+"&" + "humidity="+ et_humidity.getText()+"&" +"g_humidity=" + et_ground_humidty.getText()+"&"  +"machine_num="+ et_machine_num.getText();
                String body = body1;
                Log.d("abc", body);
                Log.d("abc1", body1);

// URL클래스의 생성자로 주소를 넘겨준다.
                try {

                    URL u = new URL("http://175.208.85.188:8421/ANuploadData/");


// 해당 주소의 페이지로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.

                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();

// POST방식으로 요청한다.( 기본값은 GET )

                    huc.setRequestMethod("POST");

// InputStream으로 서버로 부터 응답 헤더와 메시지를 읽어들이겠다는 옵션을 정의한다.

                    huc.setDoInput(true);

// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션을 정의한다.

                    huc.setDoOutput(true);

// 요청 헤더를 정의한다.( 원래 Content-Length값을 넘겨주어야하는데 넘겨주지 않아도 되는것이 이상하다. )

                    huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// 새로운 OutputStream에 요청할 OutputStream을 넣는다.

                    OutputStream os = huc.getOutputStream();

// 그리고 write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "EUC-KR"로 인코딩해서 요청한다.

// 여기서 중요한 점은 "UTF-8"로 해도 되는데 한글일 경우는 "EUC-KR"로 인코딩해야만 한글이 제대로 전달된다.

                    os.write(body.getBytes("UTF-8"));

// 그리고 스트림의 버퍼를 비워준다.

                    os.flush();

// 스트림을 닫는다.

                    os.close();

// 응답받은 메시지의 길이만큼 버퍼를 생성하여 읽어들이고, "EUC-KR"로 디코딩해서 읽어들인다.

                    BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"), huc.getContentLength());

                    String buf;

// 표준출력으로 한 라인씩 출력

                    while ((buf = br.readLine()) != null) {

                        System.out.println(buf);

                    }

// 스트림을 닫는다.

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

package com.harsha.dell.friendfinder;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupAct extends AppCompatActivity {

    EditText et1,et2,et3,et4,et5;
    LocationManager locationManager;
    Location location;
    String dataToWrite,msgtext;
    public double longitude,lattitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(SignupAct.this,AboutAct.class);
                startActivity(i);
            }
        });

        et1=(EditText)findViewById(R.id.rname);
        et2=(EditText)findViewById(R.id.rusername);
        et3=(EditText)findViewById(R.id.rpassword);
        et4=(EditText)findViewById(R.id.remail);
        et5=(EditText)findViewById(R.id.rcontact);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    }
    public void register(View view){
        String fn=et1.getText().toString();
        String un=et2.getText().toString();
        String pwd=et3.getText().toString();
        String ema=et4.getText().toString();
        String phn=et5.getText().toString();

        if(fn.length()==0&&un.length()==0&&pwd.length()==0&&ema.length()==0&&phn.length()==0){
            et1.setError("Enter your Name!!");
            et2.setError("Enter your Username!!");
            et3.setError("Enter your Password!!");
            et4.setError("Enter your Email!!");
            et5.setError("Enter your Phone!!");
            return;
        }
        if(fn.length()==0){
            et1.setError("Enter your Name!!");
            return;
        }
        if(un.length()==0){
            et2.setError("Enter your Username!!");
            return;
        }
        if(pwd.length()==0){
            et3.setError("Enter your Password!!");
            return;
        }
        if(ema.length()==0){
            et4.setError("Enter your Email!!");
            return;
        }
        if(phn.length()==0){
            et5.setError("Enter your Contact Number!!");
            return;
        }
        String data="",text="";

        boolean flag=true;
        try {
            data="user="+un;
            /**---------------------start---------*/
            URL url = new URL("http://192.168.43.228/checkusername.php");
            // Send POST data request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            // Get the server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString().trim();

            if(text.equals("success")) {
                flag=false;
                et2.setError("Username already exists");
            }
            else {
                flag=true;

            }

        }catch(Exception e){
            final String msg=e.getMessage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            });

        }

        if(flag==true) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), "No Network or GPS enabled", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        lattitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Toast.makeText(getApplicationContext(), String.valueOf(lattitude) + String.valueOf(longitude), Toast.LENGTH_LONG).show();

                    }
                } catch (SecurityException se) {
                    Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_SHORT);

                }
            }
            dataToWrite = "n=" + fn + "&u=" + un + "&p=" + pwd + "&e=" + ema + "&c=" + phn + "&la=" + lattitude + "&lo=" + longitude;
            try {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.43.228/friendfinder.php");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            DataOutputStream dstream = new DataOutputStream(connection.getOutputStream());
                            dstream.writeBytes(dataToWrite);
                            dstream.flush();
                            dstream.close();
                            msgtext = String.valueOf(connection.getResponseCode());
                        } catch (Exception e) {

                            final String d = e.getMessage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), d, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                };
                t.start();
            } catch (Exception e) {

                final String d = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), d, Toast.LENGTH_LONG).show();
                    }
                });
            }
            Intent i = new Intent(SignupAct.this, MainActivity.class);
            startActivity(i);


        }
    }



}

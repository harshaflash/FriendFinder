package com.harsha.dell.friendfinder;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Location location;
    ArrayAdapter adapter;
    String dataToWrite,text,msgtext,list[];
    LocationManager locationmanager;
    double longitude,latitude;
    double lat,lon;
    Spinner sp1;

    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(LoginActivity.this, AboutAct.class);
                startActivity(i);

            }
        });

        sp1 = (Spinner) findViewById(R.id.spinner);
        new PostData().execute();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
            }


        };
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    class PostData extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String s) {
            sp1.setAdapter(adapter);
            super.onPostExecute(s);

        }

        protected String doInBackground(String... strings) {
            try {
                //data = "user=" + u + "&pass=" + p;
                /**---------------------start---------*/
                URL url = new URL("http://192.168.43.228/getlist.php");
                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                //wr.writeBytes(data);
                wr.flush();
                // Get the server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line );
                }
                text = sb.toString().trim();
                int j=0,x=0;
                for(int i=0;i<text.length();i++){
                    if(text.charAt(i)=='@')
                        count++;
                }
                list=new String[count];
                for(int k=0;k<text.length();k++){

                    if(text.charAt(k)=='@')
                    {
                        list[x++]=text.substring(j,k);
                        j=k+1;
                    }
                }
                // msg1=list[0];
                adapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_activated_1,list);
                //sp1.setAdapter(adapter);

            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return null;
        }
    }
    String u;
    class PostData1 extends AsyncTask<String,String,String> {

        protected String doInBackground(String... strings) {
            try {
                String data = "user=" + u ;
                /**---------------------start---------*/
                URL url = new URL("http://192.168.43.228/getlocation.php");
                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
conn.setDoInput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                // Get the server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String s=null;
                sb.append(s);
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line );
                }

                Log.d("******Serious*****",sb.toString());

                text = sb.toString().trim();
                String str1[]=new String[2];
                int k=0,j=0;
                for(int i=0;i<text.length();i++){
                    if(text.charAt(i)==','){
                        str1[j++]=text.substring(k,i);
                        k=i+1;
                    }
                }

                str1[0]=str1[0].substring(4,str1[0].length());
                lat=Double.parseDouble(str1[0]);
                lon=Double.parseDouble(str1[1]);
                Intent i = new Intent(LoginActivity.this,MapsActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("long",lon);
                startActivity(i);

            } catch (Exception e) {
                final String msg = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }
    }

    public void getlocation(View view){
        String name= sp1.getSelectedItem().toString();


        for(int x=0;x<name.length();x++){
            if(name.charAt(x)==',')
                u=name.substring(x+1,name.length());
        }
        Toast.makeText(getApplicationContext(),u,Toast.LENGTH_LONG).show();
        new PostData1().execute();


    }

    public void updatelocation(View view){
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), "No Network or GPS Enabled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
            } catch (SecurityException se) {
                Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        dataToWrite = "n="+getIntent().getExtras().getString("name")+"&la="+latitude+"&lo="+longitude;
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://192.168.43.228/update.php");
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
                                Toast.makeText(getApplicationContext(),d,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            };

            t.start();
            //Toast.makeText(getApplicationContext(),msg1+count,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Location Updated",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            final String d = e.getMessage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), d, Toast.LENGTH_LONG).show();
                }
            });
        }

    }


}

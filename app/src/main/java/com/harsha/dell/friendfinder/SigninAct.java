package com.harsha.dell.friendfinder;

import android.content.Intent;
import android.os.AsyncTask;
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

public class SigninAct extends AppCompatActivity {


    EditText e1,e2;
    String u,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(SigninAct.this,AboutAct.class);
                startActivity(i);
            }
        });
        e1=(EditText)findViewById(R.id.susername);
        e2=(EditText)findViewById(R.id.spassword);
    }
    String data="",text="";
    public void login(View v) {
        u = e1.getText().toString();
        p = e2.getText().toString();
        new PostData().execute();
    }
    class PostData extends AsyncTask<String,String,String> {

        protected String doInBackground(String... strings) {
            try {
                data = "user=" + u + "&pass=" + p;
                /**---------------------start---------*/
                URL url = new URL("http://192.168.43.228/login.php");
                // Send POST data request
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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


                if (text.equals("success")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Welcome ! Login Succesfull", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(SigninAct.this, LoginActivity.class);

                            i.putExtra("name", u);
                            startActivity(i);

                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "Oops! Invalid Details ", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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


}

package com.harsha.dell.friendfinder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setAlpha(0.5f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this,AboutAct.class);
                startActivity(i);
            }
        });
        //Typeface myTypeface = Typeface.createFromAsset(getAssets(),"font.ttf");
        TextView myTextview = (TextView)findViewById(R.id.txtview1);
        //myTextview.setTypeface(myTypeface);
    }

    public void signup(View v)
    {
        Intent i = new Intent(MainActivity.this,SignupAct.class);
        startActivity(i);
    }

    public void signin(View v)
    {
        Intent i = new Intent(MainActivity.this,SigninAct.class);
        startActivity(i);
    }

 public void About(View view){
     Intent i = new Intent(MainActivity.this,AboutAct.class);
     startActivity(i);
 }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

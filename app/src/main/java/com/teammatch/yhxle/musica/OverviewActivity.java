package com.teammatch.yhxle.musica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button learn = (Button) findViewById(R.id.learn);
        learn.setOnClickListener(learnHandler);
        Button quiz = (Button) findViewById(R.id.quiz);
        quiz.setOnClickListener(quizHandler);
        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(playHandler);
    }

    View.OnClickListener learnHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (getApplicationContext(), LearnActivity.class);
            startActivity (intent);
        }
    };

    View.OnClickListener quizHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (getApplicationContext(), QuizActivity.class);
            intent.putExtra ("theme", 0);
            intent.putExtra ("practice", false);
            startActivity (intent);
        }
    };

    View.OnClickListener playHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (getApplicationContext(), PlayActivity.class);
            startActivity (intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_overview) {
            Intent intent = new Intent (this, OverviewActivity.class);
            startActivity (intent);
        } else if (id == R.id.action_learn) {
            Intent intent = new Intent (this, LearnActivity.class);
            startActivity (intent);
        } else if (id == R.id.action_play) {
            Intent intent = new Intent (this, PlayActivity.class);
            startActivity (intent);
        } /*else if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(),"Logout", Toast.LENGTH_SHORT).show();
        }*/

        return super.onOptionsItemSelected(item);
    }
}

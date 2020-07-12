package au.net.australiastudy.pilottool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private final String firstTime = "FIRST_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (!prefs.getBoolean(firstTime,true))
            start();
        else {
            Button btn = findViewById(R.id.startBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(firstTime, false);
                    editor.apply();
                    start();
                }
            });
        }
    }

    private void start(){
        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
        startActivity(intent);
        finish();
    }
}
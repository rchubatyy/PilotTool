package au.net.australiastudy.pilottool;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class ResultsActivity extends AppCompatActivity {

Button[] buttons = new Button[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        buttons[0] = findViewById(R.id.button0);
        buttons[1] = findViewById(R.id.button1);
        buttons[2] = findViewById(R.id.button2);
        buttons[3] = findViewById(R.id.button3);
        buttons[4] = findViewById(R.id.button4);
        buttons[5] = findViewById(R.id.button5);
        Intent intent = getIntent();
        int[] results = intent.getIntArrayExtra("order");
        for (int i=0; i<results.length; i++)
            buttons[i].setText(("CATEGORY #" + (results[i]+1)));
    }
}

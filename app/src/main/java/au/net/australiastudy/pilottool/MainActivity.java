package au.net.australiastudy.pilottool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import au.net.australiastudy.pilottool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private SharedPreferences prefs;
    private ActivityMainBinding bi;
    private Question[] data;
    private int current;
    private boolean wentBack = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        initQuestions();
        current = prefs.getInt("progress",0);
        for (int i=0; i<current; i++)
            data[i].setRating(prefs.getInt("rating"+i,0));
        wentBack = prefs.getBoolean("wentBack", false);
        bi.backBtn.setEnabled(!wentBack);
        updateQuestion();
        bi.proRating.setOnTouchListener(this);
    }



    private void initQuestions(){
        String[] qs = getResources().getStringArray(R.array.questions);
        data = new Question[qs.length];
        for (int i=0; i<qs.length; i++){
            data[i] = new Question(qs[i]);
        }
    }

    private void previousQuestion(){
        if (current!=0){
            current--;
            updateQuestion();
        }
    }

    private void nextQuestion(){
        if (current< data.length-1){
            current++;
            updateQuestion();
        }
        else {
            openResults();
        }
        if (current == 1 && !wentBack)
            bi.backBtn.setEnabled(true);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        if (motionEvent.getAction()==MotionEvent.ACTION_UP){
            int ratingSet = (int) bi.proRating.getRating();
            if (ratingSet>0){
                data[current].setRating(ratingSet);
                bi.proRating.setIndicator(true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                        bi.proRating.setIndicator(false);
                    }
                },1000);

            }

        }
        return false;
    }

    public void goBack(View v){
        previousQuestion();
        wentBack = true;
        bi.backBtn.setEnabled(false);
    }

    public void pause(View v){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("progress",current);
        for (int i=0; i<current; i++)
            editor.putInt("rating" + i, data[i].getRating());
        editor.putBoolean("wentBack",wentBack);
        editor.apply();
        Intent intent = new Intent();
        intent.putExtra("progress", current);
        setResult(1,intent);
        finish();
    }

    public void stop(View v){
        for (int i=current; i<data.length; i++)
            data[i].setRating(0);
        openResults();
    }


    private void updateQuestion(){
        bi.setQuizQuestion(data[current]);
        bi.progressBar.setProgress(current);
    }

    public int[] calculateResult() {
        int[][] cats = {
                {1, 3, 6, 14, 21, 27, 35, 46},
                {2, 10, 18, 29, 31, 37, 43, 48},
                {4, 13, 20, 24, 26, 33, 40, 45},
                {5, 7, 12, 15, 19, 28, 41, 47},
                {8, 16, 22, 30, 32, 36, 39, 42},
                {9, 11, 17, 23, 25, 34, 38, 44}
        };
        final int[] results = new int[6];
        for (int i=0; i<cats.length; i++){
            int sum = 0;
            for (int j=0; j<cats[i].length; j++){
                sum+=data[(cats[i][j]-1)].getRating();
            }
            results[i]=sum;
        }
        final Integer[] indices = {0,1,2,3,4,5};
        Arrays.sort(indices, new Comparator<Integer>() {
            @Override public int compare(final Integer o1, final Integer o2) {
                if (results[o1]>results[o2]) return -1;
                else if (results[o1]==results[o2]) return 0;
                else return 1;
            }
        });
        for (int i=0; i<indices.length; i++)
            results[i]= indices[i]+1;
        return results;
    }

    private void openResults(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("progress",0);
        for (int i=0; i<current; i++)
            editor.putInt("rating" + i, 0);
        editor.putBoolean("wentBack",false);
        editor.apply();
        Intent intent = new Intent();
        int[] order = calculateResult();
        intent.putExtra("order", order);
        setResult(2,intent);
        finish();
    }

}

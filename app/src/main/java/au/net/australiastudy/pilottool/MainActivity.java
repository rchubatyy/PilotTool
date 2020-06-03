package au.net.australiastudy.pilottool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.Arrays;
import java.util.Comparator;

import au.net.australiastudy.pilottool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SimpleRatingBar.OnRatingBarChangeListener {


    ActivityMainBinding bi;
    Question[] data;
    int current = 0;
    boolean wentBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initQuestions();
        bi.setQuestion(data[0]);
        bi.proRating.setOnRatingBarChangeListener(this);
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
            bi.setQuestion(data[current]);
        }
    }

    private void nextQuestion(){
        if (current< data.length-1){
            current++;
            bi.setQuestion(data[current]);
        }
        else {
        Intent intent = new Intent(this, ResultsActivity.class);
        int[] order = calculateResult();
        intent.putExtra("order", order);
        startActivity(intent);
        finish();
        }
        if (current == 1 && !wentBack)
            bi.backBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRatingChanged(final SimpleRatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser){
            ratingBar.setIndicator(true);
            int ratingSet = (int)Math.ceil(rating);
            data[current].setRating(ratingSet);
            ratingBar.setRating(ratingSet);
            System.out.println(rating);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    nextQuestion();
                    ratingBar.setIndicator(false);
                }
            },1000);

        }
    }

    public void goBack(View v){
        previousQuestion();
        wentBack = true;
        bi.backBtn.setVisibility(View.GONE);
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
            results[i]= indices[i];
        return results;
    }
}

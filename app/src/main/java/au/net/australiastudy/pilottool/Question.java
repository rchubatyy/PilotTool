package au.net.australiastudy.pilottool;

import android.content.res.Resources;

public class Question {
    private String question;
    private int rating = 0;

    Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

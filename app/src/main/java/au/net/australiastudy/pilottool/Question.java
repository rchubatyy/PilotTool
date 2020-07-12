package au.net.australiastudy.pilottool;

public class Question {
    private String name;
    private int rating = 0;

    Question(String question) {
        this.name = question;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

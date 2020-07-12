package au.net.australiastudy.pilottool;

import android.content.SharedPreferences;

public class ChatBot {
    private static ChatBot INSTANCE = null;
    private ChatBot() {
    }


    public static ChatBot getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ChatBot();
        return INSTANCE;
    }



}

package au.net.australiastudy.pilottool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private ChatBot chatBot;
    private SharedPreferences prefs;
    private ChatMessageAdapter adapter;
    private ListView chatView;
    private boolean hadAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatBot = new ChatBot(this);
        chatView = findViewById(R.id.chat);
        EditText messageEdit = findViewById(R.id.editTextMessage);
        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        chatView.setAdapter(adapter);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        hadAllData = chatBot.hasAllData();
        greeting();
        messageEdit.setOnEditorActionListener(this);
    }



    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND){
            String message = textView.getText().toString();
            //bot
            String response = chatBot.generateResponse(message);
            if (TextUtils.isEmpty(message)) {
                return false;
            }
            sendMessage(message);
            respondWith(response);
        textView.setText("");

        }
        return false;
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true);
        adapter.add(chatMessage);
        scrollDown();
    }

    private void respondWith(String message) {
        if (message.equals("")) return;
        ChatMessage chatMessage = new ChatMessage(message, false);
        adapter.add(chatMessage);
        scrollDown();
        if (!hadAllData && chatBot.hasAllData()){
            hadAllData = true;
            respondWith("Now we have all the data about you!");
            prepareForQuiz();
        }
        if (message.contains("let us"))
            startQuiz();
    }

    private void respondWith(String[] messages) {
        for (String message: messages)
            respondWith(message);
    }

    /*private String generateResponse(String message){
        SharedPreferences.Editor editor = prefs.edit();
        switch(chatBot.getMessageType(message)){
            case EMAIL:
                editor.putString("email", message);
                editor.apply();
                return "Your email is set!";
            case AGE:
                int age = ChatBot.firstIntIn(message);
                if (age>=18){
                    editor.putInt("age", age);
                    editor.apply();
                    return "All set! You are "+ age+ " years old!";
                }
                else{
                    prepareForQuiz();
                    return "";}
        }
        return "I do not understand this command yet...";
    }*/

    private void greeting(){
        String[] messages = {"Hello!", "You are first time here!", "Please enter your name, age and email"};
        if (chatBot.hasName())
            messages[0] = "Hello, " + chatBot.getName() + "!";
        if (hadAllData){
            respondWith(messages[0]);
            prepareForQuiz();
        }
        else
            respondWith(messages);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                int progress = data.getIntExtra("progress",0);
                respondWith("You have answered " + progress + " questions so far. Please let me know if you want to continue.");
                break;
            case 2:
                int[] results = data.getIntArrayExtra("order");
                assert results != null;
                String message = "Your best category is #" + results[0] + ". Good luck!";
                respondWith(message);
                break;
        }
    }

    private void startQuiz(){
        final Intent intent = new Intent(this, MainActivity.class);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivityForResult(intent, 1);
            }},2000);
    }

    public void prepareForQuiz(){
        respondWith("Please answer the questions that will appear on screen.");
        startQuiz();
    }

    private void scrollDown(){
        chatView.setSelection(adapter.getCount() - 1);
    }

}
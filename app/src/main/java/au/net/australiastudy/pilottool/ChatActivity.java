package au.net.australiastudy.pilottool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private SharedPreferences prefs;
    private ChatMessageAdapter adapter;
    private ListView chatView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatView = findViewById(R.id.chat);
        EditText messageEdit = findViewById(R.id.editTextMessage);
        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        chatView.setAdapter(adapter);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        respondWith("Hello!");
        respondWith("You are first time here!");
        respondWith("Please enter your name, age and email");
        messageEdit.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND){
            String message = textView.getText().toString();
            //bot
            String response = generateResponse(message);
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
        chatView.setSelection(adapter.getCount() - 1);
    }

    private void respondWith(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        adapter.add(chatMessage);
        chatView.setSelection(adapter.getCount() - 1);
    }

    private String getName(){
        return prefs.getString("name", "");
    }

    private int getAge(){
        return prefs.getInt("age", 0);
    }

    private String hasEmail(){
        return prefs.getString("email", "");
    }

    private String generateResponse(String message){
        return "Response lives here";
    }

    private void greeting(){

    }

}
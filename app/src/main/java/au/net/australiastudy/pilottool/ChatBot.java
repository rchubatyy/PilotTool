package au.net.australiastudy.pilottool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import java.util.HashSet;

import static android.content.Context.MODE_PRIVATE;

public class ChatBot {
    private Context context;
    private SharedPreferences prefs;

    public ChatBot(Context context){
        this.context=context;
        prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
    }

    public Context getContext(){
        return context;
    }


    public enum MessageType{
        NAME,
        NAME_ONLY,
        AGE,
        AGE_ONLY,
        EMAIL,
        EMAIL_ONLY,
        QUIZ_RESTART,
        UNKNOWN
    }
    
    public MessageType getMessageType(String message){
        if (message.contains("resume")||message.contains("Resume")||
                message.contains("restart")||message.contains("Restart")||
                message.contains("continue")||message.contains("Continue")||
                message.contains("start")||message.contains("Start"))
            return MessageType.QUIZ_RESTART;
        if (Patterns.EMAIL_ADDRESS.matcher(message).matches())
            return MessageType.EMAIL_ONLY;
        if (isNumeric(message) && !hasAge())
            return MessageType.AGE_ONLY;
        if (message.contains("years old")||message.contains("year-old")||message.contains("year old")
                ||message.contains("age is")||message.contains("Age is"))
            return MessageType.AGE;
        if (message.contains("name is")||message.contains("Name is")
                ||message.contains("Call me")||message.contains("call me"))
            return MessageType.NAME;
        if (message.matches("^[a-zA-Z\\s]+") && !hasName())
            return MessageType.NAME_ONLY;

        return MessageType.UNKNOWN;
    }

    public String generateResponse(String message){
        switch(getMessageType(message)){
            case EMAIL_ONLY:
                setEmail(message);
                return "This is your email: " + message + " and it has been set!";
            case AGE_ONLY:
                int age = Integer.parseInt(message);
                return setAge(age) ? "Your age is set!" : "You are minor yet...";
            case AGE:
                age = firstIntIn(message);
                return setAge(age) ? "Your age is set!" : "You are minor yet...";
            case NAME_ONLY:
                setName(message);
                return "Nice to meet you, " + message + "!";
            case QUIZ_RESTART:
                if (!hasAllData())
                    return "First, you need to tell me your name, age and email...";
                else
                    return "OK, let us get continue the quiz...";
        }
        return "Sorry, I do not understand you yet";
    }

    private int firstIntIn(String string){
        int i = 0;
        while (i < string.length() && !Character.isDigit(string.charAt(i))) i++;
        int j = i;
        while (j < string.length() && Character.isDigit(string.charAt(j))) j++;
        return Integer.parseInt(string.substring(i, j));
    }

    private boolean isNumeric(String string){
        if (string == null || string.length() == 0) {
            return false;
        }

        for (char c : string.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;

    }

    private void setEmail(String email){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("ratings", new HashSet<String>(0));
        editor.putString("email", email);
        editor.apply();
    }

    private void setName(String name){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("ratings", new HashSet<String>(0));
        editor.putString("name", name);
        editor.apply();
    }

    private boolean setAge(int age){
        if (age<18){
            return false;
        }
        else{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet("ratings", new HashSet<String>(0));
            editor.putInt("age", age);
            editor.apply();
            return true;
        }
    }


    public String getName(){
        return prefs.getString("name", "");
    }

    public int getAge(){
        return prefs.getInt("age", 0);
    }

    public String getEmail(){
        return prefs.getString("email", "");
    }

    public boolean hasName(){
        return !getName().equals("");
    }

    public boolean hasAge(){
        return getAge()>=18;
    }

    public boolean hasEmail(){
        return !getEmail().equals("");
    }

    public boolean hasAllData(){
        return hasName() && hasAge() && hasEmail();
    }

    public boolean containsAny(String message, CharSequence[] data){
        for (CharSequence text:data)
            if (message.contains(text))
                return true;
            return false;
    }

}

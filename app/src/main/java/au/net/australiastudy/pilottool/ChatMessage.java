package au.net.australiastudy.pilottool;

public class ChatMessage {
    private boolean isMine;
    private String content;

    public ChatMessage(String message, boolean mine) {
        content = message;
        isMine = mine;
    }

    public String getContent() {
        return content;
    }

    public boolean isMine() {
        return isMine;
    }

}
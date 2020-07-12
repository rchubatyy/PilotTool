package au.net.australiastudy.pilottool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final int MY_MESSAGE = 0, BOT_MESSAGE = 1;
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.chatmessage_layout, data);
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);
        return item.isMine() ? MY_MESSAGE : BOT_MESSAGE;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatmessage_layout, parent, false);
            TextView textView = convertView.findViewById(R.id.message);
            textView.setText(getItem(position).getContent());
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.botmessage_layout, parent, false);
            TextView textView = convertView.findViewById(R.id.message);
            textView.setText(getItem(position).getContent());
        }
        return convertView;
    }
}

package algonquin.cst2335.nguy1041.data;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MessageViewModel extends ViewModel {
    public ArrayList<String> theMessages = new java.util.ArrayList<>();
    String message;
    String timeSent;
    boolean isSentButton;

    void ChatRoom(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }
}

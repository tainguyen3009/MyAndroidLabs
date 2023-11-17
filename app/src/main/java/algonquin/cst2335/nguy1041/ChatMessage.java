package algonquin.cst2335.nguy1041;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @ColumnInfo(name="message")
    protected String text;

    @ColumnInfo(name="timeStamp")
    protected String timeSent;

    @ColumnInfo(name="SendOrReceive")
    protected boolean sendOrReceive;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public ChatMessage(){}
        public ChatMessage(String m, String t, boolean sent){
            text = m;
            timeSent = t;
            sendOrReceive = sent;
        }
        public String getMessage(){
            return text;
        }
        public String getTimeSent(){
            return timeSent;
        }
        public boolean isSentButton(){
            return sendOrReceive;
        }

        public void setSentButton(boolean sentButton) {
            sendOrReceive = sentButton;
        }
    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
    public void setMessage(String message) {
        this.text = message;
    }


    }


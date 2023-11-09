package algonquin.cst2335.nguy1041;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.nguy1041.data.MessageViewModel;
import algonquin.cst2335.nguy1041.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.nguy1041.databinding.RecieveRowBinding;
import algonquin.cst2335.nguy1041.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MessageViewModel chatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(click -> {
            String messageText = binding.typeMessage.getText().toString();

            // Create a ChatMessage object for a sent message
            ChatMessage sentMessage = new ChatMessage(messageText, getCurrentTime(), true);
            messages.add(sentMessage);

            //messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.typeMessage.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.typeMessage.getText().toString();

            // Create a ChatMessage object for a received message
            ChatMessage receivedMessage = new ChatMessage(messageText, getCurrentTime(), false);
            messages.add(receivedMessage);

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.typeMessage.setText("");
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        chatModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messages = chatModel.messages.getValue();

        class MyRowHolder extends RecyclerView.ViewHolder {
            public TextView messageText;
            public TextView timeText;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.messageText);
                timeText = itemView.findViewById(R.id.timeText);
            }
        }
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }


        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0){
                    SentRowBinding binding = SentRowBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else{
                    RecieveRowBinding binding = RecieveRowBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }}

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                if(messages.get(position).isSentButton()){
                    return 0;
                }
                else return 1;
            }
        });

    }
    public class ChatMessage{
        String message;
        String timeSent;
        boolean isSentButton;
        public ChatMessage(String m, String t, boolean sent)
        {
            message = m;
            timeSent = t;
            isSentButton = sent;
        }
        public String getMessage() {
            return message;
        }

        public String getTimeSent() {
            return timeSent;
        }

        public boolean isSentButton() {
            return isSentButton;
        }
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
        return sdf.format(new Date());
}
}
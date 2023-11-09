package algonquin.cst2335.nguy1041;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.nguy1041.data.MessageViewModel;
import algonquin.cst2335.nguy1041.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.nguy1041.databinding.RecieveRowBinding;
import algonquin.cst2335.nguy1041.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MessageViewModel chatModel;

    ChatMessageDAO mDAO;

    Executor thread = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase md = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = md.cmDAO();

        if(messages == null){
            chatModel.messages.setValue(messages = new ArrayList<>());

            thread.execute(()->{
                messages.addAll(mDAO.getAllMessages());

                runOnUiThread(() ->
                        binding.recycleView.setAdapter(myAdapter));
            });
        }

        binding.sendButton.setOnClickListener(click -> {
            String messageText = binding.typeMessage.getText().toString();

            // Create a ChatMessage object for a sent message
            ChatMessage sentMessage = new ChatMessage(messageText, getCurrentTime(), true);
            messages.add(sentMessage);

            //messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.typeMessage.setText("");

            thread.execute(()->{
                mDAO.insertMessage(sentMessage);
            });
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.typeMessage.getText().toString();

            // Create a ChatMessage object for a received message
            ChatMessage receivedMessage = new ChatMessage(messageText, getCurrentTime(), false);
            messages.add(receivedMessage);

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.typeMessage.setText("");

            thread.execute(()->{
                mDAO.insertMessage(receivedMessage);
            });
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        chatModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messages = chatModel.messages.getValue();

        class MyRowHolder extends RecyclerView.ViewHolder {
            public TextView messageText;
            public TextView timeText;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnClickListener(clk ->{
                    int position = getBindingAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setMessage("Do you want to delete the message:" + messageText.getText())
                            .setTitle("Question: ")
                            .setNegativeButton("No", (dialog, cl) -> {})
                            .setPositiveButton("Yes", (dialog, cl) ->{
                                ChatMessage removedMessage = messages.get(position);
                                messages.remove(position);
                                myAdapter.notifyItemRemoved(position);

                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", c -> {
                                            messages.add(position, removedMessage);
                                            myAdapter.notifyItemInserted(position);
                                        }).show();
                            }).create().show();
                });

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

    private  String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
        return  sdf.format(new Date());
    }

}
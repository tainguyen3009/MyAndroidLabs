package algonquin.cst2335.nguy1041;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.nguy1041.data.ChatRoomViewModel;
import algonquin.cst2335.nguy1041.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.nguy1041.databinding.RecieveRowBinding;
import algonquin.cst2335.nguy1041.databinding.SentRowBinding;


public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;
    ChatMessageDAO mDAO;
    Executor thread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        binding.button.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();

            // Create a ChatMessage object for a sent message
            ChatMessage sentMessage = new ChatMessage(messageText, getCurrentTime(), true);
            messages.add(sentMessage);

            //messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            thread.execute(() ->
            {
                long messageId = mDAO.insertMessage(sentMessage);
                sentMessage.id = (int)messageId;
                Log.d("TAG", "The id created is: " + sentMessage.id);
            });
        });

        binding.receive.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();

            // Create a ChatMessage object for a received message
            ChatMessage receivedMessage = new ChatMessage(messageText, getCurrentTime(), false);
            messages.add(receivedMessage);

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");

            thread.execute(() ->
            {
                long messageId = mDAO.insertMessage(receivedMessage);
                receivedMessage.id = (int)messageId;
                Log.d("TAG", "The id created is: " + receivedMessage.id);
            });
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        class MyRowHolder extends RecyclerView.ViewHolder {
            public TextView messageText;
            public TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);

                messageText = itemView.findViewById(R.id.messageText);
                timeText = itemView.findViewById(R.id.timeText);

                itemView.setOnClickListener(clk -> {
                    int position = getAbsoluteAdapterPosition();
                    ChatMessage selected = messages.get(position);

                    chatModel.selectedMessage.postValue(selected);
                    /*
                    int position = getBindingAdapterPosition();

                    AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                    builder.setMessage("Do you want to delete the message: " + messageText.getText())
                            .setTitle("Question: ")
                            .setNegativeButton("No", (dialog, cl) -> {})
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                ChatMessage removedMessage = messages.get(position);
                                thread.execute(() -> {
                                    mDAO.deleteMessage(removedMessage);
                                });

                                messages.remove(position);
                                myAdapter.notifyItemRemoved(position);

                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", c -> {
                                            messages.add(position, removedMessage);
                                            myAdapter.notifyItemInserted(position);
                                            thread.execute(() -> {
                                    mDAO.deleteMessage(removedMessage);
                                });
                                        }).show();
                            }).create().show(); */
                });
            }
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



        if(messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());

            thread.execute(() ->
            {
                messages.addAll(mDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);  //newValue is the newly set ChatMessage
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.addToBackStack("asd");
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.commit();
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
        return sdf.format(new Date());
}


}
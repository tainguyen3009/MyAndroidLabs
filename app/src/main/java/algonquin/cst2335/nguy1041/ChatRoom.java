package algonquin.cst2335.nguy1041;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

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

        setSupportActionBar(binding.myToolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.itemDelete){
            showDeleteMessage();
        } else if ( itemId == R.id.itemAbout){
            Toast.makeText(this, "Version 1.0, Created by Tai Nguyen", Toast.LENGTH_LONG);
        }
//        switch (item.getItemId()) {
//            case R.id.itemDelete:
//                showDeleteMessage();
//                break;
//            case R.id.itemAbout:
//                Toast.makeText(this, "Version 1.0, created by Tai Nguyen", Toast.LENGTH_SHORT).show();
//                break;
//        }
        return true;
    }

    private void showDeleteMessage() {
        if (chatModel.selectedMessage.getValue() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to delete this message?")
                    .setTitle("Delete")
                    .setNegativeButton("No", (dialog, which) -> {
                        // if "No" is clicked
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // if "Yes" is clicked
                        ChatMessage toDelete = chatModel.selectedMessage.getValue();
                        if (toDelete != null) {
                            Executor thread1 = Executors.newSingleThreadExecutor();
                            thread1.execute(() -> {
                                // delete from the database
                                mDAO.deleteMessage(toDelete);
                            });

                            int position = messages.indexOf(toDelete);
                            messages.remove(position); // remove from the array list
                            myAdapter.notifyItemRemoved(position); // notify the adapter of the removal

                            // give feedback: anything on the screen
                            Snackbar.make(findViewById(android.R.id.content), "You deleted the message", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", (btn) -> {
                                        Executor thread2 = Executors.newSingleThreadExecutor();
                                        thread2.execute(() -> {
                                            mDAO.insertMessage(toDelete);
                                        });

                                        messages.add(position, toDelete);
                                        myAdapter.notifyItemInserted(position); // notify the adapter of the insertion
                                    }).show();
                        }
                    });

            builder.create().show();
        }
    }




    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
        return sdf.format(new Date());
}


}
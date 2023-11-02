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

import java.lang.reflect.Array;
import java.util.ArrayList;

import algonquin.cst2335.nguy1041.data.MessageViewModel;
import algonquin.cst2335.nguy1041.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.nguy1041.databinding.RecieveRowBinding;
import algonquin.cst2335.nguy1041.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {


    ActivityChatRoomBinding binding;
    RecyclerView.Adapter myAdapter;
    ArrayList<String> theMessages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageViewModel chatModel = new ViewModelProvider(this).get(MessageViewModel.class);
        theMessages = chatModel.theMessages;


        binding.addSomeThing.setOnClickListener(click -> {
            theMessages.add(binding.newMessage.getText().toString());

            binding.newMessage.setText("");
            myAdapter.notifyItemInserted(theMessages.size()-1);

        });

        //create row 0 to ..
        binding.myRecyclerView.setAdapter(
                new RecyclerView.Adapter<MyRowHolder>() {

                    // just inflate the xml
                    @NonNull
                    @Override
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // viewType will be 0 for first 3 rows, 1 for everything after

                        if (viewType == 0) {
                            SentRowBinding rowBinding = SentRowBinding.inflate(getLayoutInflater(), parent, false);

                            return new MyRowHolder(binding.getRoot());
                        } else {
                            //after row 3
                            RecieveRowBinding rowBinding =  RecieveRowBinding.inflate(getLayoutInflater(), parent, false);

                            return new MyRowHolder(binding.getRoot());
                        }
                    }

                    @Override
                    public int getItemViewType(int position){
                        if (position < 3)
                            return 0;
                        else
                            return 1;
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        //replace the default text  with text at row position
                        String forRow = theMessages.get(position);
                        holder.message.setText("Text for row " + position);
                        holder.time.setText("Time for row " + position);

                    }

                    @Override
                    public int getItemCount() {
                        return theMessages.size();
                    }
                }
        ); // populate the list

        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // this represents a single row on the list
    class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);

        }
    }
}
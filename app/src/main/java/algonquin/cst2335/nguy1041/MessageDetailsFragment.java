package algonquin.cst2335.nguy1041;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.nguy1041.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m){
        selected = m;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Create the view binding class for the new layout:
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.text);
        binding.timeText.setText(selected.timeSent);
        binding.sendReceiveText.setText("" + selected.sendOrReceive);
        binding.databaseText.setText("Id = " + selected.id);

        return binding.getRoot();
    }
}

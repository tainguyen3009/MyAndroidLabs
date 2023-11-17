package algonquin.cst2335.nguy1041.data;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.nguy1041.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();
    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData< >();

}
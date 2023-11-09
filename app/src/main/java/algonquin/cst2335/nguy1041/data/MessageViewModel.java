package algonquin.cst2335.nguy1041.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.nguy1041.ChatRoom;

public class MessageViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatRoom.ChatMessage>> messages = new MutableLiveData<>();}

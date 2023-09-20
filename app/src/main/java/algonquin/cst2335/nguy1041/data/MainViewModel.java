package algonquin.cst2335.nguy1041.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
// data survive rotation changes
public class MainViewModel extends ViewModel {

    // observe this object:
    public MutableLiveData<String> editString = new  MutableLiveData<> ("");

    public MutableLiveData<Boolean> onOrOff = new MutableLiveData<Boolean>(false);
}

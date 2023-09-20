package algonquin.cst2335.nguy1041.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.nguy1041.R;
import algonquin.cst2335.nguy1041.data.MainViewModel;
import algonquin.cst2335.nguy1041.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());


        TextView mytext = binding.textview;
        Button b = binding.Button;
        EditText editText = binding.editext;

        binding.cb.setOnCheckedChangeListener(
                ( btn , onOrOff ) -> {
                    viewModel.onOrOff.postValue(onOrOff);
                }
        );

        binding.sw.setOnCheckedChangeListener(
                ( btn , onOrOff ) -> {
                    viewModel.onOrOff.postValue(onOrOff);
                }
        );

        binding.rb.setOnCheckedChangeListener(
                ( btn , onOrOff ) -> {
                    viewModel.onOrOff.postValue(onOrOff);
                }
        );

        binding.imbtn.setOnClickListener(clk -> { });
        viewModel.onOrOff.observe(this, newValue -> {
            binding.cb.setChecked(newValue);
            binding.sw.setChecked(newValue);
            binding.rb.setChecked(newValue);
        });


        // put the string back into the edit text
        editText.setText(viewModel.editString.getValue());

        //this will be called when the value changes
        viewModel.editString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                b.setText("Your text is now: " + s);
                mytext.setText("Your text is now " + s);
            }
        });

        // OnClick Listener     // anonymnous class
        b.setOnClickListener(v -> {
            viewModel.editString.postValue(editText.getText().toString());

        });
    }
}
//            @Override
//            public void onClick(View v) {
//
//                // run when click button
//                mytext.setText(R.string.button_message);
//                String String = editText.getText().toString(); // read what the user typed
//                viewModel.editString.postValue(String); // set the value, and notify it
//                b.setText("Handsome guy");
//            }
//        });

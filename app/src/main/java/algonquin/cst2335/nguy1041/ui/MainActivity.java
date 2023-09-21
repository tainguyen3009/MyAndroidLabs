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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageButton imageButton = findViewById(R.id.imageButton);

        binding.cb.setOnCheckedChangeListener(
                ( compoundButton , isChecked ) -> {
                    viewModel.onOrOff.postValue(isChecked);
                    createDisplayContext("CheckBox", isChecked);
                }
        );

        binding.sw.setOnCheckedChangeListener(
                ( compoundButton , isChecked ) -> {
                    viewModel.onOrOff.postValue(isChecked);
                    createDisplayContext("Switch", isChecked);
                }
        );

        binding.rb.setOnCheckedChangeListener(
                ( compoundButton , isChecked ) -> {
                    viewModel.onOrOff.postValue(isChecked);
                    createDisplayContext("Radio Button", isChecked);

                }
        );

        binding.imageButton.setOnClickListener(clk -> {
            int width = clk.getWidth();
            int height = clk.getHeight();

            String message = "Width: " + width + " pixels\nHeight: " + height + "pixels";


            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });

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

    private void createDisplayContext(String buttonName, boolean isChecked) {
        String cpMethod = buttonName + " is " + (isChecked ? "Checked " : " Unchecked");
        Toast.makeText(this, cpMethod, Toast.LENGTH_SHORT).show();
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

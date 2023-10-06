package algonquin.cst2335.nguy1041;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.nguy1041.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // get variable
        Intent newPage = getIntent();
        String userInput = newPage.getStringExtra("LoginEmail"); // default is null
        double age = newPage.getDoubleExtra("Age", 0.0);
        int houseNumber = newPage.getIntExtra("Address", 98);

        binding.buttonPage2.setOnClickListener(click ->{
            // to go back:
            finish();
        });
    }
}
package algonquin.cst2335.nguy1041;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

import algonquin.cst2335.nguy1041.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        File mySandBox = getFilesDir(); // return where you can save files
        String path = mySandBox.getAbsolutePath();

        if (mySandBox.exists()){
        // if yes, open it
        } else {
            // else close it
        }

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String emailFromFile = prefs.getString("LoginEmail","");
        binding.emailField.setText(emailFromFile);

        binding.loginButton.setOnClickListener( click->{
            // prefs had loaded data

            // get file editor:

            SharedPreferences.Editor editor = prefs.edit();



            // do this when clicked:
            Intent newPage = new Intent(MainActivity.this ,SecondActivity.class);
            /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED){
                startActivity(cameraIntent);
            } else{
                requestPermissions(new String[] { android.Manifest.permission.CAMERA}, 0);
            }
            startActivity( cameraIntent );*/

            String userInput = binding.emailField.getText().toString();

            // put to disk:
            editor.putString("LoginEmail", userInput); //go to disk
            editor.putFloat("Age", 30.3f);
            editor.apply();


            newPage.putExtra("LoginEmail", userInput);
            //newPage.putExtra("Age", 24.1); // double
            startActivity(newPage); // this will go to a new page

;


        } );
      Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

}

    @Override // app is visible
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - Visible on screen" );

    }

    @Override // now respond to clicks
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onResume() - respond to user input" );

    }

    @Override // opposite to onResume, no longer getting clicks
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - no longer getting clicks" );

    }

    @Override // no longer available
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onStop() - no longer available" );

    }

    @Override // been garbage
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onDestroy() - been garbage" );

    }
}
package algonquin.cst2335.nguy1041;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

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

        binding.phoneButton.setOnClickListener(click -> {
            String phoneNumber = binding.editTextPhone.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            if (call.resolveActivity(getPackageManager()) != null) {
                startActivity(call);
            }

        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                            new ActivityResultContracts.StartActivityForResult()
                            ,
                            new ActivityResultCallback<ActivityResult>() {

                                @Override

                                public void onActivityResult(ActivityResult result) {
                                    if (result.getResultCode() == Activity.RESULT_OK) {
                                        Intent data = result.getData(); // this has photo in here
                                        Bitmap thumbnail = data.getParcelableExtra("picturedata");

                                    }
                                }
                            }
                    );
            binding.picButton.setOnClickListener(click1 ->{
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    cameraResult.launch(cameraIntent);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
                }
        });
    }
}








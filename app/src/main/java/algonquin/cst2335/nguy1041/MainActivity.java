package algonquin.cst2335.nguy1041;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mytext = findViewById(R.id.textview);
        Button b = findViewById(R.id.Button);
        EditText editText = findViewById(R.id.editext);

        // OnClick Listener     // anonymnous class
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // run when click button
                mytext.setText("Button Clicked");
                editText.setText("Button Clicked");
                b.setText("Handsome guy");
            }
        });
    }

}
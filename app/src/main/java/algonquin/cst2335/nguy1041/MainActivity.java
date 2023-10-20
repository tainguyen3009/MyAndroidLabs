package algonquin.cst2335.nguy1041;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class is the first page of the application
 * @author Tai Nguyen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * this represents the string message at the top
     */
    private TextView theMessage;
    /**
     * this hold the login button object
     */
    private Button loginButton;
    /**
     * this hold the text field to write the password
     */
    private EditText passwordText;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this bundle contains the data it most 
     * recently supplied in {@link #onSaveInstanceState}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theMessage = findViewById(R.id.textView);
        loginButton = findViewById(R.id.loginButton);
        passwordText = findViewById(R.id.loginText);

        loginButton.setOnClickListener( click -> {
            String password = passwordText.getText().toString();
            boolean isComplex = isComplexEnough(password);
            if (isComplex)
                theMessage.setText("It has upper and lower case");
            else theMessage.setText("You are missing something");
        });
    }

    /**
     * This function scans a string and returns true if it has an upper and lower case
     *
     * @param text this is the String to search
     * @return True if text has upper case, lower case
     */
    private boolean isComplexEnough(String text){
        boolean result = false;
        boolean foundUpperCase = false, foundLowerCase = false;
        boolean foundNumeric = false, foundSpecialChar = false, foundAllRequirement= false;

        for (int i =0; i < text.length(); i++){
            char c = text.charAt(i);
            if(Character.isUpperCase(c))
                foundUpperCase = true;
            if (Character.isLowerCase(c))
                foundLowerCase = true;
            if(Character.isDigit(c))
                foundNumeric = true;
            if(Character.isLetterOrDigit(c))
                foundSpecialChar = true;
            if( foundAllRequirement = foundUpperCase && foundLowerCase && foundNumeric && foundSpecialChar)
                foundAllRequirement = true;

        }
        return (foundLowerCase && foundUpperCase && foundNumeric && foundSpecialChar && foundAllRequirement);
    }
}
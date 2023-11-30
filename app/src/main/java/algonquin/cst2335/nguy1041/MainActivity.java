package algonquin.cst2335.nguy1041;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import algonquin.cst2335.nguy1041.databinding.ActivityMainBinding;

/**
 * The MainActivity class is the main activity for the Android app.
 * It handles the user interface and password complexity checking.
 * @author Tai Nguyen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;
    /**
     * This method is called when the activity is created.
     * It sets up the user interface elements and handles the button click event.
     * @param savedInstanceState The saved instance state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState); // call onCreate from parent

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.forecastButton.setOnClickListener(click -> {
            //get the forecast urlencoder will replace spaces with +
            try {
                cityName = URLEncoder.encode(binding.editText.getText().toString(), "UTF8");

                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                JSONObject mainOBj = response.getJSONObject("main");
                                double temperature = mainOBj.getDouble("temp");
                                double min = mainOBj.getDouble("temp_min");
                                double max = mainOBj.getDouble("temp_max");
                                int humidity = mainOBj.getInt("humidity");

                                // Extract weather description from the "weather" array
                                JSONArray weatherArrayResponse = response.getJSONArray("weather");
                                String description = "";
                                if (weatherArrayResponse.length() > 0) {
                                    JSONObject weatherObj = weatherArrayResponse.getJSONObject(0);
                                    description = weatherObj.getString("description");
                                }

                                // textview for info we want to display on screen
                                TextView tempText = findViewById(R.id.tempText);
                                TextView maxText = findViewById(R.id.maxText);
                                TextView minText = findViewById(R.id.minText);
                                TextView humidityText = findViewById(R.id.humidityText);
                                TextView descText = findViewById(R.id.descText);

                                // Update TextViews with obtained values
                                tempText.setText("Current temperature is " + temperature);
                                tempText.setVisibility(View.VISIBLE);

                                maxText.setText("Max temperature is " + max);
                                maxText.setVisibility(View.VISIBLE);

                                minText.setText("Min temperature is " + min);
                                minText.setVisibility(View.VISIBLE);

                                humidityText.setText("Humidity is " + humidity);
                                humidityText.setVisibility(View.VISIBLE);

                                descText.setText("Description: " + description);
                                descText.setVisibility(View.VISIBLE);

                                String iconName = "";
                                JSONArray weatherArray = response.getJSONArray("weather");

                                for (int i = 0; i < weatherArray.length(); i++) {
                                    JSONObject thisObj = weatherArray.getJSONObject(i);
                                    iconName = thisObj.getString("icon");
                                }

                                //query#2 for image
                                String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";
                                String finalIconName = iconName;

                                ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        // Do something with loaded bitmap...
                                        Log.d("Image received", "image was gotten");
                                        Bitmap receivedBitmap = bitmap;

                                        // display Image on screen
                                        binding.weatherImage.setImageBitmap(bitmap);
                                        binding.weatherImage.setVisibility(View.VISIBLE);

                                        // Save the bitmap to a file
                                        FileOutputStream fOut = null;
                                        try {
                                            fOut = openFileOutput(finalIconName + ".png", Context.MODE_PRIVATE);
                                            receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                            fOut.flush();
                                            fOut.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error) -> {
                                            Log.d("Image error", "image was not gotten");
                                        });
                                queue.add(imgReq); //Fetches image from the server
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Log.d("Response", "Received" + response.toString());
                        }, //gets called if server responded appropriately
                        (error) -> {
                        } //gets called if theres an error when calling server or no response
                );
                queue.add(request); //Fetches from the server
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}



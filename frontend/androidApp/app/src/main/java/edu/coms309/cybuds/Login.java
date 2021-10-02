package edu.coms309.cybuds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLogin_Back_onClick(View view) {
        finish();
        //Intent gotoWelcomeActivityIntent = new Intent(this, MainActivity.class);
        //startActivity(gotoWelcomeActivityIntent);
    }

    public void btnLogin_submit_onClick(View view) {

        final TextView textView = (TextView) findViewById(R.id.textBoxLogin_UserName);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="https://www.ece.iastate.edu/~alexs/classes/2021_Fall_281/";
        //String url = "https://en.wikipedia.org/wiki/HTTPS";
        //String url = "http://example.com/";
        String url = "https://www.iastate.edu/index/Z";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
    // Add the request to the RequestQueue.
        queue.add(stringRequest);



        Toast.makeText(Login.this,"TODO!", Toast.LENGTH_SHORT).show();
    }
}
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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
        //GET Request:
        /*
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
                        textView.setText("GET didn't work!");
                    }
        });
    // Add the request to the RequestQueue.
        queue.add(stringRequest);*/

        //Post Request:
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url = "https://www.iastate.edu/index/Z";
        String url = "https://109cdd6d-625e-4049-8d44-b5c41012075f.mock.pstmn.io/a";
        JSONObject post= new JSONObject();
        try{
            post.put("username","name");
            post.put("password","code");

        }
        catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textView.setText("Good Response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("POST didn't work!");
            }
        });



        queue.add(jsonObjectRequest);


        Toast.makeText(Login.this,"TODO!", Toast.LENGTH_SHORT).show();
    }
}
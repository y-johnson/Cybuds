package edu.coms309.cybuds;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;


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


        final EditText tb_username = findViewById(R.id.textBoxLogin_Password);
        final EditText tb_email = findViewById(R.id.textBoxLogin_Email);
        //GET Request:
/*
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="https://www.ece.iastate.edu/~alexs/classes/2021_Fall_281/";
        //String url = "https://en.wikipedia.org/wiki/HTTPS";
        //String url = "http://example.com/";
        //String url = "https://www.iastate.edu/index/Z";
        //String url = "http://coms-309-028.cs.iastate.edu:8080/users/login";
        String url = "https://109cdd6d-625e-4049-8d44-b5c41012075f.mock.pstmn.io/b";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        tb_username.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tb_username.setText("GET didn't work!");
                    }
        });
    // Add the request to the RequestQueue.
        queue.add(stringRequest);
*/
        //Post Request:
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-028.cs.iastate.edu:8080/users/login";
        //String url = "https://109cdd6d-625e-4049-8d44-b5c41012075f.mock.pstmn.io/b";
        JSONObject post= new JSONObject();
        try {
            post.put("email",tb_email.getText());
            post.put("passwordHash",tb_username.getText());

        }
        catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //tb_username.setText("Good Response");
                //Toast.makeText(Login.this,"Success!", Toast.LENGTH_SHORT).show();
                try {
                    Toast.makeText(Login.this,"Success\r\nUser ID: "+ response.getString("id"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    finish();
                    //e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //tb_username.setText("POST didn't work!");
                Toast.makeText(Login.this,"Fail!", Toast.LENGTH_SHORT).show();
            }
        });



        queue.add(jsonObjectRequest);


        //Toast.makeText(Login.this,"TODO!", Toast.LENGTH_SHORT).show();
    }
}
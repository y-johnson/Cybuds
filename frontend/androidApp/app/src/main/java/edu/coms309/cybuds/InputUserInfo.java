package edu.coms309.cybuds;

import static edu.coms309.cybuds.api.ApiClientFactory.GetUserApi;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.coms309.cybuds.api.SlimCallback;
import edu.coms309.cybuds.model.User;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
//import test.connect.myapplication.api.SlimCallback;

public class InputUserInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView apiText1 = findViewById(R.id.activity_main_textView1);

        apiText1.setMovementMethod(new ScrollingMovementMethod());
        apiText1.setHeight(350);

        Button postByBodyBtn = findViewById(R.id.activity_main_post_by_body_button);

        EditText idIn = findViewById(R.id.activity_main_id);
        EditText usernameIn = findViewById(R.id.activity_main_username);
        EditText emailIn = findViewById(R.id.activity_main_email);
        EditText passwordIn = findViewById(R.id.activity_main_password);
        EditText firstnameIn = findViewById(R.id.activity_main_firstname);
        EditText middlenameIn = findViewById(R.id.activity_main_middlename);
        EditText lastnameIn = findViewById(R.id.activity_main_lastname);
        EditText addressIn = findViewById(R.id.activity_main_address);
        EditText phonenumberIn = findViewById(R.id.activity_main_phonenumber);
        EditText genderIn = findViewById(R.id.activity_main_gender);

        RegenerateAllUsersOnScreen(apiText1);

        postByBodyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User();
                newUser.setUsername(usernameIn.getText().toString());
                newUser.setEmail(emailIn.getText().toString());
                newUser.setPasswordHash(passwordIn.getText().toString());
                newUser.setFirstName(firstnameIn.getText().toString());
                newUser.setMiddleName(middlenameIn.getText().toString());
                newUser.setLastName(lastnameIn.getText().toString());
                newUser.setAddress(addressIn.getText().toString());
                newUser.setPhoneNumber(phonenumberIn.getText().toString());
                newUser.setGender(genderIn.getText().toString());

                //my code
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://coms-309-028.cs.iastate.edu:8080/users/login";


                JSONObject post = new JSONObject();
                try {
                    post.put("idIn", idIn.getText());
                    post.put("usernameIn", usernameIn.getText());
                    post.put("email", emailIn.getText());
                    post.put("firstName", firstnameIn.getText());
                    post.put("middleName", middlenameIn.getText());
                    post.put("lastName", lastnameIn.getText());
                    post.put("address", addressIn.getText());
                    post.put("number", phonenumberIn.getText());
                    post.put("gender", genderIn.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //



                GetUserApi().addUser(newUser).enqueue(new SlimCallback<User>(user -> {
                    RegenerateAllUsersOnScreen(apiText1);
                    idIn.setText("");
                    usernameIn.setText("");
                    emailIn.setText("");
                    passwordIn.setText("");
                    firstnameIn.setText("");
                    middlenameIn.setText("");
                    lastnameIn.setText("");
                    addressIn.setText("");
                    phonenumberIn.setText("");
                    genderIn.setText("");
                }));

                //path
               /* GetUserApi().getAllUsers(idIn.getText().toString(), usernameIn.getText().toString(),
                        emailIn.getText().toString(), passwordIn.getText().toString(),
                        passwordIn.getText().toString(), firstnameIn.getText().toString(),
                        middlenameIn.getText().toString(), lastnameIn.getText().toString(),
                        addressIn.getText().toString(), phonenumberIn.getText().toString(),
                        genderIn.getText().toString()).enqueue(new SlimCallback<User>(user->{
                            RegenerateAllUsersOnScreen(apiText1);
                            idIn.setText("");
                            usernameIn.setText("");
                            emailIn.setText("");
                            passwordIn.setText("");
                            firstnameIn.setText("");
                            middlenameIn.setText("");
                            lastnameIn.setText("");
                            addressIn.setText("");
                            phonenumberIn.setText("");
                            genderIn.setText("");
                }));*/
            }
        });
    }


    void RegenerateAllUsersOnScreen(TextView apiText1) {
        //User is of type List, this seems better but need to check.
        GetUserApi().getAllUsers().enqueue(new SlimCallback<List<User>>(users -> {
            apiText1.setText("");

            for (int i = users.size() - 1; i >= 0; i--) {
                apiText1.append(users.get(i).printable());
            }
        }, "GetAllUser"));

         /* //is of type iterable as defined in UserController.java
        GetUserApi().getAllUsers().enqueue(new SlimCallback<Iterable<User>>(users -> {
            apiText1.setText("");

            for (int i = users.size()-1; i >=0; i--){
                apiText1.append(users.get(i).printable());
            }
        }, "GetAllUser"));*/
    }

}
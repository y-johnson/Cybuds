package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static edu.coms309.cybuds.api.ApiClientFactory.GetUserApi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.coms309.cybuds.api.SlimCallback;
import edu.coms309.cybuds.model.User;

import java.util.List;
//import test.connect.myapplication.api.SlimCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    public void btnLogin_onClick(View view) {
        Intent gotoLoginActivityIntent = new Intent(this, Login.class);
        startActivity(gotoLoginActivityIntent);
        //setContentView(R.layout.activity_login);
    }
    public void btnSignUp_onClick(View view) {
        //Toast.makeText(MainActivity.this, "TODO!", Toast.LENGTH_SHORT).show();
        Intent gotoRegisterActivityIntent = new Intent(this, ActivityRegister.class);
        startActivity(gotoRegisterActivityIntent);
    }
}
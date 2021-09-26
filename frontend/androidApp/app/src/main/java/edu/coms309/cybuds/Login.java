package edu.coms309.cybuds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLogin_submit_onClick(View view) {
        Toast.makeText(Login.this,"TODO!", Toast.LENGTH_SHORT).show();
    }
}
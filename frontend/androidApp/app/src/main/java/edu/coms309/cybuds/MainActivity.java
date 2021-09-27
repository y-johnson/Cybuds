package edu.coms309.cybuds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnLogin_onClick(View view) {
        setContentView(R.layout.activity_login);
    }
    public void btnSignUp_onClick(View view) {
        Toast.makeText(MainActivity.this, "TODO!", Toast.LENGTH_SHORT).show();
    }
}
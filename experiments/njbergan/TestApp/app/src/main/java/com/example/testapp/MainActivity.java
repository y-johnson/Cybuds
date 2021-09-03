package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Button btnHelloWorld2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*btnClickMe.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              // Do something in response to button click
                                          }
                                      }*/
        Button btnHelloWorld2 = findViewById(R.id.btnHelloWorld2);

        btnHelloWorld2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            }
        });
        //Initialization Button

        //btnClickMe = (Button) findViewById(R.id.btnClickMe);

        //btnClickMe.setOnClickListener(MainActivity.this);
        //Here MainActivity.this is a Current Class Reference (context)
    }

    public void btnClickMe_onClick(View view) {
        // Do something in response to button click
        EditText editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        Toast.makeText(MainActivity.this, /*R.string.incorrect_toast"Hello"*/editTextTextPersonName.getText()+"!", Toast.LENGTH_SHORT).show();
    }

}
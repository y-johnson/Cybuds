package edu.coms309.cybuds;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.activity_main_inputuserinfo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputUser();
            }
        });
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

    /*public void activity_main_inputuserinfo(View view) {
        Intent gotoLoginActivityIntent = new Intent(this, InputUserInfo.class);
        startActivity(gotoLoginActivityIntent);
        //setContentView(R.layout.activity_login);
    }*/

    public void openInputUser(){
        Intent intent = new Intent(this, InputUserInfo.class);
        startActivity(intent);
    }



}
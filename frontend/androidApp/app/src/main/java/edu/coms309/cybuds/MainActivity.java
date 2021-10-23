package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
//import test.connect.myapplication.api.SlimCallback;

public class MainActivity extends AppCompatActivity {

    //private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    /*@Override
    protected void onResume() {
        super.onResume();
        Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(gotoProfileActivityIntent);
    }*/

    public void btnLogin_onClick(View view) {
        Intent gotoLoginActivityIntent = new Intent(this, Login.class); //for switching activities
        startActivity(gotoLoginActivityIntent);
        //setContentView(R.layout.activity_login);  //for switching layouts but not activities?
    }

    public void btnSignUp_onClick(View view) {
        Intent gotoRegisterActivityIntent = new Intent(this, ActivityRegister.class);
        startActivity(gotoRegisterActivityIntent);
    }



    /*public void activity_main_inputuserinfo(View view) {
        Intent gotoLoginActivityIntent = new Intent(this, InputUserInfo.class);
        startActivity(gotoLoginActivityIntent);
        //setContentView(R.layout.activity_login);
    }*/

//    public void openInputUser(){
//        Intent intent = new Intent(this, InputUserInfo.class);
//        startActivity(intent);
//    }


}
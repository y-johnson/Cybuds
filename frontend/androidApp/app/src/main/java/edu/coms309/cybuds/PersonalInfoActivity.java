package edu.coms309.cybuds;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_personal_info);
    }

  /*  public void btnLogin_onClick(View view) { //assign to buttons as alternative
        Intent gotoLoginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(gotoLoginActivityIntent);
    }

    public void btnLogin_onClick(View view) {
        Intent gotoLoginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(gotoLoginActivityIntent);
    }*/
}

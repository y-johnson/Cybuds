package edu.coms309.cybuds;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OtherUsersProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users_profile);
    }

    public void btnUserProfile_menu_onClick(View view) {

    }

    public void btnUserProfile_settings_onClick(View view) {
        setContentView(R.layout.activity_profile_settings);
    }
    public void btnUserProfile_profile_OnClick(View view) {
        setContentView(R.layout.activity_user_profile);
    }
}

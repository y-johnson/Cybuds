package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.coms309.cybuds.model.User;

public class SearchPeopleActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);

        Intent lastIntent = getIntent();
        user = (User) lastIntent.getSerializableExtra("currentUserProfile");

    }

    public void searchPeopleType_submit_onClick(View view) {
        RadioButton searchByClassRadio = findViewById(R.id.radio_searchPeopleType_byClass);
        RadioButton searchByMajorRadio = findViewById(R.id.radio_searchPeopleType_byMajor);
        RadioButton searchByCollegeRadio = findViewById(R.id.radio_searchPeopleType_byCollege);

        Intent toProfile = new Intent(getBaseContext(), MatchPeopleActivity.class);
        toProfile.putExtra("currentUserProfile", user);

        if (searchByClassRadio.isChecked())
            toProfile.putExtra("searchType", 1);
        else if (searchByMajorRadio.isChecked())
            toProfile.putExtra("searchType", 2);
        else if (searchByCollegeRadio.isChecked())
            toProfile.putExtra("searchType", 3);
        else
            return;

        startActivity(toProfile);
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

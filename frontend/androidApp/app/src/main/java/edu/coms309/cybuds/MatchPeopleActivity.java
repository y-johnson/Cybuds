package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import edu.coms309.cybuds.model.User;
import edu.coms309.cybuds.network.HTTPDriver;

public class MatchPeopleActivity extends AppCompatActivity {
    User user, matchedUser;
    int searchType;
    private static final HTTPDriver HTTP_DRIVER = new HTTPDriver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_people);

        Intent lastIntent = getIntent();
        user = (User) lastIntent.getSerializableExtra("currentUserProfile");
        searchType = lastIntent.getIntExtra("searchType",0);

        try {
            HTTP_DRIVER.requestMatch( getBaseContext(), user, searchType,
                    (response, context) -> {
                        User responseUser = new Gson().fromJson(response.toString(), User.class);

                        Toast.makeText(getBaseContext(), String.format("Matching: ", responseUser.getFirstName()), Toast.LENGTH_SHORT).show();

                        setMatchedUser(responseUser);

                        try {
                            TextView matchedUserName = findViewById(R.id.activity_user_profile_name);
                            TextView matchedUserBio = findViewById(R.id.activity_user_profile_bio);
                            matchedUserName.setText(String.format("%s %s", responseUser.getFirstName(), responseUser.getLastName()));
                            matchedUserBio.setText(responseUser.getBiography());
                        } catch (Exception e) {
                            Toast.makeText( getBaseContext(), "Error: Parsing", Toast.LENGTH_LONG ).show();
                        }

                        /*Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
                        toProfile.putExtra("currentUserProfile", responseUser);
                        startActivity(toProfile);*/
                    }
            );
        } catch (Exception e) {
            Toast.makeText( getBaseContext(), "Error: Network", Toast.LENGTH_LONG ).show();
        }


    }

    public void setMatchedUser(User newMatchedUser) {
        this.matchedUser = newMatchedUser;

    }
}

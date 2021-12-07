package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import edu.coms309.cybuds.model.User;

public class ProfileActivity extends AppCompatActivity {

	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		/*edu.coms309.cybuds.databinding.ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		BottomNavigationView navView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
				.build();
		NavHostFragment navHostFragment =
				(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_profile);
		assert navHostFragment != null;
		NavController navController = navHostFragment.getNavController();
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView, navController);*/

		Intent i = getIntent();
		user = (User) i.getSerializableExtra("currentUserProfile");

		TextView t = findViewById(R.id.activity_user_profile_name);
		//t.setText("asdf".toCharArray(),0,1);

		t.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
		t = findViewById(R.id.activity_user_profile_bio);
		t.setText(user.printable());
	}


	public void activity_user_profile_bio_edit_onClick(View view) {
		//FrameLayout fl = findViewById(android.R.id.custom);
		//fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		EditText editTextProfileBio = findViewById(R.id.editTextTextPersonName);
		Button buttonSubmitProfileBio = findViewById(R.id.user_profile_edit_bio_submit);
		buttonSubmitProfileBio.setVisibility(View.VISIBLE);
		editTextProfileBio.setEnabled(true);

		/* //TODO
		User user = new User();
		user.setEmail(this.<EditText>findViewById(R.id.activity_login_email).getText().toString());
		user.setPasswordHash(this.<EditText>findViewById(R.id.activity_login_password).getText().toString());

		HTTP_DRIVER.requestLogin(
				getBaseContext(),
				user,
				(response, context) -> {
					User responseUser = new Gson().fromJson(response.toString(), User.class);

					Toast.makeText(getBaseContext(), String.format("Welcome back, %s!", responseUser.getFirstName()), Toast.LENGTH_LONG).show();

					Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
					toProfile.putExtra("currentUserProfile", responseUser);
					startActivity(toProfile);
				}
		);
		 */
	}

	public void userProfileGotoMatchUser_onClick(View view) {
		//Intent toProfile = new Intent(getBaseContext(), SearchPeopleActivity.class);
		//startActivity(toProfile);

		Intent toProfile = new Intent(getBaseContext(), MatchPeopleActivity.class);
		toProfile.putExtra("currentUserProfile", user);
		startActivity(toProfile);
		//finish();

		//Intent gotoRegisterActivityIntent = new Intent(this, RegisterActivity.class);
		//startActivity(gotoRegisterActivityIntent);

	}

	public void btnUserProfile_menu_onClick(View view) {
		Toast.makeText( getBaseContext(), "Goto Feed!", Toast.LENGTH_LONG ).show();
	}

	public void btnUserProfile_settings_onClick(View view) {
		setContentView(R.layout.activity_profile_settings);
	}
	public void btnUserProfile_profile_OnClick(View view) {

	}


	public void btnUserProfile_interestCard_onClick(View view) {
		setContentView(R.layout.activity_search_interests);
	}

	public void btnUserProfile_groupCard_onClick(View view) {
		setContentView(R.layout.activity_search_groups);
	}


}
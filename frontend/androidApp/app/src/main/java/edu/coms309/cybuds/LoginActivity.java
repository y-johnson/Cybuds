package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import edu.coms309.cybuds.model.User;
import edu.coms309.cybuds.network.HTTPDriver;

public class LoginActivity extends AppCompatActivity {
	private static final HTTPDriver HTTP_DRIVER = new HTTPDriver();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void btnLogin_Back_onClick(View view) {
		finish();
	}

	/**
	 * On click, this method requests that the server authenticate the given information. The response should include information regarding the
	 * authenticated user.
	 *
	 * @param view the Activity view
	 */
	public void btnLogin_submit_onClick(View view) {
		User user = new User();
		user.setEmail(this.<EditText>findViewById(R.id.activity_login_email).getText().toString());
		user.setPasswordHash(this.<EditText>findViewById(R.id.activity_login_password).getText().toString());

		HTTP_DRIVER.requestLogin(
				getBaseContext(),
				user,
				(response, context) -> {
					User responseUser = new Gson().fromJson(response.toString(), User.class);
					Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
					toProfile.putExtra("currentUserProfile", responseUser);
					Toast.makeText(
							getBaseContext(),
							String.format("Welcome back, %s!", responseUser.getFirstName()),
							Toast.LENGTH_LONG
					).show();
					startActivity(toProfile);
				}
		);
	}
}
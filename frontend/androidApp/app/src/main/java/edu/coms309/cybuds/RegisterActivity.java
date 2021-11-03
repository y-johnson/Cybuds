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

public class RegisterActivity extends AppCompatActivity {
	private static final HTTPDriver HTTP_DRIVER = new HTTPDriver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
		@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
		startActivity(gotoProfileActivityIntent);
	}


	public void btnRegister_first_continue_onClick(View view) {
		setContentView(R.layout.activity_register_personal_info);
	}

	public void btnRegister_personalInfo_continue_onClick(View view) {
		setContentView(R.layout.activity_register_username_picture);
	}

	public void btnRegister_uName_Pict_contine_onClick(View view) {
		setContentView(R.layout.activity_register_classes);
	}

	public void btnRegister_classes_continue_onClick(View view) {
		setContentView(R.layout.setup_interest);
	}

	public void btnRegister_interests_contine_onClick(View view) {
		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
		startActivity(gotoProfileActivityIntent);
	}
	public void btnSubmit_onClick(View view) {
				//EditText idIn = findViewById(R.id.tbRegister_id);
		EditText usernameIn = findViewById(R.id.activity_register_username_picture_username);
		EditText emailIn = findViewById(R.id.activity_register_email);
		EditText passwordIn = findViewById(R.id.activity_register_password);
		EditText firstnameIn = findViewById(R.id.activity_register_personal_info_first_name);
		EditText middlenameIn = findViewById(R.id.activity_register_personal_info_middle_name);
		EditText lastnameIn = findViewById(R.id.activity_register_personal_info_last_name);
		EditText addressIn = findViewById(R.id.activity_register_personal_info_address);
		EditText phonenumberIn = findViewById(R.id.activity_register_personal_info_phone_number);
		EditText gradyearIn = findViewById(R.id.activity_register_graduation_year);


		User newUser = new User();
		newUser.setUsername(usernameIn.getText().toString());
		newUser.setEmail(emailIn.getText().toString());
		newUser.setPasswordHash(passwordIn.getText().toString());
		newUser.setFirstName(firstnameIn.getText().toString());
		newUser.setMiddleName(middlenameIn.getText().toString());
		newUser.setLastName(lastnameIn.getText().toString());
		newUser.setAddress(addressIn.getText().toString());
		newUser.setPhoneNumber(phonenumberIn.getText().toString());
		newUser.setGradYear(Integer.parseInt(gradyearIn.getText().toString()));

		HTTP_DRIVER.requestSignUp(
				getBaseContext(),
				newUser,
				(response, context) -> {
					User responseUser = new Gson().fromJson(response.toString(), User.class);

					Toast.makeText(
							getBaseContext(),
							String.format("Welcome to CyBuds, %s!", responseUser.getFirstName()),
							Toast.LENGTH_LONG
					).show();
					Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
					toProfile.putExtra("currentUserProfile", responseUser);
					startActivity(toProfile);
				}
		);

	}

}
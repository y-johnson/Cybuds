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

	EditText usernameIn;
	EditText emailIn;
	EditText passwordIn;
	EditText firstnameIn;
	EditText middlenameIn;
	EditText lastnameIn;
	EditText addressIn;
	EditText phonenumberIn;
	EditText gradyearIn;
	EditText classificationIn;
	EditText majorIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		usernameIn = findViewById(R.id.activity_register_username_picture_username);
		emailIn = findViewById(R.id.activity_register_email);
		passwordIn = findViewById(R.id.activity_register_password);
		firstnameIn = findViewById(R.id.activity_register_personal_info_first_name);
		middlenameIn = findViewById(R.id.activity_register_personal_info_middle_name);
		lastnameIn = findViewById(R.id.activity_register_personal_info_last_name);
		addressIn = findViewById(R.id.activity_register_personal_info_address);
		phonenumberIn = findViewById(R.id.activity_register_personal_info_phone_number);
		gradyearIn = findViewById(R.id.activity_register_graduation_year);
	}
		@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
		startActivity(gotoProfileActivityIntent);
	}


	public void btnRegister_first_continue_onClick(View view) {
		emailIn = findViewById(R.id.activity_register_email);
		passwordIn = findViewById(R.id.activity_register_password);
		setContentView(R.layout.activity_register_personal_info);
	}

	public void btnRegister_personalInfo_continue_onClick(View view) {
		firstnameIn = findViewById(R.id.activity_register_personal_info_first_name);
		middlenameIn = findViewById(R.id.activity_register_personal_info_middle_name);
		lastnameIn = findViewById(R.id.activity_register_personal_info_last_name);
		addressIn = findViewById(R.id.activity_register_personal_info_address);
		phonenumberIn = findViewById(R.id.activity_register_personal_info_phone_number);
		setContentView(R.layout.activity_register_username_picture);
	}

	public void btnRegister_uName_Pict_contine_onClick(View view) {
		usernameIn = findViewById(R.id.activity_register_username_picture_username);
		setContentView(R.layout.activity_register_classes);
		//btnSubmit_onClick(view);
	}

	public void btnRegister_classes_continue_onClick(View view) {
		//gradyearIn = findViewById(R.id.activity_register_graduation_year);
		classificationIn = findViewById(R.id.activity_register_graduation_year);
		majorIn = findViewById(R.id.activity_register_major);

		setContentView(R.layout.activity_register_groups);
	}

	public void btnRegister_groups_continue_onClick(View view) {
		setContentView(R.layout.activity_register_interests);
	}

	public void btnRegister_interests_contine_onClick(View view) {
		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
		startActivity(gotoProfileActivityIntent);
	}
	public void btnSubmit_onClick(View view) {
				//EditText idIn = findViewById(R.id.tbRegister_id);



		User newUser = new User();
		//Toast.makeText( getBaseContext(), String.format("Welcome to CyBuds, %s!", usernameIn.getText().toString()), Toast.LENGTH_LONG ).show();
		newUser.setUsername(usernameIn.getText().toString());
		newUser.setEmail(emailIn.getText().toString());
		newUser.setPasswordHash(passwordIn.getText().toString());
		newUser.setFirstName(firstnameIn.getText().toString());
		newUser.setMiddleName(middlenameIn.getText().toString());
		newUser.setLastName(lastnameIn.getText().toString());
		newUser.setAddress(addressIn.getText().toString());
		newUser.setPhoneNumber(phonenumberIn.getText().toString());
		newUser.setClassification(classificationIn.getText().toString());
		gradyearIn.getText().toString();
		newUser.setGradYear(0);
		/*//newUser.setUsername(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setEmail(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setPasswordHash(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		/*newUser.setFirstName(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setMiddleName(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setLastName(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setAddress(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setPhoneNumber(this.<EditText>findViewById(R.id.activity_register_email).getText().toString());
		newUser.setGradYear(7);*/

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
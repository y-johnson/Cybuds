package edu.coms309.cybuds;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.coms309.cybuds.model.User;
import edu.coms309.cybuds.network.HTTPDriver;

public class RegisterActivity extends AppCompatActivity {
	private static final HTTPDriver HTTP_DRIVER = new HTTPDriver();

	private static final String[] GenderSpinnerVals = {"Gender", "Male", "Female", "Other"};

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
	//EditText majorIn;
	EditText genderIn;
	Spinner genderSpinner;

	List<String> interests;

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

		// initiate a Switch
		Switch simpleSwitch = (Switch) findViewById(R.id.activity_register_username_picture_switch);

		// check current state of a Switch (true or false).
		Boolean switchState = simpleSwitch.isChecked();
		if (simpleSwitch.isChecked()) {
			//tell db user is premium
		} else {
			//tell db user is not premium
		}


	}
		@Override
	protected void onDestroy() {
			// initiate a Switch
			Switch simpleSwitch = (Switch) findViewById(R.id.activity_register_username_picture_switch);

			// check current state of a Switch (true or false).
			Boolean switchState = simpleSwitch.isChecked();
			if (simpleSwitch.isChecked()) {
				//tell db user is premium
			} else {
				//tell db user is not premium
			}

		super.onDestroy();
		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
		startActivity(gotoProfileActivityIntent);
	}


	public void btnRegister_first_continue_onClick(View view) {
		emailIn = findViewById(R.id.activity_register_email);
		passwordIn = findViewById(R.id.activity_register_password);
		setContentView(R.layout.activity_register_personal_info);
		genderSpinner = findViewById(R.id.spinner);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, GenderSpinnerVals );
		genderSpinner.setAdapter(spinnerArrayAdapter);


		/*String[] ar_dd_bus_type = getResources().getStringArray(R.array.ar_dd_bus_type);
		List<String> lst_bus_type = Arrays.asList(ar_dd_bus_type);
		ArrayList<String> ar_bus_type = new ArrayList<>(lst_bus_type);
		//==

		ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.activity_register_personal_info, R.id.spinner, ar_bus_type);
		adapter.setDropDownViewResource(R.layout
				.item_spinner_dropdown);
		//=========
		Spinner sp_my_spinner= rootView.findViewById(R.id.sp_my_spinner);
		sp_my_spinner.setAdapter(adapter);*/



		/*ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.emptyArray, R.layout.item_spinner_dropdown);

		adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
		genderSpinner.setAdapter(adapter);*/

	}

	public void btnRegister_photo_1_OnClick(View view){
		ImageView bigImage = findViewById(R.id.activity_register_username_picture_p5);
		bigImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage1));
		ImageView profileImage = findViewById(R.id.card_view_image);
		profileImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage1));
	}
	public void btnRegister_photo_2_OnClick(View view){
		ImageView bigImage = findViewById(R.id.activity_register_username_picture_p5);
		bigImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage2));
		ImageView profileImage = findViewById(R.id.card_view_image);
		profileImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage2));
	}
	public void btnRegister_photo_3_OnClick(View view){
		ImageView bigImage = findViewById(R.id.activity_register_username_picture_p5);
		bigImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage3));
		ImageView profileImage = findViewById(R.id.card_view_image);
		profileImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage3));
	}
	public void btnRegister_photo_4_OnClick(View view){
		ImageView bigImage = findViewById(R.id.activity_register_username_picture_p5);
		bigImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage4));
		ImageView profileImage = findViewById(R.id.card_view_image);
		profileImage.setImageDrawable(getResources().getDrawable(R.drawable.pimage4));
	}

	public void btnRegister_interests_add_OnClick(View view){
		interests.add(view.toString());
	}

	public void btnRegister_groups_add_OnClick(View view){
		interests.add(view.toString());
	}

	public void btnRegister_personalInfo_continue_onClick(View view) {
		firstnameIn = findViewById(R.id.activity_register_personal_info_first_name);
		middlenameIn = findViewById(R.id.activity_register_personal_info_middle_name);
		lastnameIn = findViewById(R.id.activity_register_personal_info_last_name);
		addressIn = findViewById(R.id.activity_register_personal_info_address);
		phonenumberIn = findViewById(R.id.activity_register_personal_info_phone_number);
		genderIn = findViewById(R.id.activity_register_personal_info_gender);
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
		//majorIn = findViewById(R.id.activity_register_major);

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
		newUser.setGender(Integer.parseInt(genderIn.getText().toString()));
		//gradyearIn.getText().toString();
		//newUser.setGradYear(0);
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
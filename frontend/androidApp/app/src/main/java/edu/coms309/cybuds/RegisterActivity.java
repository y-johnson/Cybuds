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

	public void btnSubmit_onClick(View view) {
		User newUser = new User();
		newUser.setUsername(this.<EditText>findViewById(R.id.tbRegister_UserName).getText().toString());
		newUser.setEmail(this.<EditText>findViewById(R.id.tbRegister_email).getText().toString());
		newUser.setPasswordHash(this.<EditText>findViewById(R.id.tbRegister_Password).getText().toString());
		newUser.setFirstName(this.<EditText>findViewById(R.id.tbRegister_fName).getText().toString());
		newUser.setMiddleName(this.<EditText>findViewById(R.id.tbRegister_mName).getText().toString());
		newUser.setLastName(this.<EditText>findViewById(R.id.tbRegister_lName).getText().toString());
		newUser.setAddress(this.<EditText>findViewById(R.id.tbRegister_address).getText().toString());
		newUser.setPhoneNumber(this.<EditText>findViewById(R.id.tbRegister_phoneNum).getText().toString());

		HTTP_DRIVER.requestSignUp(
				getBaseContext(),
				newUser,
				(response, context) -> {
					User responseUser = new Gson().fromJson(response.toString(), User.class);
					Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
					toProfile.putExtra("currentUserProfile", responseUser);
					Toast.makeText(
							getBaseContext(),
							String.format("Welcome to CyBuds, %s!", responseUser.getFirstName()),
							Toast.LENGTH_LONG
					).show();
					startActivity(toProfile);
				}
		);

	}

}
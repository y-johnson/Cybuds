package edu.coms309.cybuds;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import edu.coms309.cybuds.api.SlimCallback;
import edu.coms309.cybuds.model.User;

import static edu.coms309.cybuds.api.ApiClientFactory.GetUserApi;

public class ActivityRegister extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		TextView apiText1 = findViewById(R.id.tbRegister_textView);

		apiText1.setMovementMethod(new ScrollingMovementMethod());
		apiText1.setHeight(350);

		Button postByBodyBtn = findViewById(R.id.btnRegister_Submit);

		EditText idIn = findViewById(R.id.tbRegister_id);
		EditText usernameIn = findViewById(R.id.tbRegister_UserName);
		EditText emailIn = findViewById(R.id.tbRegister_email);
		EditText passwordIn = findViewById(R.id.tbRegister_Password);
		EditText firstnameIn = findViewById(R.id.tbRegister_fName);
		EditText middlenameIn = findViewById(R.id.tbRegister_mName);
		EditText lastnameIn = findViewById(R.id.tbRegister_lName);
		EditText addressIn = findViewById(R.id.tbRegister_address);
		EditText phonenumberIn = findViewById(R.id.tbRegister_phoneNum);
		EditText genderIn = findViewById(R.id.tbRegister_gender);

		//RegenerateAllUsersOnScreen(apiText1);

		postByBodyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				User newUser = new User();
				newUser.setUsername(usernameIn.getText().toString());
				newUser.setEmail(emailIn.getText().toString());
				newUser.setPasswordHash(passwordIn.getText().toString());
				newUser.setFirstName(firstnameIn.getText().toString());
				newUser.setMiddleName(middlenameIn.getText().toString());
				newUser.setLastName(lastnameIn.getText().toString());
				newUser.setAddress(addressIn.getText().toString());
				newUser.setPhoneNumber(phonenumberIn.getText().toString());
				newUser.setGender(genderIn.getText().toString());


				GetUserApi().addUser(newUser).enqueue(new SlimCallback<User>(user -> {
					RegenerateAllUsersOnScreen(apiText1);
					idIn.setText("");
					usernameIn.setText("");
					emailIn.setText("");
					passwordIn.setText("");
					firstnameIn.setText("");
					middlenameIn.setText("");
					lastnameIn.setText("");
					addressIn.setText("");
					phonenumberIn.setText("");
					genderIn.setText("");
				}));

				//path
               /* GetUserApi().getAllUsers(idIn.getText().toString(), usernameIn.getText().toString(),
                        emailIn.getText().toString(), passwordIn.getText().toString(),
                        passwordIn.getText().toString(), firstnameIn.getText().toString(),
                        middlenameIn.getText().toString(), lastnameIn.getText().toString(),
                        addressIn.getText().toString(), phonenumberIn.getText().toString(),
                        genderIn.getText().toString()).enqueue(new SlimCallback<User>(user->{
                            RegenerateAllUsersOnScreen(apiText1);
                            idIn.setText("");
                            usernameIn.setText("");
                            emailIn.setText("");
                            passwordIn.setText("");
                            firstnameIn.setText("");
                            middlenameIn.setText("");
                            lastnameIn.setText("");
                            addressIn.setText("");
                            phonenumberIn.setText("");
                            genderIn.setText("");
                }));*/
			}
		});
	}

	void RegenerateAllUsersOnScreen(TextView apiText1) {
		//User is of type List, this seems better but need to check.
		GetUserApi().getAllUsers().enqueue(new SlimCallback<List<User>>(users -> {
			apiText1.setText("");

			for (int i = users.size() - 1; i >= 0; i--) {
				apiText1.append(users.get(i).printable());
			}
		}, "GetAllUser"));

         /* //is of type iterable as defined in UserController.java
        GetUserApi().getAllUsers().enqueue(new SlimCallback<Iterable<User>>(users -> {
            apiText1.setText("");

            for (int i = users.size()-1; i >=0; i--){
                apiText1.append(users.get(i).printable());
            }
        }, "GetAllUser"));*/
	}

	public void btnRegister_Back_onClick(View view) {
		finish();
	}
}
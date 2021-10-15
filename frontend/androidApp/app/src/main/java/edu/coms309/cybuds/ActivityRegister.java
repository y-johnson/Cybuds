package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.coms309.cybuds.api.SlimCallback;
import edu.coms309.cybuds.model.User;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
//import test.connect.myapplication.api.SlimCallback;

public class ActivityRegister extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

//		TextView apiText1 = findViewById(R.id.activity_main_textView1);
//
//		apiText1.setMovementMethod(new ScrollingMovementMethod());
//		apiText1.setHeight(350);


	}

	public void btnSubmit_onClick(View view) {
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

		//my code
		RequestQueue queue = Volley.newRequestQueue(ActivityRegister.super.getBaseContext());
		String url = "http://coms-309-028.cs.iastate.edu:8080/users";

//				JSONObject post = new JSONObject();
//				try {
//					post.put("username", usernameIn.getText());
//					post.put("email", emailIn.getText());
//					post.put("passwordHash", passwordIn.getText());
//					post.put("firstName", firstnameIn.getText());
//					post.put("middleName", middlenameIn.getText());
//					post.put("lastName", lastnameIn.getText());
//					post.put("address", addressIn.getText());
//					post.put("number", phonenumberIn.getText());
//					post.put("gender", genderIn.getText());
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
		Gson gson = new Gson();
		String s = gson.toJson(newUser);
		try {
			JSONObject jsonObject = new JSONObject(s);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					//tb_password.setText("Good Response");
					//Toast.makeText(Login.this,"Success!", Toast.LENGTH_SHORT).show();
					try {
						System.out.printf("Yo");
						Gson gson = new Gson();
						User user = gson.fromJson(response.toString(), User.class);
						Intent toProfile = new Intent(ActivityRegister.super.getBaseContext(), ProfileActivity.class);
						toProfile.putExtra("currentUserProfile", user);
						Toast.makeText(ActivityRegister.this, "Success\r\nUser ID: " + response.getString("id"), Toast.LENGTH_LONG).show();
						startActivity(toProfile);
					} catch (JSONException e) {
						System.out.println("Nay");
						;
						finish();
						//e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					System.out.println("Nay!");
					;
					//tb_password.setText("POST didn't work!");
					Toast.makeText(ActivityRegister.this, "Fail!", Toast.LENGTH_SHORT).show();
				}
			});

			queue.add(jsonObjectRequest);
		} catch (JSONException e) {
			System.out.println("Nay!!");
			;

			e.printStackTrace();
		}

	}
//	void RegenerateAllUsersOnScreen(TextView apiText1) {
//		//User is of type List, this seems better but need to check.
//		GetUserApi().getAllUsers().enqueue(new SlimCallback<List<User>>(users -> {
//			apiText1.setText("");
//
//			for (int i = users.size() - 1; i >= 0; i--) {
//				apiText1.append(users.get(i).printable());
//			}
//		}, "GetAllUser"));
//
//         /* //is of type iterable as defined in UserController.java
//        GetUserApi().getAllUsers().enqueue(new SlimCallback<Iterable<User>>(users -> {
//            apiText1.setText("");
//
//            for (int i = users.size()-1; i >=0; i--){
//                apiText1.append(users.get(i).printable());
//            }
//        }, "GetAllUser"));*/
//	}

}
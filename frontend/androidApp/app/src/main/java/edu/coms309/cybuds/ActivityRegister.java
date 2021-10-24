//package edu.coms309.cybuds;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import androidx.appcompat.app.AppCompatActivity;
//import edu.coms309.cybuds.model.User;
////import test.connect.myapplication.api.SlimCallback;
//
//public class ActivityRegister extends AppCompatActivity {
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_register);
//
////		TextView apiText1 = findViewById(R.id.activity_main_textView1);
////
////		apiText1.setMovementMethod(new ScrollingMovementMethod());
////		apiText1.setHeight(350);
//
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
//		startActivity(gotoProfileActivityIntent);
//	}
//
//	public void btnRegister_Back_onClick(View view) {
//		finish();
//	}
//
//	public void btnRegister_first_continue_onClick(View view) {
//		setContentView(R.layout.activity_register_personal_info);
//	}
//
//	public void btnRegister_personalInfo_continue_onClick(View view) {
//		setContentView(R.layout.activity_register_username_picture);
//	}
//
//	public void btnRegister_uName_Pict_contine_onClick(View view) {
//		setContentView(R.layout.activity_register_classes);
//	}
//
//	public void btnRegister_classes_continue_onClick(View view) {
//		setContentView(R.layout.setup_interest);
//	}
//
//	public void btnRegister_interests_contine_onClick(View view) {
//		Intent gotoProfileActivityIntent = new Intent(this, ProfileActivity.class);
//		startActivity(gotoProfileActivityIntent);
//	}
//
//	public void btnSubmit_onClick(View view) {
//		//EditText idIn = findViewById(R.id.tbRegister_id);
//		EditText usernameIn = findViewById(R.id.activity_register_username_picture_username);
//		EditText emailIn = findViewById(R.id.activity_register_email);
//		EditText passwordIn = findViewById(R.id.activity_register_password);
//		EditText firstnameIn = findViewById(R.id.activity_register_personal_info_first_name);
//		EditText middlenameIn = findViewById(R.id.activity_register_personal_info_middle_name);
//		EditText lastnameIn = findViewById(R.id.activity_register_personal_info_last_name);
//		EditText addressIn = findViewById(R.id.activity_register_personal_info_address);
//		EditText phonenumberIn = findViewById(R.id.activity_register_personal_info_phone_number);
//
//		User newUser = new User();
//		newUser.setUsername(usernameIn.getText().toString());
//		newUser.setEmail(emailIn.getText().toString());
//		newUser.setPasswordHash(passwordIn.getText().toString());
//		newUser.setFirstName(firstnameIn.getText().toString());
//		newUser.setMiddleName(middlenameIn.getText().toString());
//		newUser.setLastName(lastnameIn.getText().toString());
//		newUser.setAddress(addressIn.getText().toString());
//		newUser.setPhoneNumber(phonenumberIn.getText().toString());
//
//		//my code
//		RequestQueue queue = Volley.newRequestQueue(ActivityRegister.super.getBaseContext());
//		String url = "http://coms-309-028.cs.iastate.edu:8080/users";
//
////				JSONObject post = new JSONObject();
////				try {
////					post.put("username", usernameIn.getText());
////					post.put("email", emailIn.getText());
////					post.put("passwordHash", passwordIn.getText());
////					post.put("firstName", firstnameIn.getText());
////					post.put("middleName", middlenameIn.getText());
////					post.put("lastName", lastnameIn.getText());
////					post.put("address", addressIn.getText());
////					post.put("number", phonenumberIn.getText());
////					post.put("gender", genderIn.getText());
////				} catch (JSONException e) {
////					e.printStackTrace();
////				}
//		Gson gson = new Gson();
//		String s = gson.toJson(newUser);
//		try {
//			JSONObject jsonObject = new JSONObject(s);
//			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//				@Override
//				public void onResponse(JSONObject response) {
//					//tb_password.setText("Good Response");
//					//Toast.makeText(Login.this,"Success!", Toast.LENGTH_SHORT).show();
//					try {
//						System.out.printf("Yo");
//						Gson gson = new Gson();
//						User user = gson.fromJson(response.toString(), User.class);
//						Intent toProfile = new Intent(ActivityRegister.super.getBaseContext(), ProfileActivity.class);
//						toProfile.putExtra("currentUserProfile", user);
//						Toast.makeText(ActivityRegister.this, "Success\r\nUser ID: " + response.getString("id"), Toast.LENGTH_LONG).show();
//						startActivity(toProfile);
//					} catch (JSONException e) {
//						System.out.println("Nay");
//						;
//						finish();
//						//e.printStackTrace();
//					}
//				}
//			}, new Response.ErrorListener() {
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					System.out.println("Nay!");
//					;
//					//tb_password.setText("POST didn't work!");
//					Toast.makeText(ActivityRegister.this, "Fail!", Toast.LENGTH_SHORT).show();
//				}
//			});
//
//			queue.add(jsonObjectRequest);
//		} catch (JSONException e) {
//			System.out.println("Nay!!");
//			;
//
//			e.printStackTrace();
//		}
//
//	}
////	void RegenerateAllUsersOnScreen(TextView apiText1) {
////		//User is of type List, this seems better but need to check.
////		GetUserApi().getAllUsers().enqueue(new SlimCallback<List<User>>(users -> {
////			apiText1.setText("");
////
////			for (int i = users.size() - 1; i >= 0; i--) {
////				apiText1.append(users.get(i).printable());
////			}
////		}, "GetAllUser"));
////
////         /* //is of type iterable as defined in UserController.java
////        GetUserApi().getAllUsers().enqueue(new SlimCallback<Iterable<User>>(users -> {
////            apiText1.setText("");
////
////            for (int i = users.size()-1; i >=0; i--){
////                apiText1.append(users.get(i).printable());
////            }
////        }, "GetAllUser"));*/
////	}
//
//}
package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void btnLogin_onClick(View view) {
		Intent gotoLoginActivityIntent = new Intent(this, LoginActivity.class);
		startActivity(gotoLoginActivityIntent);
	}

	public void btnSignUp_onClick(View view) {
		//Toast.makeText(MainActivity.this, "TODO!", Toast.LENGTH_SHORT).show();
		Intent gotoRegisterActivityIntent = new Intent(this, RegisterActivity.class);
		startActivity(gotoRegisterActivityIntent);
	}


}
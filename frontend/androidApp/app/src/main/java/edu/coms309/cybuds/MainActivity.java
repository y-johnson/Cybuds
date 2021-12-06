package edu.coms309.cybuds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.coms309.cybuds.ui.AdapterRecyclerView;

public class MainActivity extends AppCompatActivity {
//	RecyclerView datalist;
//	List<String> titles;
//	AdapterRecyclerView adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView title = (TextView) findViewById(R.id.activity_main_group_name);
		title.setText("Get Group's Name"); //calls db and retrieves random group name
		TextView description = (TextView) findViewById(R.id.activity_main_short_description);
		description.setText("Get Group's Description"); //calls db and retrieves the above group names description
		//Group picture element not implemented

//		datalist = findViewById(R.id.activity_register_interests_recycler_view);
//
//		titles = new ArrayList<>();
//		titles.add("Engineering");
//		titles.add("Business");
//		titles.add("Design");
//		titles.add("LAS");
//		titles.add("Agriculture and Life Sciences");
//		titles.add("Vetinary Medicine");
//
//		adapter = new AdapterRecyclerView(this, titles);
//
//		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false);
//		datalist.setLayoutManager(gridLayoutManager);
//		datalist.setAdapter(adapter);

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
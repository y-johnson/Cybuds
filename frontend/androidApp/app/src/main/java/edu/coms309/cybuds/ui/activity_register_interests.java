package edu.coms309.cybuds.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.coms309.cybuds.R;

public class activity_register_interests extends AppCompatActivity {

    RecyclerView dataList;
    List<String> titles;
    AdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interests);

        dataList = findViewById(R.id.activity_register_interests_recycler_view);

        titles = new ArrayList<>();
        titles.add("Engineering");
        titles.add("Business");
        titles.add("Design");
        titles.add("LAS");
        titles.add("Agriculture and Life Sciences");
        titles.add("Veterinary Medicine");

        adapter = new AdapterRecyclerView(this, titles);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }
}
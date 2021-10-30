package edu.coms309.cybuds.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.coms309.cybuds.R;

public class activity_register_groups extends AppCompatActivity {
    RecyclerView datalist;
    List<String> titles;
    List<String> description;
    AdapterRecyclerViewGroups adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datalist = findViewById(R.id.activity_register_interests_recycler_view);

        titles = new ArrayList<>();
        titles.add("StuDogs");
        titles.add("RSS");
        titles.add("LOL");
        titles.add("Dab-N-Drab");
        titles.add("Maths");
        titles.add("¯l_(ツ)_|¯");
        // titles.add("La Di Da");

        description = new ArrayList<>();
        description.add("We love to study with dogs");
        description.add("We love to read sad stories");
        description.add("We live to laugh");
        description.add("We live to paint");
        description.add("We do maths");
        description.add("We do nothing");

        adapter = new AdapterRecyclerViewGroups(this, titles, description);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false);
        datalist.setLayoutManager(gridLayoutManager);
        datalist.setAdapter(adapter);
    }
}
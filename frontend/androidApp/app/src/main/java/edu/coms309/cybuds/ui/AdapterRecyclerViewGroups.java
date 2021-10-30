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

public class AdapterRecyclerViewGroups extends RecyclerView.Adapter<AdapterRecyclerViewGroups.ViewHolder> {

    List<String> titles;
    List<String> description;
    LayoutInflater inflater;

    public AdapterRecyclerViewGroups(Context ctx, List<String> titles, List<String> description){
        this.titles = titles;
        this.description = description;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.groups_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.description.setText(description.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.activity_register_groups_card_title);
            description = itemView.findViewById(R.id.activity_register_groups_card_description);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    //todo 17:40
                }
            });
        }
    }
}


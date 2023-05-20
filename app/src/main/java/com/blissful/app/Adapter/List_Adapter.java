package com.blissful.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blissful.app.Activity.player_activity;
import com.blissful.app.Model.List_Model;
import com.blissful.app.R;
import java.util.ArrayList;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.ViewHolder> {

    private final Context context;
    private final ArrayList<List_Model> courseModelArrayList;

    // Constructor
    public List_Adapter(Context context, ArrayList<List_Model> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List_Adapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout

        String name = courseModelArrayList.get(position).getCourse_name();
        String url = courseModelArrayList.get(position).getVideoUrl();
        List_Model model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());

     //   holder.courseIV.setImageResource(model.getCourse_image());
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(v.getContext(), "My Item position: " + String.valueOf(name) , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), player_activity.class);
                i.putExtra("name", name);
                i.putExtra("url", url);
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return courseModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
     //   private final ImageView courseIV;
        private final TextView courseNameTV;
       // private final TextView courseRatingTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //  courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
         //   courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
        }
    }
}
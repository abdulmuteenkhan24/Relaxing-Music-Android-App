package com.blissful.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blissful.app.Model.Albums_Model;
import com.blissful.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Albums_Adapter extends ArrayAdapter<Albums_Model> {

    public Albums_Adapter(@NonNull Context context, ArrayList<Albums_Model> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_view, parent, false);
        }

        Albums_Model courseModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);

        courseTV.setText(courseModel.getCourse_name());
        try {
            Picasso.get()
                    .load(courseModel.getImgid())
                    .placeholder(R.drawable.logo)
                    .error(android.R.drawable.stat_notify_error)
                    .into(courseIV);
        } catch (Exception ex) {

        }

        return listitemView;


    }

}



package com.noideastudios.hrms;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CandidateAdapter extends ArrayAdapter<Candidate> {

    public CandidateAdapter(@NonNull Context context, int resource, ArrayList<Candidate> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.candidate_tile, parent, false);

        TextView name = convertView.findViewById(R.id.nameTile);
        TextView phone = convertView.findViewById(R.id.phoneTile);
        TextView status = convertView.findViewById(R.id.statusTile);
        LinearLayout tile = convertView.findViewById(R.id.tile);

        Candidate candidate = getItem(position);

        if (candidate != null) {
            name.setText(candidate.getName());
            phone.setText(candidate.getPhone());
            status.setText(candidate.getStatus());
            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return convertView;
    }
}

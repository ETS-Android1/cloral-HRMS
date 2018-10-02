package com.noideastudios.hrms;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class AllApplicationsActivity extends AppCompatActivity {

    DBhandler dBhandler;
    Button sortBy;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ListView oldListView;
    ArrayList<Candidate> arrayList;
    CandidateAdapter candidateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_applications);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("All Applications");
        }
        setUpView();
    }

    private void setUpView() {
        dBhandler = new DBhandler(this, null, null, 1);
        oldListView = findViewById(R.id.oldCandidateList);
        arrayList = dBhandler.returnCandidates(0);
        candidateAdapter = new CandidateAdapter(
                AllApplicationsActivity.this, R.layout.candidate_tile, arrayList);
        oldListView.setAdapter(candidateAdapter);

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBhandler.deleteAll();
                candidateAdapter.clear();
            }
        });

        sortBy = findViewById(R.id.sortbybutton);
        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpSortBy();
            }
        });
    }

    private void setUpSortBy() {
        final View sortDialog = getLayoutInflater().inflate(R.layout.sortby, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(AllApplicationsActivity.this);
        builder.setView(sortDialog);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        radioGroup = sortDialog.findViewById(R.id.sortByRG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = sortDialog.findViewById(checkedId);
                Toast.makeText(AllApplicationsActivity.this,
                        "Sorting by " + radioButton.getText().toString(),
                        Toast.LENGTH_LONG).show();

                oldListView = findViewById(R.id.oldCandidateList);
                switch (radioButton.getText().toString()) {
                    case "Status":
                        arrayList = dBhandler.returnCandidates(100);
                        break;
                    case "Alphabetically":
                        arrayList = dBhandler.returnCandidates(200);
                        break;
                    case "Position Applied For":
                        arrayList = dBhandler.returnCandidates(300);
                        break;
                    case "Time":
                    default:
                        arrayList = dBhandler.returnCandidates(0);
                        break;

                }
                candidateAdapter = new CandidateAdapter(
                        AllApplicationsActivity.this, R.layout.candidate_tile, arrayList);
                oldListView.setAdapter(candidateAdapter);

                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

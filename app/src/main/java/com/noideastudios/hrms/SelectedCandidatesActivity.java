package com.noideastudios.hrms;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectedCandidatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_candidates);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Selected Candidates");
        }
        setUpList();
    }

    private void setUpList() {
        DBhandler dBhandler = new DBhandler(this, null, null, 1);
        ListView selectedListView = findViewById(R.id.selectedCandidateList);
        ArrayList<Candidate> arrayList = dBhandler.returnCandidates(1);
        CandidateAdapter candidateAdapter = new CandidateAdapter(this, R.layout.candidate_tile, arrayList);
        selectedListView.setAdapter(candidateAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

package com.noideastudios.hrms;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class AllApplicationsActivity extends AppCompatActivity {

    public static CandidateAdapter candidateAdapter;
    public static ListView oldListView;
    DBhandler dBhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_applications);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("All Applications");
        }
        setUpList();
    }

    private void setUpList() {
        dBhandler = new DBhandler(this, null, null, 1);
        oldListView = findViewById(R.id.oldCandidateList);
        ArrayList<Candidate> arrayList = dBhandler.returnCandidates(0);
        candidateAdapter = new CandidateAdapter(this, R.layout.candidate_tile, arrayList);
        oldListView.setAdapter(candidateAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

package com.noideastudios.hrms;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllApplicationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DBhandler dBhandler;
    Button sortBy, push, retrieve;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ListView oldListView;
    ArrayList<Candidate> arrayList;
    CandidateAdapter candidateAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    ProgressBar progressBar;

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
        getValues();

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

        push = findViewById(R.id.push);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushToRD();
            }
        });

        retrieve = findViewById(R.id.retrieve);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childEventListener = null;
                retrieveFromRD();
            }
        });
    }

    private void getValues() {
        arrayList = dBhandler.returnCandidates(0);
        candidateAdapter = new CandidateAdapter(
                AllApplicationsActivity.this, R.layout.candidate_tile, arrayList);
        oldListView.setAdapter(candidateAdapter);
        oldListView.setOnItemClickListener(this);
    }

    private void retrieveFromRD() {
        final View pullDialog = getLayoutInflater().inflate(R.layout.request_key, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AllApplicationsActivity.this);
        builder.setView(pullDialog);

        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        progressBar = pullDialog.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        final Button key_proceed = pullDialog.findViewById(R.id.key_proceed);
        key_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key_proceed.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                TextInputEditText keyTIET = pullDialog.findViewById(R.id.Key);
                final String key = keyTIET.getText().toString();
                if (!key.isEmpty()) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(key)) {
                                key_proceed.setClickable(true);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AllApplicationsActivity.this, "Key does not exist!", Toast.LENGTH_LONG).show();
                            } else {
                                if (childEventListener == null) {
                                    childEventListener = new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Candidate candidate = dataSnapshot.getValue(Candidate.class);
                                            dBhandler.addCandidate(candidate);
                                            dialog.dismiss();
                                            getValues();
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(AllApplicationsActivity.this, "Retrieval Successful!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };
                                }
                                databaseReference.child(key).addChildEventListener(childEventListener);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    getValues();
                } else {
                    progressBar.setVisibility(View.GONE);
                    key_proceed.setClickable(true);
                    Toast.makeText(AllApplicationsActivity.this, "Key cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        key_proceed.setClickable(true);
    }

    private void pushToRD() {
        final View pushDialog = getLayoutInflater().inflate(R.layout.request_key, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AllApplicationsActivity.this);
        builder.setView(pushDialog);

        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        progressBar = pushDialog.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        final Button key_proceed = pushDialog.findViewById(R.id.key_proceed);
        key_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                key_proceed.setClickable(false);
                TextInputEditText keyTIET = pushDialog.findViewById(R.id.Key);
                String key = keyTIET.getText().toString();
                if (!key.isEmpty()) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child(key);
                    if (childEventListener != null)
                        databaseReference.removeEventListener(childEventListener);

                    Map<String, Candidate> sample = new HashMap<>();
                    for (Candidate entry : arrayList) {
                        sample.put(String.valueOf(entry.getId()), entry);
                    }
                    databaseReference.setValue(sample);
                    Toast.makeText(AllApplicationsActivity.this, "Push successful!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    progressBar.setVisibility(View.GONE);
                    key_proceed.setClickable(true);
                    Toast.makeText(AllApplicationsActivity.this, "Key cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        key_proceed.setClickable(true);
    }

    private void setUpSortBy() {
        final View sortDialog = getLayoutInflater().inflate(R.layout.sortby, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(AllApplicationsActivity.this);
        builder.setView(sortDialog);

        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(AllApplicationsActivity.this, CandidateDetailsActivity.class);
        intent.putExtra("employee_id", arrayList.get(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpView();
    }
}

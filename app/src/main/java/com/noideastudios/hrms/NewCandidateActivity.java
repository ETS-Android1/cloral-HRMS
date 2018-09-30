package com.noideastudios.hrms;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class NewCandidateActivity extends AppCompatActivity {

    DBhandler dBhandler;
    TextInputEditText cName, cPhone, cPos;
    Spinner cStatus;
    Button proceed, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_candidate);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Add New Candidate");
        }

        cStatus = findViewById(R.id.status);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.statusSpinnner));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cStatus.setAdapter(arrayAdapter);
        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDB();
            }
        });
    }

    private void setUpDB() {
        dBhandler = new DBhandler(this, null, null, 1);

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBhandler.deleteAll();
            }
        });

        cName = findViewById(R.id.name);
        cPhone = findViewById(R.id.phone);
        cPos = findViewById(R.id.position);
        cName.setError(null);
        cPhone.setError(null);
        cPos.setError(null);

        if (TextUtils.isEmpty(cName.getText()) || TextUtils.isEmpty(cPhone.getText()) || TextUtils.isEmpty(cPos.getText())) {
            if (TextUtils.isEmpty(cName.getText()))
                cName.setError("Field cannot be empty!");
            if (TextUtils.isEmpty(cPhone.getText()))
                cPhone.setError("Field cannot be empty!");
            if (TextUtils.isEmpty(cPos.getText()))
                cPos.setError("Field cannot be empty!");
        } else {
            Candidate candidate = new Candidate(
                    cName.getText().toString(),
                    cPhone.getText().toString(),
                    cPos.getText().toString(),
                    cStatus.getSelectedItem().toString());
            dBhandler.addCandidate(candidate);
            Toast.makeText(NewCandidateActivity.this, cName.getText().toString() + " added!", Toast.LENGTH_LONG).show();
            cName.setError(null);
            cPhone.setError(null);
            cPos.setError(null);
            cName.setText("");
            cPhone.setText("");
            cPos.setText("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

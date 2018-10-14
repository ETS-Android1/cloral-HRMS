package com.noideastudios.hrms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.noideastudios.hrms.Functions.BitmapToUri;
import static com.noideastudios.hrms.Functions.getResizedBitmap;

public class CandidateDetailsActivity extends AppCompatActivity {

    DBhandler dBhandler;
    Candidate candidate;
    Button save, reset;
    int id;
    CircleImageView imageView;
    TextView Name, Phone, Position, Resume;
    Spinner Status;
    String name, phone, position, status, uri;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Candidate Details");
        }

        Intent intent = getIntent();
        if (intent != null)
            id = intent.getIntExtra("employee_id", 0);

        dBhandler = new DBhandler(this, null, null, 1);

        Name = findViewById(R.id.nameDetail);
        Phone = findViewById(R.id.phoneDetail);
        Position = findViewById(R.id.positionDetail);
        Status = findViewById(R.id.statusDetail);
        imageView = findViewById(R.id.imageDetail);
        Resume = findViewById(R.id.resumeDetail);
        setView();
        setUpEdit();
    }

    private void setView() {

        candidate = dBhandler.returnCandidate(id);
        openResume();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item,
                getResources().getStringArray(R.array.statusSpinnner));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Status.setAdapter(arrayAdapter);
        switch (candidate.getStatus()) {
            case "Selected":
                Status.setSelection(0);
                break;
            case "Rejected":
                Status.setSelection(1);
                break;
            case "In Process":
                Status.setSelection(2);
                break;
            case "Interview":
                Status.setSelection(3);
                break;
        }

        Name.setText(candidate.getName());
        Phone.setText(candidate.getPhone());
        Position.setText(candidate.getPosition());
        Picasso.with(this)
                .load(candidate.getPhotoURI())
                .error(R.drawable.employee_tie)
                .into(imageView);
        uri = candidate.getPhotoURI();
    }

    public void setUpEdit() {
        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoURI = uploadPic();
            }
        });
        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name.setEnabled(true);
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phone.setEnabled(true);
                Phone.setInputType(InputType.TYPE_CLASS_PHONE);
                Phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            }
        });
        Position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position.setEnabled(true);
            }
        });


        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = Name.getText().toString();
                phone = Phone.getText().toString();
                position = Position.getText().toString();
                status = Status.getSelectedItem().toString();
                uri = String.valueOf(photoURI);
                dBhandler.updateCandidate(id, name, phone, position, status, uri);
                Toast.makeText(CandidateDetailsActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();

                setView();
            }
        });

    }

    private Uri uploadPic() {
        PickSetup setup = new PickSetup()
                .setTitle("Pick your choice!")
                .setSystemDialog(true);

        PickImageDialog.build(setup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        Bitmap compressed = getResizedBitmap(r.getBitmap(), 1000);
                        photoURI = BitmapToUri(CandidateDetailsActivity.this, compressed);
                        Picasso.with(CandidateDetailsActivity.this)
                                .load(photoURI)
                                .error(R.drawable.employee_tie)
                                .into(imageView);
                    }
                })
                .setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        Toast.makeText(getApplicationContext(),
                                "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show(CandidateDetailsActivity.this);
        return photoURI;
    }

    private void openResume() {
        String resumeUri = candidate.getResumeURI();
        String displayName = "N/A";
        if (resumeUri == null) {
            File file = new File(resumeUri);
            displayName = "View";
            Resume.setText(displayName);
            Resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(candidate.getResumeURI()), "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 200);
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

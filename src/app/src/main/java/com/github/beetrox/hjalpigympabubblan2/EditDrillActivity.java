package com.github.beetrox.hjalpigympabubblan2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EditDrillActivity extends AppCompatActivity {

    private Button btnSelect, btnUpload;
    private ImageView imageView;
    private EditText drillNameEditText;
    private EditText drillDescriptionEditText;
    //    private EditText drillTags;
    private AutoCompleteTextView tagsTextView;
    private ListView tagsListView;
    ArrayList<String> categories;
    ArrayList<String> availableTags;
    ArrayList<String> drillTags;
    Spinner categorySpinner;
    Intent intent;
    String userId;

    Drill drill;

    String drillId;
    String drillName;
    String drillDescription;
    String drillCategory;
    String imageUrl;

    TextView drillNameTextView;
    TextView drillDescriptionTextView;
    ImageView drillImageView;
    ImageView favouriteImageView;

    ArrayAdapter<String> tagsListViewAdapter;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference drillReference;
    DatabaseReference tagReference;

    boolean nameSelected;
    boolean descriptionSelected;
    boolean tagsSelected;
    boolean categorySelected;
    boolean imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgViewUpload);
        drillNameEditText = findViewById(R.id.editTextName);
        drillDescriptionEditText = findViewById(R.id.editTextDescription);
//        drillTags = findViewById(R.id.editTextTags);
        categorySpinner = findViewById(R.id.categorySpinner);
        tagsTextView = findViewById(R.id.tagsTextView);
        tagsListView = findViewById(R.id.tagsListView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tagReference = firebaseDatabase.getReference().child("tags");

        nameSelected = false;
        descriptionSelected = false;
        tagsSelected = false;
        categorySelected = false;
        imageSelected = false;

        drillTags = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get the value based on the key
            drillId = extras.getString("drillId");
            drillCategory = extras.getString("drillCategory");
        }

        drillReference = firebaseDatabase.getReference().child("drills").child(drillCategory).child(drillId);

        categorySpinner.setEnabled(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

// Attach a listener to read the data at our posts reference
        drillReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drill = dataSnapshot.getValue(Drill.class);
//                System.out.println(drill.getName());
                        drillNameEditText.setText(drill.getName());
                        drillDescriptionEditText.setText(drill.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // find all tags and add to list

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replace old text on firebase
                drill.setName(drillNameEditText.getText().toString());
                drill.setDescription(drillDescriptionEditText.getText().toString());
                drillReference.setValue(drill).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}

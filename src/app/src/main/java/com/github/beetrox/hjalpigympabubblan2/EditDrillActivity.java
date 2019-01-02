package com.github.beetrox.hjalpigympabubblan2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

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
    List<String> drillTags;
    List<String> oldTags;
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
        oldTags = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drillId = extras.getString("drillId");
            drillCategory = extras.getString("drillCategory");
        }

        drillReference = firebaseDatabase.getReference().child("drills").child(drillCategory).child(drillId);

        categorySpinner.setEnabled(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        CreateTagsList();
        SetTagsTextViewAdapter();
        SetTagListViewAdapter();

// Attach a listener to read the data at our posts reference
        drillReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drill = dataSnapshot.getValue(Drill.class);
//                System.out.println(drill.getName());
                        drillNameEditText.setText(drill.getName());
                        drillDescriptionEditText.setText(drill.getDescription());
                        drillTags.clear();
                        drillTags.addAll(drill.getTags());
                        oldTags.addAll(drill.getTags());
//                        System.out.println(drillTags);
                        tagsListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // find all tags and add to list
        // change picture

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replace old text on firebase
                RemoveOldTags();
                AddNewTags();
                drill.setName(drillNameEditText.getText().toString());
                drill.setDescription(drillDescriptionEditText.getText().toString());
                drill.setTags(drillTags);
                drillReference.setValue(drill).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        tagsTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = adapterView.getItemAtPosition(i).toString();
                tagsListViewAdapter.notifyDataSetChanged();
                boolean tagExists = CheckDuplicate(tag);
                if(!tagExists) {
                    drillTags.add(tag);
//                    CheckEnableUpload();
                }
                tagsTextView.setText("");
            }
        });

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drillTags.remove(i);
//                CheckEnableUpload();
                tagsListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void SetTagListViewAdapter() {
        tagsListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drillTags);
        tagsListViewAdapter.setNotifyOnChange(true);
        tagsListView.setAdapter(tagsListViewAdapter);
    }

    private void SetTagsTextViewAdapter() {
        ArrayAdapter<String> tagsTextViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, availableTags);
        tagsTextViewAdapter.setNotifyOnChange(true);
        tagsTextView.setAdapter(tagsTextViewAdapter);
    }

    private boolean CheckDuplicate(String tag) {
        for(int i=0; i<drillTags.size(); i++) {
            if(tag.equals(drillTags.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void RemoveOldTags() {
        for(int i=0; i<oldTags.size(); i++) {
            String oldTag = oldTags.get(i);
            boolean tagExists = false;

            for(int j=0; j<drillTags.size(); j++) {
                String drillTag = drillTags.get(j);
                if (oldTag.equals(drillTag)) {
                    tagExists = true;
                }
            }

            if (!tagExists) {
                tagReference.child(drillCategory).child(oldTag.toLowerCase()).child(drillId).removeValue();
            }
        }
    }

    private void AddNewTags() {
        for (int i=0; i<drillTags.size(); i++) {
            String drillTag = drillTags.get(i);
            boolean addTag = true;
            for (int j=0; j<oldTags.size(); j++) {
                String oldTag = oldTags.get(j);
                if (drillTag.equals(oldTag)) {
                    addTag = false;
                }
            }
            if (addTag) {
                tagReference.child(drillCategory).child(drillTag.toLowerCase()).child(drillId).setValue(true);
            }
        }
    }

    public void CreateTagsList() {
        availableTags = new ArrayList<>();
        //tumbling
        availableTags.add("tumbling");
        availableTags.add("rondat");
        availableTags.add("flickis");
        availableTags.add("whipback");
        availableTags.add("handvolt");
        availableTags.add("spagathandvolt");
        availableTags.add("handvolt 2");
        availableTags.add("salto");
        availableTags.add("dubbelsalto");
        availableTags.add("frivolt tumbling");
        availableTags.add("voltstart");
        availableTags.add("helstart");
        availableTags.add("bakåtskruv");
        availableTags.add("handstående");
        availableTags.add("hjulning");
        availableTags.add("framåtkullerbytta");
        availableTags.add("bakåtkullerbytta");
        //trampet
        availableTags.add("trampett");
        availableTags.add("frivolt trampett");
        availableTags.add("tsukahara");
        availableTags.add("dubbel frivolt");
        availableTags.add("överslag");
        availableTags.add("överslag skruv");
        availableTags.add("överslag volt");
        availableTags.add("petrik");
        //both
        availableTags.add("framåtskruv");
        availableTags.add("inhopp");
        //floor
        availableTags.add("fristående");
        availableTags.add("piruett");
        availableTags.add("balans");
        availableTags.add("hopp");
        availableTags.add("piksitt");
        availableTags.add("spetspik");
        availableTags.add("hjulning flickis");
        //other
        availableTags.add("löpträning");
        availableTags.add("stretch");
        //strength
        availableTags.add("parstyrka");
        availableTags.add("styrka med redskap");
    }
}

package com.github.beetrox.hjalpigympabubblan2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private Button btnSelect, btnUpload;
    private ImageView imageView;
    private EditText drillName;
    private EditText drillDescription;
//    private EditText drillTags;
    private AutoCompleteTextView tagsTextView;
    private ListView tagsListView;
    ArrayList<String> categories;
    ArrayList<String> availableTags;
    ArrayList<String> drillTags;
    Spinner categorySpinner;
    Intent intent;
    String userId;

    ArrayAdapter<String> tagsListViewAdapter;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
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

//        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgViewUpload);
        drillName = findViewById(R.id.editTextName);
        drillDescription = findViewById(R.id.editTextDescription);
//        drillTags = findViewById(R.id.editTextTags);
        categorySpinner = findViewById(R.id.categorySpinner);
        tagsTextView = findViewById(R.id.tagsTextView);
        tagsListView = findViewById(R.id.tagsListView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        drillReference = firebaseDatabase.getReference().child("drills");
        tagReference = firebaseDatabase.getReference().child("tags");

        nameSelected = false;
        descriptionSelected = false;
        tagsSelected = false;
        categorySelected = false;
        imageSelected = false;

        drillTags = new ArrayList<>();

        CreateTagsList();
        SetTagsTextViewAdapter();
        SetTagListViewAdapter();

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        Menu menu = navigation.getMenu();
//        MenuItem menuItem = menu.getItem(3);
//        menuItem.setChecked(true);

        btnUpload.setEnabled(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            userId = user.getUid();
        }

        categories = new ArrayList<>();
        PopulateCategories();

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
//
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImage();
//            }
//        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Also calling CreateDrill from inside UploadImage.
                UploadImage();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    categorySelected = true;
                } else {
                    categorySelected = false;
                }
                CheckEnableUpload();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        drillName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==0) {
                    nameSelected = false;
                    CheckEnableUpload();
                } else {
                    nameSelected = true;
                    CheckEnableUpload();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        drillDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==0) {
                    descriptionSelected = false;
                    CheckEnableUpload();
                } else {
                    descriptionSelected = true;
                    CheckEnableUpload();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    CheckEnableUpload();
                }
                tagsTextView.setText("");
            }
        });

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drillTags.remove(i);
                CheckEnableUpload();
                tagsListViewAdapter.notifyDataSetChanged();
            }
        });

//        drillTags.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().trim().length()==0) {
//                    tagsSelected = false;
//                    CheckEnableUpload();
//                } else {
//                    tagsSelected = true;
//                    CheckEnableUpload();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private boolean CheckDuplicate(String tag) {
        for(int i=0; i<drillTags.size(); i++) {
            if(tag.equals(drillTags.get(i))) {
                return true;
            }
        }
        return false;
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

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
    }

    private void PopulateCategories() {
        categories.add("Select category");
        categories.add("Skill");
        categories.add("Strength");
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        // Set imageSelected to true?
    }

    private void CreateDrill(String imageUri) {
//        String tagString = drillTags.getText().toString().toLowerCase();
//        List<String> tags = Arrays.asList(TextUtils.split(tagString, ","));

//        for(int i=0;i<tags.size();i++) {
//            String tag = tags.get(i).trim();
//            tags.set(i, tag);
//        }

            final String drillId = UUID.randomUUID().toString();
            final Drill drill = new Drill(drillId, userId, drillName.getText().toString(), imageUri, drillDescription.getText().toString(), drillTags, categorySpinner.getSelectedItem().toString());

            drillReference.child(drill.category).child(drillId).setValue(drill).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String category = drill.getCategory();
                    CreateTags(category, drill.getTags(), drillId);
                    CreateUserDrill(drillId, category);
                    // might destroy activity before CreateTags is completed
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
    }

    private void CreateTags(String category, List<String> tags, String id) {
        for(int i=0;i<tags.size();i++) {
            String tag = tags.get(i).toLowerCase();
            tagReference.child(category).child(tag).child(id).setValue(true);
        }
    }

    private void CreateUserDrill(String drillId, String category) {
        firebaseDatabase.getReference().child("users").child(userId).child("myDrills").child(category).child(drillId).setValue(true);
    }

    private void UploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final String fileName = UUID.randomUUID().toString();

            final StorageReference ref = storageReference.child("images/" + fileName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                    System.out.println("URL " + uri.toString());
                                    CreateDrill(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void CheckEnableUpload() {
        if (nameSelected && descriptionSelected && drillTags.size()>0 && categorySelected) {
            btnUpload.setEnabled(true);
        } else {
            btnUpload.setEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.menuDrills:
//                    intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.menuStrength:
//                    intent = new Intent(getApplicationContext(), StrengthActivity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.menuDifficulty:
//                    intent = new Intent(getApplicationContext(), DifficultyActivity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.menuUpload:
////                    intent = new Intent(getApplicationContext(), UploadActivity.class);
////                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        }
//    };
}

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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private EditText drillTags;
    ArrayList<String> categories;
    Spinner categorySpinner;
    Intent intent;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference drillReference;
    DatabaseReference tagReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgViewUpload);
        drillName = findViewById(R.id.editTextName);
        drillDescription = findViewById(R.id.editTextDescription);
        drillTags = findViewById(R.id.editTextTags);
        categorySpinner = findViewById(R.id.categorySpinner);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        drillReference = firebaseDatabase.getReference().child("drills");
        tagReference = firebaseDatabase.getReference().child("tags");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        categories = new ArrayList<>();
        PopulateCategories();

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
//
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
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
    }

    private void CreateDrill(String imageUri) {
        String tagString = drillTags.getText().toString();
        List<String> tags = Arrays.asList(TextUtils.split(tagString, ","));

        for(int i=0;i<tags.size();i++) {
            String tag = tags.get(i).trim();
            tags.set(i, tag);
        }

        final Drill drill = new Drill(drillName.getText().toString(), imageUri, drillDescription.getText().toString(), tags, categorySpinner.getSelectedItem().toString());
        final String id = UUID.randomUUID().toString();

        drillReference.child(drill.category).child(id).setValue(drill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                CreateTags(drill.getCategory(), drill.getTags(), id);
                // might destroy activity before CreateTags is completed
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CreateTags(String category, List<String> tags, String id) {

        for(int i=0;i<tags.size();i++) {
            String tag = tags.get(i);
            tagReference.child(category).child(tag).child(id).setValue(true);
        }
        return;
    }

    public static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }

    private void UploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final String fileName = UUID.randomUUID().toString();
//            System.out.println(fileName);

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuDrills:
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuStrength:
                    intent = new Intent(getApplicationContext(), StrengthActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuDifficulty:
                    intent = new Intent(getApplicationContext(), DifficultyActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuUpload:
//                    intent = new Intent(getApplicationContext(), UploadActivity.class);
//                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}

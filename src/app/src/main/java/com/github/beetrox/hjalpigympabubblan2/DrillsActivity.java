package com.github.beetrox.hjalpigympabubblan2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrillsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String userId;

    String drillId;
    String drillName;
    String drillDescription;
    String drillCategory;
    String imageUrl;

    TextView drillNameTextView;
    TextView drillDescriptionTextView;
    ImageView drillImageView;
    ImageView favouriteImageView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get the value based on the key
            drillId = extras.getString("drillId");
            drillName = extras.getString("drillName");
            drillDescription = extras.getString("drillDescription");
            drillCategory = extras.getString("drillCategory");
            imageUrl = extras.get("imageUrl").toString();
//            Log.d(TAG, value);
        }

        drillNameTextView = findViewById(R.id.titleTextView);
        drillDescriptionTextView = findViewById(R.id.descriptionTextView);

        drillNameTextView.setText(drillName);
        drillDescriptionTextView.setText(drillDescription);

        drillImageView = findViewById(R.id.drillImageView);
        Glide.with(getApplicationContext()).load(imageUrl).into(drillImageView);

        favouriteImageView = findViewById(R.id.favouriteStar);

        firebaseDatabase = FirebaseDatabase.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        favouriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fill in and add to favourite list under user, add something to memorise which stars are filled in
                if (favouriteImageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favourite_star_empty).getConstantState()) {
                    favouriteImageView.setImageResource(R.drawable.favourite_star);
                    firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId).setValue(true);
                } else {
                    favouriteImageView.setImageResource(R.drawable.favourite_star_empty);
                    firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId).removeValue();
                }
            }
        });

        drillDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        Menu menu = navigation.getMenu();
//        for(int i=0; i<menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            menuItem.setChecked(false);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference favouriteReference = firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId);
        System.out.println(userId + " " + drillCategory + " " + drillId);
        favouriteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean value = (boolean) dataSnapshot.getValue();
                    System.out.println(value);
                    if (value) {
                        favouriteImageView.setImageResource(R.drawable.favourite_star);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
//                    intent = new Intent(getApplicationContext(), UploadActivity.class);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        }
//    };
}

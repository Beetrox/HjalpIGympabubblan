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
import android.widget.Toast;

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
    String drillUserId;

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
            drillUserId = extras.get("drillUserId").toString();
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
        if(user != null) {
            userId = user.getUid();
        }

        favouriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fill in and add to favourite list under user, add something to memorise which stars are filled in
                if(user != null) {
                    if (favouriteImageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favourite_star_empty).getConstantState()) {
                        favouriteImageView.setImageResource(R.drawable.favourite_star);
                        firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId).setValue(true);
                    } else {
                        favouriteImageView.setImageResource(R.drawable.favourite_star_empty);
                        firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId).removeValue();
                    }
                } else {
                    Toast.makeText(DrillsActivity.this, getString(R.string.favourite_sign_in), Toast.LENGTH_SHORT).show();
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            userId = user.getUid();
            DatabaseReference favouriteReference = firebaseDatabase.getReference().child("users").child(userId).child("favourites").child(drillCategory).child(drillId);
//            System.out.println(userId + " " + drillCategory + " " + drillId);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drill_menu, menu);

        MenuItem editDrill = menu.findItem(R.id.drillEdit);
        MenuItem deleteDrill = menu.findItem(R.id.drillDelete);
        editDrill.setVisible(false);
        deleteDrill.setVisible(false);
        if (userId != null && userId.equals(drillUserId)) {
            editDrill.setVisible(true);
            deleteDrill.setVisible(true);
        }

        editDrill.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // send extras, which drill
                intent = new Intent(getApplicationContext(), EditDrillActivity.class);
                intent.putExtra("drillId", drillId);
                intent.putExtra("drillCategory", drillCategory);
                startActivity(intent);
                return false;
            }
        });

        deleteDrill.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                firebaseDatabase.getReference().child("drills").child(drillCategory).child(drillId).removeValue();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

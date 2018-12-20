package com.github.beetrox.hjalpigympabubblan2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerViewAdapter myAdapter;
    public List<Drill> myDrills = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        myAdapter = new RecyclerViewAdapter(this, myDrills);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("drills");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        setUpDataBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
    }

    private void setUpDataBase() {
        myDrills.clear();

        firebaseDatabase.getReference().child("users").child(userId).child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        final List<String> drillKeys = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.child("Skill").getChildren()) {
                    String key = childSnapshot.getKey();
                    databaseReference.child("Skill").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Drill drill = dataSnapshot.getValue(Drill.class);
                            myDrills.add(drill);
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                for (DataSnapshot childSnapshot : dataSnapshot.child("Strength").getChildren()) {
                    String key = childSnapshot.getKey();
                    databaseReference.child("Strength").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Drill drill = dataSnapshot.getValue(Drill.class);
                            myDrills.add(drill);
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

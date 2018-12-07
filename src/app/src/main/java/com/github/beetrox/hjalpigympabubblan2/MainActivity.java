package com.github.beetrox.hjalpigympabubblan2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Drill> drills = new ArrayList<>();
    private ActionBar toolbar;
    Intent intent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerViewAdapter myAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills_grid);

//        BottomNavigationMenuView navigationMenu = findViewById(R.id.navigation_view);
        toolbar = getSupportActionBar();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("drills").child("Skill");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

//        CreateMockDrills();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        myAdapter = new RecyclerViewAdapter(this, drills);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myAdapter);

        setUpDataBase();
    }

    public void setUpDataBase() {
        // already signed in
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Main", "onDataChange: ");

                drills.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Drill drill = dataSnapshot1.getValue(Drill.class);
                    drills.add(drill);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);

        MenuItem search_item = menu.findItem(R.id.action_bar_search);

        final SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                drills.clear();
                firebaseDatabase.getReference().child("tags").child("Skill").child(query.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final List<String> drillKeys = new ArrayList<>();

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Drill drill = dataSnapshot.getValue(Drill.class);
                                    drills.add(drill);
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("Change " + newText);
                //show suggestions
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
//
//        MenuItem searchItem = menu.findItem(R.id.action_bar_search);
//
//        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//            System.out.println("first " + searchItem);
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
//            System.out.println("second " + searchItem);
//        }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuDrills:
//                    toolbar.setTitle("Drills");
//                    intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
                    return true;
                case R.id.menuStrength:
//                    toolbar.setTitle("Strength");
                    intent = new Intent(getApplicationContext(), StrengthActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuDifficulty:
//                    toolbar.setTitle("Difficulty");
                    intent = new Intent(getApplicationContext(), DifficultyActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuUpload:
//                    toolbar.setTitle("Upload");
                    intent = new Intent(getApplicationContext(), UploadActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    public void CreateMockDrills() {

//        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i."));
//        drills.add(new DifficultyDrill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
//        drills.add(new DifficultyDrill("Salto från plint", "Drills", "Extra fallhöjd."));
//        drills.add(new DifficultyDrill("Salto i trampett", "Drills", "Våga ta stämmet."));
//        drills.add(new DifficultyDrill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
//        drills.add(new DifficultyDrill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
//        drills.add(new DifficultyDrill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
//        drills.add(new DifficultyDrill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
//        drills.add(new DifficultyDrill("Flickisbacke", "Drills", "En backe att göra flickis i."));
//        drills.add(new DifficultyDrill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
//        drills.add(new DifficultyDrill("Salto från plint", "Drills", "Extra fallhöjd."));
//        drills.add(new DifficultyDrill("Salto i trampett", "Drills", "Våga ta stämmet."));
//        drills.add(new DifficultyDrill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
//        drills.add(new DifficultyDrill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
//        drills.add(new DifficultyDrill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
//        drills.add(new DifficultyDrill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
    }
}

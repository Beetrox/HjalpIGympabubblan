package com.github.beetrox.hjalpigympabubblan2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<DifficultyDrill> drills = new ArrayList<>();
    private ActionBar toolbar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills_grid);

//        BottomNavigationMenuView navigationMenu = findViewById(R.id.navigation_view);
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CreateMockDrills();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, drills);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menuDrills:
                    toolbar.setTitle("Drills");
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuStrength:
                    toolbar.setTitle("Strength");
                    return true;
                case R.id.menuDifficulty:
                    toolbar.setTitle("Difficulty");
                    intent = new Intent(getApplicationContext(), DifficultyActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menuUpload:
                    toolbar.setTitle("Upload");
                    intent = new Intent(getApplicationContext(), UploadActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    public void CreateMockDrills() {

        drills.add(new DifficultyDrill("Flickisbacke", "Drills", "En backe att göra flickis i."));
        drills.add(new DifficultyDrill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
        drills.add(new DifficultyDrill("Salto från plint", "Drills", "Extra fallhöjd."));
        drills.add(new DifficultyDrill("Salto i trampett", "Drills", "Våga ta stämmet."));
        drills.add(new DifficultyDrill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
        drills.add(new DifficultyDrill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
        drills.add(new DifficultyDrill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
        drills.add(new DifficultyDrill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
        drills.add(new DifficultyDrill("Flickisbacke", "Drills", "En backe att göra flickis i."));
        drills.add(new DifficultyDrill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
        drills.add(new DifficultyDrill("Salto från plint", "Drills", "Extra fallhöjd."));
        drills.add(new DifficultyDrill("Salto i trampett", "Drills", "Våga ta stämmet."));
        drills.add(new DifficultyDrill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
        drills.add(new DifficultyDrill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
        drills.add(new DifficultyDrill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
        drills.add(new DifficultyDrill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
    }
}

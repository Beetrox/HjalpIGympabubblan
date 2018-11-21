package com.github.beetrox.hjalpigympabubblan2;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Drill> drills = new ArrayList<>();
//    MenuItem menuDifficulty = findViewById(R.id.menuDifficulty);
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills_grid);

        BottomNavigationMenuView navigationMenu = findViewById(R.id.navigation_view);
        toolbar = getSupportActionBar();

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
                    System.out.println("Drills");
                    return true;
                case R.id.menuStrength:
                    toolbar.setTitle("Strength");
                    return true;
                case R.id.menuDifficulty:
                    toolbar.setTitle("Difficulty");
                    return true;
            }
            return false;
        }
    };

    public void CreateMockDrills() {

        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i."));
        drills.add(new Drill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
        drills.add(new Drill("Salto från plint", "Drills", "Extra fallhöjd."));
        drills.add(new Drill("Salto i trampett", "Drills", "Våga ta stämmet."));
        drills.add(new Drill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
        drills.add(new Drill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
        drills.add(new Drill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
        drills.add(new Drill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i."));
        drills.add(new Drill("Bam", "Drills", "Bra för att fokusera på inhoppet."));
        drills.add(new Drill("Salto från plint", "Drills", "Extra fallhöjd."));
        drills.add(new Drill("Salto i trampett", "Drills", "Våga ta stämmet."));
        drills.add(new Drill("Handvoltstrappa", "Drills", "För att få lite extra fart."));
        drills.add(new Drill("Piruett i rockring", "Drills", "Jobba på att få piruetten balanserad."));
        drills.add(new Drill("Bakåtskruv till tjockmatta", "Drills", "Inbaning för bakåtskruv."));
        drills.add(new Drill("Överslag till upphöjt", "Drills", "Hjälper till att få en bra andrabåge."));
    }
}

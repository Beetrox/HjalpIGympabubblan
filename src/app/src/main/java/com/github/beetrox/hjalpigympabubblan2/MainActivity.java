package com.github.beetrox.hjalpigympabubblan2;

import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Drill> drills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills_grid);

        BottomNavigationMenuView navigationMenu = findViewById(R.id.navigation_view);

        CreateMockDrills();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, drills);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myAdapter);
    }

    public void CreateMockDrills() {

        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));
        drills.add(new Drill("Flickisbacke", "Drills", "En backe att göra flickis i"));

    }
}

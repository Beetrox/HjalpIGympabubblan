package com.github.beetrox.hjalpigympabubblan2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DrillsActivity extends AppCompatActivity {

    String drillName;
    String drillDescription;
    String imageUrl;

    TextView drillNameTextView;
    TextView drillDescriptionTextView;
    ImageView drillImageView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get the value based on the key
            drillName = extras.getString("drillName");
            drillDescription = extras.getString("drillDescription");
            imageUrl = extras.get("imageUrl").toString();
//            Log.d(TAG, value);
        }

        drillNameTextView = findViewById(R.id.titleTextView);
        drillDescriptionTextView = findViewById(R.id.descriptionTextView);

        drillNameTextView.setText(drillName);
        drillDescriptionTextView.setText(drillDescription);

        drillImageView = findViewById(R.id.drillImageView);
        Glide.with(getApplicationContext()).load(imageUrl).into(drillImageView);

        drillDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        Menu menu = navigation.getMenu();
//        for(int i=0; i<menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            menuItem.setChecked(false);
//        }
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
                    intent = new Intent(getApplicationContext(), UploadActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}

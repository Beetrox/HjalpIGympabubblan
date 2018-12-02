package com.github.beetrox.hjalpigympabubblan2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            System.out.println("Glide " + imageUrl);
//            Log.d(TAG, value);
        }

        drillNameTextView = findViewById(R.id.titleTextView);
        drillDescriptionTextView = findViewById(R.id.descriptionTextView);

        drillNameTextView.setText(drillName);
        drillDescriptionTextView.setText(drillDescription);

        drillImageView = findViewById(R.id.drillImageView);
        Glide.with(getApplicationContext()).load(imageUrl).into(drillImageView);
    }
}

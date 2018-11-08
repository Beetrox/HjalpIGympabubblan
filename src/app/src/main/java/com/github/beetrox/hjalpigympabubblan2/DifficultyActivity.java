package com.github.beetrox.hjalpigympabubblan2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class DifficultyActivity extends AppCompatActivity {

    TextView difficultyTextView;
    Spinner firstSkillSpinner;
    Spinner secondSkillSpinner;
    Spinner thirdSkillSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        difficultyTextView = findViewById(R.id.difficultyTextView);
        firstSkillSpinner = findViewById(R.id.firstSkillSpinner);
        secondSkillSpinner = findViewById(R.id.secondSkillSpinner);
        thirdSkillSpinner = findViewById(R.id.thirdSkillSpinner);

//        List<com.github.beetrox.hjalpigympabubblan2.Skill> skillsList =  new ArrayList<com.github.beetrox.hjalpigympabubblan2.Skill>();
//        skillsList.add("Rondat");
//        skillsList.add("Flickis");
//        skillsList.add("Salto grupperad");
//        skillsList.add("Salto pik");

        List<Double> skillArray =  new ArrayList<Double>();
        skillArray.add(0.0);
        skillArray.add(0.1);
        skillArray.add(0.2);
        skillArray.add(0.3);

        ArrayAdapter<Double> skillAdapter = new ArrayAdapter<Double>(
                this, android.R.layout.simple_spinner_item, skillArray);

        skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSkillSpinner.setAdapter(skillAdapter);
        secondSkillSpinner.setAdapter(skillAdapter);
        thirdSkillSpinner.setAdapter(skillAdapter);

        firstSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

                double result = DifficultyResult();
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        secondSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

                double result = DifficultyResult();
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        thirdSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

                double result = DifficultyResult();
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public double DifficultyResult() {

        double result;
        result = Double.valueOf(firstSkillSpinner.getSelectedItem().toString()) + Double.valueOf(secondSkillSpinner.getSelectedItem().toString()) + Double.valueOf(thirdSkillSpinner.getSelectedItem().toString());
//        result = firstSkillSpinner.getSelectedItem();
        return result;
    }
}

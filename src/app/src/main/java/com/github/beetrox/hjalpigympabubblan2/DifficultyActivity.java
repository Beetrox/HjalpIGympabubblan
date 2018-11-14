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

    List<Skill> skills = new ArrayList<>();
    List<String> skillNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        difficultyTextView = findViewById(R.id.difficultyTextView);
        firstSkillSpinner = findViewById(R.id.firstSkillSpinner);
        secondSkillSpinner = findViewById(R.id.secondSkillSpinner);
        thirdSkillSpinner = findViewById(R.id.thirdSkillSpinner);

        PopulateSkills();
        PopulateSkillNames();

//        skillNames =  new ArrayList<>();
//        skillNames.add("Välj övning");
//        skillNames.add(rondat.getName());
//        skillNames.add(flickis.getName());
//        skillNames.add(saltoGrupperad.getName());
//
        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, skillNames);
//
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSkillSpinner.setAdapter(skillsAdapter);
        secondSkillSpinner.setAdapter(skillsAdapter);
        thirdSkillSpinner.setAdapter(skillsAdapter);

//        List<String> skillArray =  new ArrayList<>();
//        skillArray.add("0.0");
//        skillArray.add("0.1");
//        skillArray.add("0.2");
//        skillArray.add("0.3");
//
//        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_spinner_item, skillArray);
//
//        skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        firstSkillSpinner.setAdapter(skillAdapter);
//        secondSkillSpinner.setAdapter(skillAdapter);
//        thirdSkillSpinner.setAdapter(skillAdapter);

//        List<Double> skillArray =  new ArrayList<Double>();
//        skillArray.add(0.0);
//        skillArray.add(0.1);
//        skillArray.add(0.2);
//        skillArray.add(0.3);
//
//        ArrayAdapter<Double> skillAdapter = new ArrayAdapter<Double>(
//                this, android.R.layout.simple_spinner_item, skillArray);
//
//        skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        firstSkillSpinner.setAdapter(skillAdapter);
//        secondSkillSpinner.setAdapter(skillAdapter);
//        thirdSkillSpinner.setAdapter(skillAdapter);

        firstSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("first item selected");
                double result = DifficultyResult(position);
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        secondSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("second item selected");
                double result = DifficultyResult(position);
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        thirdSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("third item selected");
                double result = DifficultyResult(position);
                difficultyTextView.setText(String.valueOf(result));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public double DifficultyResult(int position) {

        double result;
        double difficulty = 0;
        String skillName = skillNames.get(position);

        for(int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            if(skill.name.equals(skillName)) {
                difficulty = skill.getDifficulty();
            }
        }
        result = difficulty;
                // if no skill use 0.0
//        result = Double.valueOf(firstSkillSpinner.getSelectedItem().toString()) + Double.valueOf(secondSkillSpinner.getSelectedItem().toString()) + Double.valueOf(thirdSkillSpinner.getSelectedItem().toString());
//        result = firstSkillSpinner.getSelectedItem();
        return result;
    }

    public void PopulateSkills() {
        Skill noneSelected = new Skill("None selected", 0);
        Skill rondat = new Skill("Rondat", 0.1);
        Skill flickis = new Skill("Flickis", 0.15);
        Skill saltoGrupperad = new Skill("Salto grupperad", 0.2);

        skills.add(noneSelected);
        skills.add(rondat);
        skills.add(flickis);
        skills.add(saltoGrupperad);
    }

    public void PopulateSkillNames() {
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            String name = skill.getName();
            skillNames.add(name);
        }
    }
}

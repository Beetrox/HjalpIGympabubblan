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

    double firstSkillDifficulty = 0;
    double secondSkillDifficulty = 0;
    double thirdSkillDifficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        difficultyTextView = findViewById(R.id.difficultyTextView);
        firstSkillSpinner = findViewById(R.id.firstSkillSpinner);
        secondSkillSpinner = findViewById(R.id.secondSkillSpinner);
        thirdSkillSpinner = findViewById(R.id.thirdSkillSpinner);

        secondSkillSpinner.setEnabled(false);
        thirdSkillSpinner.setEnabled(false);

        PopulateSkills();
        PopulateSkillNames();
//
        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, skillNames);
//
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSkillSpinner.setAdapter(skillsAdapter);
        secondSkillSpinner.setAdapter(skillsAdapter);
        thirdSkillSpinner.setAdapter(skillsAdapter);

        firstSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstSkillDifficulty = DifficultyResult(position);
                ShowDifficulty();
                if(position > 0) {
                    secondSkillSpinner.setEnabled(true);
                } else {
                    secondSkillSpinner.setEnabled(false);
                    secondSkillSpinner.setSelection(0);
                    thirdSkillSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        secondSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondSkillDifficulty = DifficultyResult(position);
                ShowDifficulty();
                if(position > 0) {
                    thirdSkillSpinner.setEnabled(true);
                } else {
                    thirdSkillSpinner.setEnabled(false);
                    thirdSkillSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        thirdSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thirdSkillDifficulty = DifficultyResult(position);
                ShowDifficulty();
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

    void ShowDifficulty() {
        difficultyTextView.setText(String.valueOf(firstSkillDifficulty + secondSkillDifficulty + thirdSkillDifficulty));
    }

    public void PopulateSkills() {
        Skill noneSelected = new Skill("Välj övning", 0);
        Skill rondat = new Skill("Rondat", 0.1);
        Skill flickis = new Skill("Flickis", 0.15);
        Skill saltoGrupperad = new Skill("Salto grupperad", 0.2);
        Skill saltoPik = new Skill("Salto pik", 0.25);
        Skill saltoStrackt = new Skill("Salto sträckt", 0.3);
        Skill saltoStrackt360 = new Skill("Salto sträckt 360", 0.4);
        Skill saltoStrackt720 = new Skill("Salto sträckt 720", 0.5);
        Skill handvolt = new Skill("Handvolt", 0.15);
        Skill frivoltGrupperad = new Skill("Frivolt grupperad", 0.2);
        Skill frivoltPik = new Skill("Frivolt pik", 0.25);
        Skill frivoltStrackt = new Skill("Frivolt sträckt", 0.3);
        Skill frivoltStrackt180 = new Skill("Frivolt sträckt 180", 0.35);
        Skill frivoltStrackt360 = new Skill("Frivolt sträckt 360", 0.4);
        Skill frivoltStrackt540 = new Skill("Frivolt sträckt 540", 0.45);
        Skill frivoltStracktStart = new Skill("Frivolt sträckt start", 0.15);

        skills.add(noneSelected);
        skills.add(rondat);
        skills.add(flickis);
        skills.add(saltoGrupperad);
        skills.add(saltoPik);
        skills.add(saltoStrackt);
        skills.add(saltoStrackt360);
        skills.add(saltoStrackt720);
        skills.add(handvolt);
        skills.add(frivoltGrupperad);
        skills.add(frivoltPik);
        skills.add(frivoltStrackt);
        skills.add(frivoltStrackt180);
        skills.add(frivoltStrackt360);
        skills.add(frivoltStrackt540);
        skills.add(frivoltStracktStart);

    }

    public void PopulateSkillNames() {
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            String name = skill.getName();
            skillNames.add(name);
        }
    }
}
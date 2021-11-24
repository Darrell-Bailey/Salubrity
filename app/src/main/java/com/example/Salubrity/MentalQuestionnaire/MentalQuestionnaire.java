package com.example.Salubrity.MentalQuestionnaire;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.Salubrity.MainActivity;
import com.example.Salubrity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MentalQuestionnaire extends Fragment{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button submitButton;
    Button finishButton;

    @SuppressLint("StaticFieldLeak")
    private static TextView progressText;
    SeekBar seekBar;

    String answer;
    Integer arrayRetrieval = 1;

    TextView question;

    TextView confirm;

    String[] questions = {
            "The general level of happiness felt throughout the day was low.",
            "You communicated well with your peers.",
            "Anxiety levels were high throughout the day.",
            "I did not feel very energized today.",
            "I made healthy food choices today.",
            "After my exercise I felt less focused for the remainder of the day.",
            "Your overall performance throughout the day satisfied you."
    };

    Integer questionsLength = questions.length;

    TextView notes, questionnaire;
    String note;

    String email;

    Integer i = 0;

    String lichterAnswer;

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mental_questionnaire, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        email = activity.getMyData();

        confirm = v.findViewById(R.id.Confirmation);
        confirm.setVisibility(View.GONE);

        question = v.findViewById(R.id.question);
        questionnaire = v.findViewById(R.id.questionnaire);

        submitButton = v.findViewById(R.id.submitQuestionnaire);

        finishButton = v.findViewById(R.id.finish);
        finishButton.setVisibility(View.GONE);

        seekBar = v.findViewById(R.id.seekBar);
        progressText = v.findViewById(R.id.textSeekbar);

        notes = v.findViewById(R.id.Notes);
        note = notes.getText().toString();

        question.setText(questions[0]);
        final String date = getDateTime();

        assert email != null;
        DocumentReference datesRef = db.collection("Users").document(email).collection("Dates").document(date);
        datesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if(document.exists()) {
                        if (document.get("KEY_Question0") != null) {
                            confirm.setVisibility(View.VISIBLE);

                            question.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            submitButton.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            notes.setVisibility(View.GONE);

                            finishButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Navigation.findNavController(v).navigate(R.id.action_questionnaire_to_navigation_home);
                                }
                            });
                        }
                    }else{
                        confirm.setVisibility(View.GONE);
                    }
                }else{
                    Log.d("Error", "Failed with: ", task.getException());
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        answer = String.valueOf(progress);
                        switch (answer) {
                            case "0":
                                lichterAnswer = "Strongly Disagree";
                                progressText.setText("Strongly Disagree");
                                break;
                            case "1":
                                lichterAnswer = "Disagree";
                                progressText.setText("Disagree");
                                break;
                            case "2":
                                lichterAnswer = "Neutral";
                                progressText.setText("Neutral");
                                break;
                            case "3":
                                lichterAnswer = "Agree";
                                progressText.setText("Agree");
                                break;
                            case "4":
                                lichterAnswer = "Strongly Agree";
                                progressText.setText("Strongly Agree");
                                break;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayRetrieval < questionsLength) {
                    question.setText(questions[arrayRetrieval]);
                    note = notes.getText().toString();
                    saveAnswer(date);
                    notes.setText("");
                    arrayRetrieval++;
                } else {
                    note = notes.getText().toString();
                    saveAnswer(date);
                    question.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                    progressText.setVisibility(View.GONE);
                    notes.setVisibility(View.GONE);
                    questionnaire.setVisibility(View.GONE);

                    finishButton.setVisibility(View.VISIBLE);
                }
            }
        });


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_questionnaire_to_navigation_home);
            }
        });

        return v;
    }

    private String getDateTime(){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        return df.format(date);
    }

    public void saveAnswer(String d){
        Map<String, Object> data = new HashMap<>();
        data.put("Date", d);
        data.put("KEY_Question" + i, lichterAnswer);
        data.put("KEY_Note" + i, note);

        DocumentReference docRef = db.collection("Users").document(email).collection("Dates").document(d);
        docRef.set(data, SetOptions.merge());

        i++;
    }
}


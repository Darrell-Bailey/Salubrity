package com.example.Salubrity.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Salubrity.MainActivity;
import com.example.Salubrity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeFragment extends Fragment {

    String email;
    String date;


    TextView results, recEntries, dateEntry, questions, foodsText, exerciseText;

    Button search;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        email = activity.getMyData();

        results = v.findViewById(R.id.results);
        search = v.findViewById(R.id.button);
        recEntries = v.findViewById(R.id.populatedRecentEntries);
        dateEntry = v.findViewById(R.id.editTextDate);
        questions = v.findViewById(R.id.Questions);
        foodsText = v.findViewById(R.id.foods);
        exerciseText = v.findViewById(R.id.exercise);

        questions.setText("1) The general level of happiness felt throughout the day was low.\n\n" +
                "2) You communicated well with your peers.\n\n" +
                "3) Anxiety levels were high throughout the day.\n\n" +
                "4) I did not feel very energized today.\n\n"+
                "5) I made healthy food choices today.\n\n"+
                "6) After my exercise I felt less focused for the remainder of the day.\n\n"+
                "7) Your overall performance throughout the day satisfied you.");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = dateEntry.getText().toString();
                getEntries();
            }
        });

        getRecentEntries();

        return v;
    }

    public void getEntries(){
        db.collection("Users").document("darrellbailey56@gmail.com").collection("Dates").document(date).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            StringBuilder text;
                            StringBuilder exercise = new StringBuilder();
                            String food = "";

                            String date = Objects.requireNonNull(documentSnapshot.get("Date")).toString();
                            text = new StringBuilder("Date: " + date);
                            for(int i = 0; i < 7; i++){
                                if (documentSnapshot.get("KEY_Question" + i) != null) {
                                    String answer = Objects.requireNonNull(documentSnapshot.get("KEY_Question" + i)).toString();
                                    text.append("\n\nQuestion ").append(i + 1).append(": ").append(answer);
                                }

                                if (documentSnapshot.get("KEY_Note" + i) != null) {
                                    String note = Objects.requireNonNull(documentSnapshot.get("KEY_Note" + i)).toString();
                                    text.append("       Note: ").append(note);
                                }
                            }

                            for(int i = 0; i < 10; i++) {
                                if (documentSnapshot.get("Exercise" + i) != null) {
                                    String exercise0 = Objects.requireNonNull(documentSnapshot.get("Exercise" + i)).toString();
                                    exercise.append("Exercise: ").append(exercise0);
                                }
                                if (documentSnapshot.get("SetsReps" + i) != null) {
                                    String sets = Objects.requireNonNull(documentSnapshot.get("SetsReps" + i)).toString();
                                    exercise.append("       Sets/Reps: ").append(sets);
                                }
                                if (documentSnapshot.get("Duration" + i) != null) {
                                    String duration = Objects.requireNonNull(documentSnapshot.get("Duration" + i)).toString();
                                    exercise.append("       Duration: ").append(duration).append("\n\n");
                                }
                            }

                            if (documentSnapshot.get("Calories") != null) {
                                String calories = Objects.requireNonNull(documentSnapshot.get("Calories")).toString();
                                food += "Calories: " + calories;
                            }
                            if (documentSnapshot.get("Carbohydrates") != null) {
                                String carbs = Objects.requireNonNull(documentSnapshot.get("Carbohydrates")).toString();
                                food += "\nCarbohydrates: " + carbs + " g";
                            }
                            if (documentSnapshot.get("Fats") != null) {
                                String fats = Objects.requireNonNull(documentSnapshot.get("Fats")).toString();
                                food += "\nFats: " + fats + " g";
                            }
                            if (documentSnapshot.get("Protein") != null) {
                                String protein = Objects.requireNonNull(documentSnapshot.get("Protein")).toString();
                                food += "\nProtein: " + protein + " g";
                            }
                            if (documentSnapshot.get("Foods") != null) {
                                String foods = Objects.requireNonNull(documentSnapshot.get("Foods")).toString();
                                food += "\nFoods: " + foods;
                            }

                            foodsText.setText(food);
                            exerciseText.setText(exercise.toString());
                            results.setText(text.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Not a Valid Date", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getRecentEntries(){
        db.collection("Users").document("darrellbailey56@gmail.com").collection("Dates").document("05-05-2021").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            StringBuilder text;
                            StringBuilder exercise = new StringBuilder();
                            String food = "";

                            String date = Objects.requireNonNull(documentSnapshot.get("Date")).toString();
                            text = new StringBuilder("Date: " + date);
                            for(int i = 0; i < 7; i++){
                                if (documentSnapshot.get("KEY_Question" + i) != null) {
                                    String answer = Objects.requireNonNull(documentSnapshot.get("KEY_Question" + i)).toString();
                                    text.append("\n\nQuestion ").append(i + 1).append(": ").append(answer);
                                }

                                if (documentSnapshot.get("KEY_Note" + i) != null) {
                                    String note = Objects.requireNonNull(documentSnapshot.get("KEY_Note" + i)).toString();
                                    text.append("       Note: ").append(note);
                                }
                            }

                            for(int i = 0; i < 10; i++) {
                                if (documentSnapshot.get("Exercise" + i) != null) {
                                    String exercise0 = Objects.requireNonNull(documentSnapshot.get("Exercise" + i)).toString();
                                    exercise.append("Exercise: ").append(exercise0);
                                }
                                if (documentSnapshot.get("SetsReps" + i) != null) {
                                    String sets = Objects.requireNonNull(documentSnapshot.get("SetsReps" + i)).toString();
                                    exercise.append("       Sets/Reps: ").append(sets);
                                }
                                if (documentSnapshot.get("Duration" + i) != null) {
                                    String duration = Objects.requireNonNull(documentSnapshot.get("Duration" + i)).toString();
                                    exercise.append("       Duration: ").append(duration).append("\n\n");
                                }
                            }

                            if (documentSnapshot.get("Calories") != null) {
                                String calories = Objects.requireNonNull(documentSnapshot.get("Calories")).toString();
                                food += "Calories: " + calories;
                            }
                            if (documentSnapshot.get("Carbohydrates") != null) {
                                String carbs = Objects.requireNonNull(documentSnapshot.get("Carbohydrates")).toString();
                                food += "\nCarbohydrates: " + carbs + " g";
                            }
                            if (documentSnapshot.get("Fats") != null) {
                                String fats = Objects.requireNonNull(documentSnapshot.get("Fats")).toString();
                                food += "\nFats: " + fats + " g";
                            }
                            if (documentSnapshot.get("Protein") != null) {
                                String protein = Objects.requireNonNull(documentSnapshot.get("Protein")).toString();
                                food += "\nProtein: " + protein + " g";
                            }
                            if (documentSnapshot.get("Foods") != null) {
                                String foods = Objects.requireNonNull(documentSnapshot.get("Foods")).toString();
                                food += "\nFoods: " + foods;
                            }

//                            String text = "";
//                            String date = Objects.requireNonNull(documentSnapshot.get("Date")).toString();
//                            if (documentSnapshot.get("KEY_Question0") != null) {
//                                String answer = Objects.requireNonNull(documentSnapshot.get("KEY_Question0")).toString();
//                                text = "Date: " + date + "\nQuestion 1: " + answer;
//                            }
//
//                            if(documentSnapshot.get("KEY_Note0") != null){
//                                String note = Objects.requireNonNull(documentSnapshot.get("KEY_Note0")).toString();
//                                text += "       Note: " + note;
//                            }
//
//                            if (documentSnapshot.get("KEY_Question1") != null) {
//                                String answer1 = Objects.requireNonNull(documentSnapshot.get("KEY_Question1")).toString();
//                                text += "\nQuestion 2: " + answer1;
//                            }
//                            if(documentSnapshot.get("KEY_Note2") != null){
//                                String note1 = Objects.requireNonNull(documentSnapshot.get("KEY_Note1")).toString();
//                            text += "       Note: " + note1;
//                            }
//
//                            if (documentSnapshot.get("KEY_Question2") != null) {
//                                String answer2 = Objects.requireNonNull(documentSnapshot.get("KEY_Question2")).toString();
//                                text += "\nQuestion 3: " + answer2;
//                            }
//                            if(documentSnapshot.get("KEY_Note2") != null){
//                                String note2 = Objects.requireNonNull(documentSnapshot.get("KEY_Note2")).toString();
//                                text += "       Note: " + note2;
//                            }
//
//                            if (documentSnapshot.get("KEY_Question3") != null) {
//                                String answer2 = Objects.requireNonNull(documentSnapshot.get("KEY_Question3")).toString();
//                                text += "\nQuestion 4: " + answer2;
//                            }
//                            if(documentSnapshot.get("KEY_Note0") != null){
//                                String note3 = Objects.requireNonNull(documentSnapshot.get("KEY_Note3")).toString();
//                                text += "       Note: " + note3;
//                            }
//
//                            if(documentSnapshot.get("Exercise0") != null){
//                                String exercise = Objects.requireNonNull(documentSnapshot.get("Exercise0")).toString();
//                                text += "\nExercise: " + exercise;
//                            }
//                            if(documentSnapshot.get("SetsReps0") != null){
//                                String sets = Objects.requireNonNull(documentSnapshot.get("SetsReps0")).toString();
//                                text += "       Sets/Reps: " + sets;
//                            }
//                            if(documentSnapshot.get("Duration0") != null){
//                                String duration = Objects.requireNonNull(documentSnapshot.get("Duration0")).toString();
//                                text += "       Duration: " + duration;
//                            }
//
//                            if(documentSnapshot.get("Exercise1") != null){
//                                String exercise1 = Objects.requireNonNull(documentSnapshot.get("Exercise1")).toString();
//                                text += "\nExercise: " + exercise1;
//                            }
//                            if(documentSnapshot.get("SetsReps1") != null){
//                                String sets = Objects.requireNonNull(documentSnapshot.get("SetsReps1")).toString();
//                                text += "       Sets/Reps: " + sets;
//                            }
//                            if(documentSnapshot.get("Duration1") != null){
//                                String duration = Objects.requireNonNull(documentSnapshot.get("Duration1")).toString();
//                                text += "       Duration: " + duration;
//                            }
//
//                            if(documentSnapshot.get("Exercise2") != null){
//                                String exercise2 = Objects.requireNonNull(documentSnapshot.get("Exercise2")).toString();
//                                text += "\nExercise: " + exercise2;
//                            }
//                            if(documentSnapshot.get("SetsReps2") != null){
//                                String sets = Objects.requireNonNull(documentSnapshot.get("SetsReps2")).toString();
//                                text += "       Sets/Reps: " + sets;
//                            }
//                            if(documentSnapshot.get("Duration2") != null){
//                                String duration = Objects.requireNonNull(documentSnapshot.get("Duration2")).toString();
//                                text += "       Duration: " + duration;
//                            }
//
//                            if(documentSnapshot.get("Calories") != null){
//                                String calories = Objects.requireNonNull(documentSnapshot.get("Calories")).toString();
//                                text += "\nCalories:    " + calories;
//                            }
//                            if(documentSnapshot.get("Carbohydrates") != null){
//                                String carbs = Objects.requireNonNull(documentSnapshot.get("Carbohydrates")).toString();
//                                text += "\nCarbs:   " + carbs;
//                            }
//                            if(documentSnapshot.get("Fats") != null){
//                                String fats = Objects.requireNonNull(documentSnapshot.get("Fats")).toString();
//                                text += "  Fats:    " + fats;
//                            }
//                            if(documentSnapshot.get("Protein") != null){
//                                String protein = Objects.requireNonNull(documentSnapshot.get("Protein")).toString();
//                                text += "  Protein: " + protein;
//                            }
//                            if(documentSnapshot.get("foods") != null){
//                                String foods = Objects.requireNonNull(documentSnapshot.get("foods")).toString();
//                                text += "\nFoods: " + foods;
//                            }

                            recEntries.setText(text + "\n\n" + exercise + food);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
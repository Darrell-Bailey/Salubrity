package com.example.Salubrity.ui.dashboard;

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
import androidx.navigation.Navigation;

import com.example.Salubrity.MainActivity;
import com.example.Salubrity.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Exercise extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String email;

    String data = "";
    ArrayList<String> exerciseArray = new ArrayList<>();
    ArrayList<String> setsArray = new ArrayList<>();
    ArrayList<String> durationArray = new ArrayList<>();

    Button submitButton, addButton;

    TextView exerciseText, setsText, durationText, textViewData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        email = activity.getMyData();

        final String date = getDateTime();

        exerciseText = v.findViewById(R.id.exercise1);
        setsText = v.findViewById(R.id.sets1);
        durationText = v.findViewById(R.id.Time);
        textViewData = v.findViewById(R.id.textViewData);

        addButton = v.findViewById(R.id.AddText);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercise();
            }
        });


        submitButton = v.findViewById(R.id.submit_exercise_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry(date);
                Toast.makeText(requireContext(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_home);
            }
        });


        return v;
    }

    public void saveEntry(String d){
        for(int b = 0; b <= exerciseArray.size() - 1; b++){
            Map<String, Object> data = new HashMap<>();
            data.put("Date", d);
            data.put("Exercise" + b, exerciseArray.get(b));
            data.put("SetsReps" + b, setsArray.get(b));
            data.put("Duration" + b, durationArray.get(b));

            DocumentReference docRef = db.collection("Users").document(email).collection("Dates").document(d);
            docRef.set(data, SetOptions.merge());
        }
    }

    public void addExercise(){
        String exercise = exerciseText.getText().toString();
        String sets = setsText.getText().toString();
        String time = durationText.getText().toString();

        exerciseArray.add(exercise);
        setsArray.add(sets);
        durationArray.add(time);

        data += "Exercise: " + exercise + "\nSets: " + sets + "\nTime: " + time + "\n\n";
        textViewData.setText(data);

        exerciseText.setText("");
        setsText.setText("");
        durationText.setText("");
    }

    private String getDateTime(){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        return df.format(date);
    }

}
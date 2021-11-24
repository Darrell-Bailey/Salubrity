package com.example.Salubrity.ui.notifications;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Food extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button submitButton;

    String cals, prot, carbs, fats, foods;

    String email;
    TextView caloriesText, proteinText, carbsText, fatsText, foodsText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        email = activity.getMyData();

        caloriesText = v.findViewById(R.id.calories_eaten_edittext);
        proteinText = v.findViewById(R.id.protein_edittext);
        carbsText = v.findViewById(R.id.carbs_edittext);
        fatsText = v.findViewById(R.id.fats_edittext);
        foodsText = v.findViewById(R.id.foods_edittext);

        final String date = getDateTime();


        submitButton = v.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cals = caloriesText.getText().toString();
                prot = proteinText.getText().toString();
                carbs = carbsText.getText().toString();
                fats = fatsText.getText().toString();
                foods = foodsText.getText().toString();
                saveEntry(date);
                Toast.makeText(requireContext(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_navigation_notifications_to_navigation_home);
            }
        });


        return v;
    }

    public void saveEntry(String d){
        final DocumentReference docRef = db.collection("Users").document(email).collection("Dates").document(d);

        Map<String, Object> data = new HashMap<>();
        data.put("Date", d);
        data.put("Calories", cals);
        data.put("Protein", prot);
        data.put("Carbohydrates",carbs);
        data.put("Fats", fats);
        data.put("Foods", foods);
        docRef.set(data, SetOptions.merge());
    }

    private String getDateTime(){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        return df.format(date);
    }
}
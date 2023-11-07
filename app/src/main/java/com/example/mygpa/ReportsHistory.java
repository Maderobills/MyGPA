package com.example.mygpa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportsHistory extends Fragment {

    RecyclerView schoolRecycler;
    SchoolsAdaptor adapterSchools;
    ArrayList<SchoolFormData> schoolList;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_reports_history, container, false);

        schoolRecycler = myView.findViewById(R.id.recycler_id_schools);

        LinearLayoutManager layoutManagerSchool = new LinearLayoutManager(getActivity());
        layoutManagerSchool.setReverseLayout(true);
        layoutManagerSchool.setStackFromEnd(true);
        schoolRecycler.setLayoutManager(layoutManagerSchool);

        schoolList = new ArrayList<>();
        adapterSchools = new SchoolsAdaptor(schoolList, getActivity());
        schoolRecycler.setAdapter(adapterSchools);

        readSchools();

        return myView;
    }

    private void readSchools() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Schools")
                    .child("Class Year") // Corrected path to the ClassYear
                    .child(uid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    schoolList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SchoolFormData schoolsData = dataSnapshot.getValue(SchoolFormData.class);
                        schoolList.add(schoolsData);
                    }

                    adapterSchools.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error here, e.g., show an error message
                    Toast.makeText(getActivity(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

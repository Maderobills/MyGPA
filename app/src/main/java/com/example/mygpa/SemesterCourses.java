package com.example.mygpa;

import android.os.Bundle;

import androidx.annotation.NonNull;
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


public class SemesterCourses extends Fragment {

    RecyclerView coursesRecycler;
    CoursesAdaptor adapterCourses;
    ArrayList<CoursesFormData> courseList;

    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.schools_recycler_data, container, false);

        coursesRecycler = myView.findViewById(R.id.recycler_id_courses);

        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getActivity());
        layoutManagerCourse.setReverseLayout(true);
        layoutManagerCourse.setStackFromEnd(true);
        coursesRecycler.setLayoutManager(layoutManagerCourse);

        courseList = new ArrayList<>();
        adapterCourses = new CoursesAdaptor(courseList, getActivity());
        coursesRecycler.setAdapter(adapterCourses);

        readCourses();

        return myView;
    }

    private void readCourses() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Schools")
                    .child(uid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CoursesFormData courseData = dataSnapshot.getValue(CoursesFormData.class);
                        courseList.add(courseData);
                    }

                    adapterCourses.notifyDataSetChanged();
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
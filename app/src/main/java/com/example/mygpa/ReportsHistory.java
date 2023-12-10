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

public class ReportsHistory extends Fragment {

    RecyclerView schoolRecycler,courseRecycler;
    SchoolsAdaptor adapterSchools;
    SemCoursesAdaptor adapterCourses;

    ArrayList<SchoolFormData> schoolList = new ArrayList<>();
    ArrayList<SemCourseFormData> courseList = new ArrayList<>();

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_reports_history, container, false);

        schoolRecycler = myView.findViewById(R.id.recycler_id_schools);

        LinearLayoutManager layoutManagerSchool = new LinearLayoutManager(getContext());
        layoutManagerSchool.setReverseLayout(true);
        layoutManagerSchool.setStackFromEnd(true);
        schoolRecycler.setLayoutManager(layoutManagerSchool);

        adapterSchools = new SchoolsAdaptor(schoolList, getContext());
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
                    .child("Students").child(uid).child("Schools");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    schoolList.clear();
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        SchoolFormData schoolsData = schoolSnapshot.getValue(SchoolFormData.class);
                        if (schoolsData != null) {
                            schoolList.add(schoolsData);
                            readCourses();
                        }
                    }

                    adapterSchools.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void readCourses() {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference()
                .child("Students").child(mAuth.getUid()).child("Schools");

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseId = "DUC";
                    SemCourseFormData courseData = courseSnapshot.child("Courses").getValue(SemCourseFormData.class);
                    courseList.clear();
                    if (courseData != null) {
                        courseList.add(courseData);
                    }
                }

                courseRecycler = schoolRecycler.findViewById(R.id.recycler_id_courses);

                LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getContext());
                layoutManagerCourse.setReverseLayout(true);
                layoutManagerCourse.setStackFromEnd(true);

                // Initialize adapterCourses here
                adapterCourses = new SemCoursesAdaptor(getContext(), courseList);
                courseRecycler.setLayoutManager(layoutManagerCourse);

                // Set the adapter to the RecyclerView
                courseRecycler.setAdapter(adapterCourses);

                // Notify the adapter for changes
                adapterCourses.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


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

        adapterCourses = new SemCoursesAdaptor(getContext(), courseList);
        courseRecycler = myView.findViewById(R.id.recycler_id_courses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        courseRecycler.setLayoutManager(layoutManager);
        courseRecycler.setAdapter(adapterCourses);

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
                courseList.clear();
                // Clear the courseList before adding new data
                for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                    DataSnapshot coursesSnapshot = schoolSnapshot.child("Courses");
                    for (DataSnapshot semesterSnapshot : coursesSnapshot.getChildren()) {
                        for (DataSnapshot courseSnapshot : semesterSnapshot.child("Semester 1").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);
                            if (courseData != null) {
                                courseList.add(courseData);
                            }
                        }
                        for (DataSnapshot courseSnapshot : semesterSnapshot.child("Semester 2").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);
                            if (courseData != null) {
                                courseList.add(courseData);
                            }
                        }
                    }
                }
                // Initialize and set adapter to the RecyclerView here
                adapterCourses.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //RETRIEVES SPECIFIC SCHOOL
    /*private void readCourses(String keyS) {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference()
                .child("Students").child(mAuth.getUid()).child("Schools");

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Clear the courseList before adding new data
                for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                    String schoolKey = schoolSnapshot.getKey();
                    if (schoolKey != null && schoolKey.equals(keyS)) {
                        courseList.clear();
                        // Retrieve courses for the specific school with ID "keyS"
                        DataSnapshot coursesSnapshot = schoolSnapshot.child("Courses").child(keyS);
                        if (coursesSnapshot.exists()) {
                            for (DataSnapshot courseSnapshot : coursesSnapshot.child("Semester 1").getChildren()) {
                                SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);
                                if (courseData != null) {
                                    courseList.add(courseData);
                                }
                            }
                        }
                        break; // Break the loop once courses for the specific school are retrieved
                    }
                }
                // Initialize and set adapter to the RecyclerView here
                adapterCourses.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}


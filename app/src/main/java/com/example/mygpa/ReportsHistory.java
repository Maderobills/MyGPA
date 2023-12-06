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

    RecyclerView schoolRecycler, courseRecycler;
    SchoolsAdaptor adapterSchools;
    SemCoursesAdaptor adapterCourses;

    ArrayList<SchoolFormData> schoolList;
    ArrayList<SemCourseFormData> courseList;


    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_reports_history, container, false);


        schoolRecycler = myView.findViewById(R.id.recycler_id_schools);
        courseRecycler = myView.findViewById(R.id.recycler_id_courses);

        LinearLayoutManager layoutManagerSchool = new LinearLayoutManager(getActivity());
        layoutManagerSchool.setReverseLayout(true);
        layoutManagerSchool.setStackFromEnd(true);
        schoolRecycler.setLayoutManager(layoutManagerSchool);

        schoolList = new ArrayList<>();
        adapterSchools = new SchoolsAdaptor(schoolList, getActivity());
        schoolRecycler.setAdapter(adapterSchools);

        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getActivity());
        layoutManagerCourse.setReverseLayout(true);
        layoutManagerCourse.setStackFromEnd(true);
        courseRecycler.setLayoutManager(layoutManagerCourse);

        courseList = new ArrayList<>();
        adapterCourses = new SemCoursesAdaptor(getActivity(), courseList);
        courseRecycler.setAdapter(adapterCourses);

        readSchools();
        readCourses();


        return myView;
    }

    private void readSchools() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    schoolList.clear();

                    // Iterate through each school under the student
                    for (DataSnapshot schoolSnapshot : snapshot.child("Schools").getChildren()) {
                        String schoolId = schoolSnapshot.getKey();
                        SchoolFormData schoolsData = schoolSnapshot.getValue(SchoolFormData.class);
                        // Assuming SchoolFormData contains getters and setters for school data
                        schoolList.add(schoolsData);
                    }

                    adapterSchools.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(getActivity(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void readCourses() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList.clear();

                    // Iterate through each course under the student
                    for (DataSnapshot courseSnapshot : snapshot.child("Courses").getChildren()) {
                        String courseId = courseSnapshot.getKey(); // Assuming courseId identifies the course uniquely
                        SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);
                        if (courseData != null) {
                            courseList.add(courseData);
                        }
                    }

                    adapterCourses.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(getActivity(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




}

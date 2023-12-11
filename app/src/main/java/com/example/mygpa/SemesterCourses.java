package com.example.mygpa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SemesterCourses extends Fragment {

    SemCoursesAdaptor adapterCourses;
    ArrayList<SemCourseFormData> courseList = new ArrayList<>();
    RecyclerView courseRecycler;

    private FirebaseAuth mAuth;
    private TextView schoolName;
    private TextView gpaScale;
    private TextView semN;
    private TextView daS;
    private TextView daE;
    private String storedSchName;
    private String storedGpaSc;
    private String storedNSem;
    private String storedDateS;
    private String storedDateE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_semester_courses, container, false);

        courseRecycler = myView.findViewById(R.id.recycler_id_courses);

        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getActivity());
        layoutManagerCourse.setReverseLayout(true);
        layoutManagerCourse.setStackFromEnd(true);
        courseRecycler.setLayoutManager(layoutManagerCourse);

        courseList = new ArrayList<>();
        adapterCourses = new SemCoursesAdaptor(getActivity(), courseList);
        courseRecycler.setAdapter(adapterCourses);

        schoolName = myView.findViewById(R.id.School_name);
        gpaScale = myView.findViewById(R.id.scale_type);
        semN = myView.findViewById(R.id.sem_number);
        daS = myView.findViewById(R.id.sDate_text);
        daE = myView.findViewById(R.id.eDate_text);

        schoolName.setText(storedSchName);
        gpaScale.setText(storedGpaSc);
        semN.setText(storedNSem);
        daS.setText(storedDateS + " / ");
        daE.setText(storedDateE);


        readCourses(storedSchName);


        return myView;
    }

    public void setSchoolData(String schName, String gpaSc, String nSem, String dateS, String dateE) {
        storedSchName = schName;
        storedGpaSc = gpaSc;
        storedNSem = nSem;
        storedDateS = dateS;
        storedDateE = dateE;
    }

    private void readCourses(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 1").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList.add(courseData);
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
    }
}
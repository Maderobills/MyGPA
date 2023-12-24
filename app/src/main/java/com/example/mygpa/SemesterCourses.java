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
import android.widget.LinearLayout;
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

    SemCoursesAdaptor adapterCourses,adapterCourses_2 ;
    ArrayList<SemCourseFormData> courseList,courseList_2 = new ArrayList<>();
    RecyclerView courseRecycler,courseRecycler_2;

    private FirebaseAuth mAuth;
    private TextView schoolName;
    private TextView idN;
    private TextView proN;

    private TextView gpaScale;
    private TextView semN;
    private TextView daS;
    private TextView daE;
    private String storedSchName;
    private String storedIdno;
    private String storedProG;
    private String storedGpaSc;
    private String storedNSem;
    private String storedDateS;
    private String storedDateE;

    LinearLayout layout_s_1,layout_s_2,layout_s_3,layout_s_4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_semester_courses, container, false);

        layout_s_1 = myView.findViewById(R.id.layout_Sem_1);
        layout_s_2 = myView.findViewById(R.id.layout_Sem_2);
        layout_s_3 = myView.findViewById(R.id.layout_Sem_3);
        layout_s_4 = myView.findViewById(R.id.layout_Sem_4);

        courseRecycler = myView.findViewById(R.id.recycler_id_courses);
        courseRecycler_2 = myView.findViewById(R.id.recycler_id_courses_2);

        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getActivity());
        layoutManagerCourse.setReverseLayout(true);
        layoutManagerCourse.setStackFromEnd(true);
        courseRecycler.setLayoutManager(layoutManagerCourse);

        LinearLayoutManager layoutManagerCourse_2 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_2.setReverseLayout(true);
        layoutManagerCourse_2.setStackFromEnd(true);
        courseRecycler_2.setLayoutManager(layoutManagerCourse_2);

        courseList = new ArrayList<>();
        adapterCourses = new SemCoursesAdaptor(getActivity(), courseList);
        courseRecycler.setAdapter(adapterCourses);

        courseList_2 = new ArrayList<>();
        adapterCourses_2 = new SemCoursesAdaptor(getActivity(), courseList_2);
        courseRecycler_2.setAdapter(adapterCourses_2);

        schoolName = myView.findViewById(R.id.School_name);
        idN = myView.findViewById(R.id.indexNo_text);
        proN = myView.findViewById(R.id.inProgram);
        gpaScale = myView.findViewById(R.id.scale_type);
        semN = myView.findViewById(R.id.sem_number);
        daS = myView.findViewById(R.id.sDate_text);
        daE = myView.findViewById(R.id.eDate_text);

        schoolName.setText(storedSchName);
        idN.setText(storedIdno);
        proN.setText(storedProG);
        gpaScale.setText(storedGpaSc);
        semN.setText(storedNSem);
        daS.setText(storedDateS + " / ");
        daE.setText(storedDateE);



        int getSnum = Integer.parseInt(storedNSem);
        viewSemLayout(getSnum);

        readCourses(storedSchName);
        readCourses_2(storedSchName);


        return myView;
    }

    private void viewSemLayout(int numOfSem){
        if (numOfSem > 0 && numOfSem <= 8) {
            // Check different cases based on the number of semesters
            switch (numOfSem) {
                case 1:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.GONE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 2:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 3:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 4:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    break;
                /*case 5:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.GONE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 6:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 7:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.GONE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;
                case 8:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    break;*/
                default:
                    // Handle cases where more than 2 semesters are selected
                    // Show all relevant views here if needed
                    break;
            }
        } else {
            // The value is outside the range of 1 to 8
            // Handle the case where the value is not within the range
        }

    }

    public void setSchoolData(String schName, String idNo, String proG, String gpaSc, String nSem, String dateS, String dateE) {
        storedSchName = schName;
        storedIdno = idNo;
        storedProG = proG;
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
    private void readCourses_2(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_2.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 2").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_2.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_2.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
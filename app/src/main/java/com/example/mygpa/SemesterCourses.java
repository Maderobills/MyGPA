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

    SemCoursesAdaptor adapterCourses,adapterCourses_2, adapterCourses_3, adapterCourses_4, adapterCourses_5, adapterCourses_6, adapterCourses_7, adapterCourses_8;
    ArrayList<SemCourseFormData> courseList,courseList_2,courseList_3,courseList_4,courseList_5,courseList_6,courseList_7,courseList_8 = new ArrayList<>();
    RecyclerView courseRecycler,courseRecycler_2,courseRecycler_3, courseRecycler_4, courseRecycler_5, courseRecycler_6, courseRecycler_7, courseRecycler_8;

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

    LinearLayout layout_s_1,layout_s_2,layout_s_3,layout_s_4, layout_s_5,layout_s_6,layout_s_7,layout_s_8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_semester_courses, container, false);

        layout_s_1 = myView.findViewById(R.id.layout_Sem_1);
        layout_s_2 = myView.findViewById(R.id.layout_Sem_2);
        layout_s_3 = myView.findViewById(R.id.layout_Sem_3);
        layout_s_4 = myView.findViewById(R.id.layout_Sem_4);
        layout_s_5 = myView.findViewById(R.id.layout_Sem_5);
        layout_s_6 = myView.findViewById(R.id.layout_Sem_6);
        layout_s_7 = myView.findViewById(R.id.layout_Sem_7);
        layout_s_8 = myView.findViewById(R.id.layout_Sem_8);

        courseRecycler = myView.findViewById(R.id.recycler_id_courses);
        courseRecycler_2 = myView.findViewById(R.id.recycler_id_courses_2);
        courseRecycler_3 = myView.findViewById(R.id.recycler_id_courses_3);
        courseRecycler_4 = myView.findViewById(R.id.recycler_id_courses_4);
        courseRecycler_5 = myView.findViewById(R.id.recycler_id_courses_5);
        courseRecycler_6 = myView.findViewById(R.id.recycler_id_courses_6);
        courseRecycler_7 = myView.findViewById(R.id.recycler_id_courses_7);
        courseRecycler_8 = myView.findViewById(R.id.recycler_id_courses_8);




        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(getActivity());
        layoutManagerCourse.setReverseLayout(true);
        layoutManagerCourse.setStackFromEnd(true);
        courseRecycler.setLayoutManager(layoutManagerCourse);

        LinearLayoutManager layoutManagerCourse_2 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_2.setReverseLayout(true);
        layoutManagerCourse_2.setStackFromEnd(true);
        courseRecycler_2.setLayoutManager(layoutManagerCourse_2);

        LinearLayoutManager layoutManagerCourse_3 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_3.setReverseLayout(true);
        layoutManagerCourse_3.setStackFromEnd(true);
        courseRecycler_3.setLayoutManager(layoutManagerCourse_3);

        LinearLayoutManager layoutManagerCourse_4 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_4.setReverseLayout(true);
        layoutManagerCourse_4.setStackFromEnd(true);
        courseRecycler_4.setLayoutManager(layoutManagerCourse_4);

        LinearLayoutManager layoutManagerCourse_5 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_5.setReverseLayout(true);
        layoutManagerCourse_5.setStackFromEnd(true);
        courseRecycler_5.setLayoutManager(layoutManagerCourse_5);

        LinearLayoutManager layoutManagerCourse_6 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_6.setReverseLayout(true);
        layoutManagerCourse_6.setStackFromEnd(true);
        courseRecycler_6.setLayoutManager(layoutManagerCourse_6);

        LinearLayoutManager layoutManagerCourse_7 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_7.setReverseLayout(true);
        layoutManagerCourse_7.setStackFromEnd(true);
        courseRecycler_7.setLayoutManager(layoutManagerCourse_7);

        LinearLayoutManager layoutManagerCourse_8 = new LinearLayoutManager(getActivity());
        layoutManagerCourse_8.setReverseLayout(true);
        layoutManagerCourse_8.setStackFromEnd(true);
        courseRecycler_8.setLayoutManager(layoutManagerCourse_8);

        courseList = new ArrayList<>();
        adapterCourses = new SemCoursesAdaptor(getActivity(), courseList);
        courseRecycler.setAdapter(adapterCourses);

        courseList_2 = new ArrayList<>();
        adapterCourses_2 = new SemCoursesAdaptor(getActivity(), courseList_2);
        courseRecycler_2.setAdapter(adapterCourses_2);

        courseList_3 = new ArrayList<>();
        adapterCourses_3 = new SemCoursesAdaptor(getActivity(), courseList_3);
        courseRecycler_3.setAdapter(adapterCourses_3);

        courseList_4 = new ArrayList<>();
        adapterCourses_4 = new SemCoursesAdaptor(getActivity(), courseList_4);
        courseRecycler_4.setAdapter(adapterCourses_4);

        courseList_5 = new ArrayList<>();
        adapterCourses_5 = new SemCoursesAdaptor(getActivity(), courseList_5);
        courseRecycler_5.setAdapter(adapterCourses_5);

        courseList_6 = new ArrayList<>();
        adapterCourses_6 = new SemCoursesAdaptor(getActivity(), courseList_6);
        courseRecycler_6.setAdapter(adapterCourses_6);

        courseList_7 = new ArrayList<>();
        adapterCourses_7 = new SemCoursesAdaptor(getActivity(), courseList_7);
        courseRecycler_7.setAdapter(adapterCourses_7);

        courseList_8 = new ArrayList<>();
        adapterCourses_8 = new SemCoursesAdaptor(getActivity(), courseList_8);
        courseRecycler_8.setAdapter(adapterCourses_8);

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
        readCourses_3(storedSchName);
        readCourses_4(storedSchName);
        readCourses_5(storedSchName);
        readCourses_6(storedSchName);
        readCourses_7(storedSchName);
        readCourses_8(storedSchName);


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
                    layout_s_5.setVisibility(View.GONE);
                    layout_s_6.setVisibility(View.GONE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 2:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.GONE);
                    layout_s_4.setVisibility(View.GONE);
                    layout_s_5.setVisibility(View.GONE);
                    layout_s_6.setVisibility(View.GONE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 3:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.GONE);
                    layout_s_5.setVisibility(View.GONE);
                    layout_s_6.setVisibility(View.GONE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 4:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    layout_s_5.setVisibility(View.GONE);
                    layout_s_6.setVisibility(View.GONE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 5:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    layout_s_5.setVisibility(View.VISIBLE);
                    layout_s_6.setVisibility(View.GONE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 6:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    layout_s_5.setVisibility(View.VISIBLE);
                    layout_s_6.setVisibility(View.VISIBLE);
                    layout_s_7.setVisibility(View.GONE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 7:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    layout_s_5.setVisibility(View.VISIBLE);
                    layout_s_6.setVisibility(View.VISIBLE);
                    layout_s_7.setVisibility(View.VISIBLE);
                    layout_s_8.setVisibility(View.GONE);
                    break;
                case 8:
                    layout_s_1.setVisibility(View.VISIBLE);
                    layout_s_2.setVisibility(View.VISIBLE);
                    layout_s_3.setVisibility(View.VISIBLE);
                    layout_s_4.setVisibility(View.VISIBLE);
                    layout_s_5.setVisibility(View.VISIBLE);
                    layout_s_6.setVisibility(View.VISIBLE);
                    layout_s_7.setVisibility(View.VISIBLE);
                    layout_s_8.setVisibility(View.VISIBLE);
                    break;
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
    private void readCourses_3(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_3.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 3").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_3.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_3.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void readCourses_4(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_4.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 4").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_4.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_4.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void readCourses_5(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_5.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 5").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_5.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_5.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void readCourses_6(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_6.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 6").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_6.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_6.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void readCourses_7(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_7.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 7").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_7.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_7.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void readCourses_8(String nameSch) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference coursesReference = FirebaseDatabase.getInstance().getReference()
                    .child("Students").child(uid).child("Schools");

            coursesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseList_8.clear();
                    // Clear the courseList before adding new data
                    for (DataSnapshot schoolSnapshot : snapshot.getChildren()) {
                        // Retrieve courses only for the school with ID "DUC"
                        for (DataSnapshot courseSnapshot : schoolSnapshot.child("Courses").child(nameSch).child("Semester 8").getChildren()) {
                            SemCourseFormData courseData = courseSnapshot.getValue(SemCourseFormData.class);

                            if (courseData != null) {
                                courseList_8.add(courseData);
                            }
                        }
                    }
                    // Initialize and set adapter to the RecyclerView here
                    adapterCourses_8.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
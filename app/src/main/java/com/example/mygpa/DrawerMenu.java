package com.example.mygpa;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DrawerMenu extends AppCompatActivity {
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;


    private FirebaseAuth mAuth;
    private DatabaseReference mSchoolData, mCoursesData, mCoursesData_2, mCoursesData_3, mCoursesData_4, mCoursesData_5, mCoursesData_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);


        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DashboardHome()).commit();

        }

        replaceFragment(new DashboardHome());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_dash) {
                replaceFragment(new DashboardHome());
            } else if (item.getItemId() == R.id.reports_bottom) {
                replaceFragment(new ReportsHistory());
            } else if (item.getItemId() == R.id.chart_mode) {
                replaceFragment(new Analysis());
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(new Settings());
            }
            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout addCourse = dialog.findViewById(R.id.add_course);
        LinearLayout shortsLayout = dialog.findViewById(R.id.cal_gpa);
        LinearLayout addRecord = dialog.findViewById(R.id.add_record);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showToast("Add GPA2");
            }
        });
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AddRecordPopup();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        setupDialogAppearance(dialog);
    }

    private void AddRecordPopup() {

        // Create a new Dialog
        Dialog dialog = new Dialog(this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the content view to your custom layout (addgparecord_popup)
        dialog.setContentView(R.layout.addgparecord_popup);

        final HorizontalScrollView horizontalScrollView = dialog.findViewById(R.id.horizontal_view);
        final LinearLayout schoolInfoView = dialog.findViewById(R.id.School_Info);
        final LinearLayout courseInfoView = dialog.findViewById(R.id.Course_Info);

        Button nextButton = dialog.findViewById(R.id.nextScrollButton);
        Button previousButton = dialog.findViewById(R.id.backScrollButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button addRecordButton = dialog.findViewById(R.id.addButton);

        // Define a local inner class
        class recAddClass {
            private SchoolFormData AddSchoolPopup() {

                final EditText schoolName = dialog.findViewById(R.id.schoolNameEditText);
                final EditText programmeName = dialog.findViewById(R.id.programEditText);
                final EditText startDate = dialog.findViewById(R.id.startDateEditText);
                final EditText endDate = dialog.findViewById(R.id.endDateEditText);
                final EditText semNum = dialog.findViewById(R.id.semestersEditText);
                final RadioGroup gpaRadioGroup = dialog.findViewById(R.id.gpaRadioGroup);
                final RadioButton radio4_0 = dialog.findViewById(R.id.radio_4_0);
                final RadioButton radio5_0 = dialog.findViewById(R.id.radio_5_0);

                String nameOfSchool = schoolName.getText().toString().trim();
                String programme = programmeName.getText().toString().trim();
                String dateStart = startDate.getText().toString().trim();
                String dateEnd = endDate.getText().toString().trim();
                String semNumber = semNum.getText().toString().trim();
                int selectedRadioButtonId = gpaRadioGroup.getCheckedRadioButtonId();
                String scale4 = radio4_0.getText().toString().trim();
                String scale5 = radio5_0.getText().toString().trim();


                if (TextUtils.isEmpty(nameOfSchool)) {
                    schoolName.setError("Enter Name of School");
                    return null;
                }
                if (TextUtils.isEmpty(programme)) {
                    programmeName.setError("Specify Program of Study");
                    return null;
                }
                if (TextUtils.isEmpty(dateStart)) {
                    startDate.setError("Specify Start Date");
                    return null;
                }
                if (TextUtils.isEmpty(dateEnd)) {
                    endDate.setError("Specify End Date");
                    return null;
                }

                String selectedGpaScale = "";

                if (selectedRadioButtonId == radio4_0.getId()) {
                    selectedGpaScale = "4.0";
                } else if (selectedRadioButtonId == radio5_0.getId()) {
                    selectedGpaScale = "5.0";
                } else {
                    Toast.makeText(DrawerMenu.this, "Select GPA scale", Toast.LENGTH_SHORT).show();
                }
                scrL();

                SchoolFormData data = new SchoolFormData(nameOfSchool, programme, dateStart, dateEnd, semNumber, selectedGpaScale);
                return data;
            }

            private void scrL() {
                // Scroll to the Course Info section
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                courseInfoView.showContextMenu();
            }

            private SemCourseFormData AddCoursePopup() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_1 = dialog.findViewById(R.id.course_name_1);
                final EditText courseCode_1 = dialog.findViewById(R.id.course_code_1);
                final EditText courseScore_1 = dialog.findViewById(R.id.course_score_1);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_1 = courseName_1.getText().toString().trim();
                String codeCourse_1 = courseCode_1.getText().toString().trim();
                String scoreCourse_1 = courseScore_1.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_1)) {
                    courseName_1.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_1)) {
                    courseCode_1.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse = new SemCourseFormData(headingSem, nameCourse_1, codeCourse_1, scoreCourse_1);
                return dataSemCourse;

            }

            private SemCourseFormData AddCoursePopup_2() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_2 = dialog.findViewById(R.id.course_name_2);
                final EditText courseCode_2 = dialog.findViewById(R.id.course_code_2);
                final EditText courseScore_2 = dialog.findViewById(R.id.course_score_2);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_2 = courseName_2.getText().toString().trim();
                String codeCourse_2 = courseCode_2.getText().toString().trim();
                String scoreCourse_2 = courseScore_2.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_2)) {
                    courseName_2.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_2)) {
                    courseCode_2.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse_2 = new SemCourseFormData(headingSem, nameCourse_2, codeCourse_2, scoreCourse_2);
                return dataSemCourse_2;

            }

            private SemCourseFormData AddCoursePopup_3() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_3 = dialog.findViewById(R.id.course_name_3);
                final EditText courseCode_3 = dialog.findViewById(R.id.course_code_3);
                final EditText courseScore_3 = dialog.findViewById(R.id.course_score_3);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_3 = courseName_3.getText().toString().trim();
                String codeCourse_3 = courseCode_3.getText().toString().trim();
                String scoreCourse_3 = courseScore_3.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_3)) {
                    courseName_3.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_3)) {
                    courseCode_3.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse_3 = new SemCourseFormData(headingSem, nameCourse_3, codeCourse_3, scoreCourse_3);
                return dataSemCourse_3;

            }

            private SemCourseFormData AddCoursePopup_4() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_4 = dialog.findViewById(R.id.course_name_4);
                final EditText courseCode_4 = dialog.findViewById(R.id.course_code_4);
                final EditText courseScore_4 = dialog.findViewById(R.id.course_score_4);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_4 = courseName_4.getText().toString().trim();
                String codeCourse_4 = courseCode_4.getText().toString().trim();
                String scoreCourse_4 = courseScore_4.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_4)) {
                    courseName_4.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_4)) {
                    courseCode_4.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse_4 = new SemCourseFormData(headingSem, nameCourse_4, codeCourse_4, scoreCourse_4);
                return dataSemCourse_4;

            }

            private SemCourseFormData AddCoursePopup_5() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_5 = dialog.findViewById(R.id.course_name_5);
                final EditText courseCode_5 = dialog.findViewById(R.id.course_code_5);
                final EditText courseScore_5 = dialog.findViewById(R.id.course_score_5);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_5 = courseName_5.getText().toString().trim();
                String codeCourse_5 = courseCode_5.getText().toString().trim();
                String scoreCourse_5 = courseScore_5.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_5)) {
                    courseName_5.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_5)) {
                    courseCode_5.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse_5 = new SemCourseFormData(headingSem, nameCourse_5, codeCourse_5, scoreCourse_5);
                return dataSemCourse_5;

            }

            private SemCourseFormData AddCoursePopup_6() {


                final TextView semHeading = dialog.findViewById(R.id.semHead);
                final EditText courseName_6 = dialog.findViewById(R.id.course_name_6);
                final EditText courseCode_6 = dialog.findViewById(R.id.course_code_6);
                final EditText courseScore_6 = dialog.findViewById(R.id.course_score_6);

                String headingSem = semHeading.getText().toString().trim();
                String nameCourse_6 = courseName_6.getText().toString().trim();
                String codeCourse_6 = courseCode_6.getText().toString().trim();
                String scoreCourse_6 = courseScore_6.getText().toString().trim();


                if (TextUtils.isEmpty(headingSem)) {
                    semHeading.setError("Enter Semester Number");
                    return null;
                }
                if (TextUtils.isEmpty(nameCourse_6)) {
                    courseName_6.setError("Enter Course Title");
                    return null;
                }
                if (TextUtils.isEmpty(codeCourse_6)) {
                    courseCode_6.setError("Enter Course Code");
                    return null;
                }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                SemCourseFormData dataSemCourse_6 = new SemCourseFormData(headingSem, nameCourse_6, codeCourse_6, scoreCourse_6);
                return dataSemCourse_6;

            }

            private void pushData() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uid = mUser.getUid();

                EditText schoolName = dialog.findViewById(R.id.schoolNameEditText);
                String nameOfSchool = schoolName.getText().toString().trim();

                TextView semHeading = dialog.findViewById(R.id.semHead);
                String headingSem = semHeading.getText().toString().trim();

                EditText programmeName = dialog.findViewById(R.id.programEditText);
                String programme = programmeName.getText().toString().trim();

                EditText courseName_1 = dialog.findViewById(R.id.course_name_1);
                String nameCourse_1 = courseName_1.getText().toString().trim();

                EditText courseName_2 = dialog.findViewById(R.id.course_name_2);
                String nameCourse_2 = courseName_2.getText().toString().trim();

                EditText courseName_3 = dialog.findViewById(R.id.course_name_3);
                String nameCourse_3 = courseName_3.getText().toString().trim();

                EditText courseName_4 = dialog.findViewById(R.id.course_name_4);
                String nameCourse_4 = courseName_4.getText().toString().trim();

                EditText courseName_5 = dialog.findViewById(R.id.course_name_5);
                String nameCourse_5 = courseName_5.getText().toString().trim();

                EditText courseName_6 = dialog.findViewById(R.id.course_name_6);
                String nameCourse_6 = courseName_6.getText().toString().trim();

                DatabaseReference mStudentData = FirebaseDatabase.getInstance().getReference().child("Students").child(uid);
                mSchoolData = mStudentData.child("Schools").child(nameOfSchool); // Generate a unique school ID
                mCoursesData = mSchoolData.child("Courses").child(nameOfSchool).child(headingSem); // Generate a unique semester ID

                // Pushing school data
                mSchoolData.setValue(AddSchoolPopup());

                // Pushing course data with unique keys
                DatabaseReference mCoursesData_1 = mCoursesData.push();
                mCoursesData_1.setValue(AddCoursePopup());

                mCoursesData_2 = mCoursesData.push();
                mCoursesData_2.setValue(AddCoursePopup_2());

                mCoursesData_3 = mCoursesData.push();
                mCoursesData_3.setValue(AddCoursePopup_3());

                mCoursesData_4 = mCoursesData.push();
                mCoursesData_4.setValue(AddCoursePopup_4());

                mCoursesData_5 = mCoursesData.push();
                mCoursesData_5.setValue(AddCoursePopup_5());

                mCoursesData_6 = mCoursesData.push();
                mCoursesData_6.setValue(AddCoursePopup_6());

                // After saving all courses, listen for completion
                mCoursesData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // All courses have been added
                        Log.d("Firebase", "Courses Data Added Successfully!");
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error occurred while saving data
                        Log.e("Firebase", "Error saving data: " + databaseError.getMessage());
                    }
                });
            }
        }

            recAddClass inner = new recAddClass();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.AddSchoolPopup();
            }

        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll to the School Info section
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                schoolInfoView.showContextMenu();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.AddCoursePopup();
                inner.pushData();
                //TOASTERS
                Toast.makeText(DrawerMenu.this, "Record Data Added", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();

    }



    private void showToast(String message) {
        Toast.makeText(DrawerMenu.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupDialogAppearance(Dialog dialog) {
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setupDialogAppearanceForm(Dialog dialog) {
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }


}
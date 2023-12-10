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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DrawerMenu extends AppCompatActivity {
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;


    private FirebaseAuth mAuth;
    private DatabaseReference mSchoolData,mSemesterData,mCoursesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);




        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        if (savedInstanceState == null){
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

     fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showBottomDialog();
            }
        });

    }



    private void replaceFragment(Fragment fragment){

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

    private void AddRecordPopup(){

        // Create a new Dialog
        Dialog dialog = new Dialog(this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the content view to your custom layout (addgparecord_popup)
        dialog.setContentView(R.layout.addgparecord_popup);

        final  HorizontalScrollView horizontalScrollView = dialog.findViewById(R.id.horizontal_view);
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

                DatabaseReference mStudentData = FirebaseDatabase.getInstance().getReference().child("Students").child(uid);
                mSchoolData = mStudentData.child("Schools").child(nameOfSchool); // Generate a unique school ID
                mCoursesData = mSchoolData.child("Courses").child(nameOfSchool); // Generate a unique semester ID

                // Pushing school data
                mSchoolData.setValue(AddSchoolPopup());
//.child(programme).child(headingSem).child(nameCourse_1)
                // Pushing course data
                mCoursesData.setValue(AddCoursePopup())
                        .addOnSuccessListener(aVoid -> {
                            // Data successfully saved
                            Log.d("Firebase", "Courses Data Added Successfully!");
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while saving data
                            Log.e("Firebase", "Error saving data: " + e.getMessage());
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
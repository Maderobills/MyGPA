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

import java.util.Arrays;
import java.util.Objects;

public class DrawerMenu extends AppCompatActivity {
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;


    private FirebaseAuth mAuth;
    private DatabaseReference mSchoolData, mCoursesData, mCoursesData_2, mCoursesData_3, mCoursesData_4, mCoursesData_5, mCoursesData_6;
    private DatabaseReference mCoursesData_s2, mCoursesData_2_s2, mCoursesData_3_s2, mCoursesData_4_s2, mCoursesData_5_s2, mCoursesData_6_s2;

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

        /*final HorizontalScrollView horizontalScrollView = dialog.findViewById(R.id.horizontal_view);*/
        final LinearLayout schoolInfoView = dialog.findViewById(R.id.School_Info);
        final LinearLayout courseInfoView = dialog.findViewById(R.id.Course_Info);
        final LinearLayout courseInfoView_2 = dialog.findViewById(R.id.Course_Info_2);

        Button nextButton = dialog.findViewById(R.id.nextScrollButton);
        Button prevSchButton = dialog.findViewById(R.id.schScrollButton);

        Button scrollSem_2 = dialog.findViewById(R.id.nextScrollButton_1);
        Button scrollBckSem_1 = dialog.findViewById(R.id.backScrollSem_1);
        Button addRecordButton = dialog.findViewById(R.id.addButton);

        Button scrollSem_3 = dialog.findViewById(R.id.nextScrollButton_2);
        Button addRecFrmSem_2 =dialog.findViewById(R.id.addButton_s2);

        final String[] selectedGpaScale = {""};

        // Define a local inner class
        class recAddClass {
            private SchoolFormData AddSchoolPopup() {

                final EditText schoolName = dialog.findViewById(R.id.schoolNameEditText);
                final EditText indexNumber = dialog.findViewById(R.id.indextNumberEditText);
                final EditText programmeName = dialog.findViewById(R.id.programEditText);
                final EditText startDate = dialog.findViewById(R.id.startDateEditText);
                final EditText endDate = dialog.findViewById(R.id.endDateEditText);
                final EditText semNum = dialog.findViewById(R.id.semestersEditText);
                final RadioGroup gpaRadioGroup = dialog.findViewById(R.id.gpaRadioGroup);
                final RadioButton radio4_0 = dialog.findViewById(R.id.radio_4_0);
                final RadioButton radio5_0 = dialog.findViewById(R.id.radio_5_0);

                String nameOfSchool = schoolName.getText().toString().trim();
                String idNumber = indexNumber.getText().toString().trim();
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
                if (TextUtils.isEmpty(idNumber)) {
                    indexNumber.setError("Enter Index No.");
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
                if (TextUtils.isEmpty(semNumber)) {
                    semNum.setError("Specify ");
                    return null;
                }



                if (selectedRadioButtonId == radio4_0.getId()) {
                    selectedGpaScale[0] = "4.0";
                } else if (selectedRadioButtonId == radio5_0.getId()) {
                    selectedGpaScale[0] = "5.0";
                } else {
                    Toast.makeText(DrawerMenu.this, "Select GPA scale", Toast.LENGTH_SHORT).show();
                }

                naviSem_1();
                inner_1.AddCoursePopup(Arrays.toString(selectedGpaScale));


                SchoolFormData data = new SchoolFormData(nameOfSchool, idNumber, programme, dateStart, dateEnd, semNumber, selectedGpaScale[0]);
                return data;
            }


            private void naviSem_1(){

                // Hide School_Info layout
                schoolInfoView.setVisibility(View.GONE);
                // Show Course_Info layout
                courseInfoView.setVisibility(View.VISIBLE);
                courseInfoView_2.setVisibility(View.GONE);
                EditText sems = dialog.findViewById(R.id.semestersEditText);
                int semVal = Integer.parseInt(sems.getText().toString().trim());
                if (semVal > 0 && semVal <= 8) {
                    // Check different cases based on the number of semesters
                    switch (semVal) {
                        case 1:
                            scrollSem_2.setVisibility(View.GONE);
                            scrollSem_3.setVisibility(View.GONE);

                            break;
                        case 2:
                            scrollSem_2.setVisibility(View.VISIBLE);
                            scrollSem_3.setVisibility(View.GONE);
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
            private void naviSem_2(){
                // Hide School_Info layout
                schoolInfoView.setVisibility(View.GONE);
                courseInfoView.setVisibility(View.GONE);
                // Show Course_Info layout
                courseInfoView_2.setVisibility(View.VISIBLE);
            }

            private void addCourseDetailSelector(){

                TextView sel1 = dialog.findViewById(R.id.sel_1);
                TextView sel2 = dialog.findViewById(R.id.sel_2);
                TextView sel3 = dialog.findViewById(R.id.sel_3);
                TextView sel4 = dialog.findViewById(R.id.sel_4);
                TextView sel5 = dialog.findViewById(R.id.sel_5);
                TextView sel6 = dialog.findViewById(R.id.sel_6);
                TextView sel7 = dialog.findViewById(R.id.sel_7);
                TextView sel8 = dialog.findViewById(R.id.sel_8);

                sel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel1.setTextColor(getResources().getColor(R.color.lavender));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse.viewCourseDetail_1();
                    }
                });

                sel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel2.setTextColor(getResources().getColor(R.color.lavender));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse.viewCourseDetail_2();
                    }
                });
                sel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel3.setTextColor(getResources().getColor(R.color.lavender));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse.viewCourseDetail_3();
                    }
                });
                sel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel4.setTextColor(getResources().getColor(R.color.lavender));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse.viewCourseDetail_4();
                    }
                });
                sel5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel5.setTextColor(getResources().getColor(R.color.lavender));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse.viewCourseDetail_5();
                    }
                });
                sel6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel6.setTextColor(getResources().getColor(R.color.lavender));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_w);


                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse.viewCourseDetail_6();
                    }
                });
                sel7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel7.setTextColor(getResources().getColor(R.color.lavender));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_w);


                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse.viewCourseDetail_7();
                    }
                });
                sel8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel8.setTextColor(getResources().getColor(R.color.lavender));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse.viewCourseDetail_8();
                    }
                });

            }
            private void addCourseDetailSelector_2(){

                TextView sel1 = dialog.findViewById(R.id.sel_s2_1);
                TextView sel2 = dialog.findViewById(R.id.sel_s2_2);
                TextView sel3 = dialog.findViewById(R.id.sel_s2_3);
                TextView sel4 = dialog.findViewById(R.id.sel_s2_4);
                TextView sel5 = dialog.findViewById(R.id.sel_s2_5);
                TextView sel6 = dialog.findViewById(R.id.sel_s2_6);
                TextView sel7 = dialog.findViewById(R.id.sel_s2_7);
                TextView sel8 = dialog.findViewById(R.id.sel_s2_8);

                sel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel1.setTextColor(getResources().getColor(R.color.lavender));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse_2.viewCourseDetail_1();
                    }
                });

                sel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel2.setTextColor(getResources().getColor(R.color.lavender));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse_2.viewCourseDetail_2();
                    }
                });
                sel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel3.setTextColor(getResources().getColor(R.color.lavender));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);

                        viewCourse_2.viewCourseDetail_3();
                    }
                });
                sel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel4.setTextColor(getResources().getColor(R.color.lavender));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse_2.viewCourseDetail_4();
                    }
                });
                sel5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel5.setTextColor(getResources().getColor(R.color.lavender));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse_2.viewCourseDetail_5();
                    }
                });
                sel6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel6.setTextColor(getResources().getColor(R.color.lavender));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_w);


                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel7.setTextColor(getResources().getColor(R.color.white));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse_2.viewCourseDetail_6();
                    }
                });
                sel7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel7.setTextColor(getResources().getColor(R.color.lavender));
                        sel7.setBackgroundResource(R.drawable.r_backgrd_w);


                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel8.setTextColor(getResources().getColor(R.color.white));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse_2.viewCourseDetail_7();
                    }
                });
                sel8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel8.setTextColor(getResources().getColor(R.color.lavender));
                        sel8.setBackgroundResource(R.drawable.r_backgrd_w);

                        sel2.setTextColor(getResources().getColor(R.color.white));
                        sel2.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel3.setTextColor(getResources().getColor(R.color.white));
                        sel3.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel4.setTextColor(getResources().getColor(R.color.white));
                        sel4.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel5.setTextColor(getResources().getColor(R.color.white));
                        sel5.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel6.setTextColor(getResources().getColor(R.color.white));
                        sel6.setBackgroundResource(R.drawable.r_backgrd_t);
                        sel1.setTextColor(getResources().getColor(R.color.white));
                        sel1.setBackgroundResource(R.drawable.r_backgrd_t);
                        viewCourse_2.viewCourseDetail_8();
                    }
                });

            }
            class viewCourseDetailInput{
                final LinearLayout viewCourse_1 = dialog.findViewById(R.id.course_details_1);
                final LinearLayout viewCourse_2 = dialog.findViewById(R.id.course_details_2);
                final LinearLayout viewCourse_3 = dialog.findViewById(R.id.course_details_3);
                final LinearLayout viewCourse_4 = dialog.findViewById(R.id.course_details_4);
                final LinearLayout viewCourse_5 = dialog.findViewById(R.id.course_details_5);
                final LinearLayout viewCourse_6 = dialog.findViewById(R.id.course_details_6);

                private void viewCourseDetail_1(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.GONE);
                    viewCourse_3.setVisibility(View.GONE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_2(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.GONE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_3(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_4(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_5(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_6(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);
                }
                private void viewCourseDetail_7(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);

                }
                private void viewCourseDetail_8(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);

                }

            }
            class viewCourseDetailInput_2{
                final LinearLayout viewCourse_1 = dialog.findViewById(R.id.course_details_s2_1);
                final LinearLayout viewCourse_2 = dialog.findViewById(R.id.course_details_s2_2);
                final LinearLayout viewCourse_3 = dialog.findViewById(R.id.course_details_s2_3);
                final LinearLayout viewCourse_4 = dialog.findViewById(R.id.course_details_s2_4);
                final LinearLayout viewCourse_5 = dialog.findViewById(R.id.course_details_s2_5);
                final LinearLayout viewCourse_6 = dialog.findViewById(R.id.course_details_s2_6);

                private void viewCourseDetail_1(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.GONE);
                    viewCourse_3.setVisibility(View.GONE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_2(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.GONE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_3(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.GONE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_4(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.GONE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_5(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.GONE);

                }
                private void viewCourseDetail_6(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);
                }
                private void viewCourseDetail_7(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);

                }
                private void viewCourseDetail_8(){
                    viewCourse_1.setVisibility(View.VISIBLE);
                    viewCourse_2.setVisibility(View.VISIBLE);
                    viewCourse_3.setVisibility(View.VISIBLE);
                    viewCourse_4.setVisibility(View.VISIBLE);
                    viewCourse_5.setVisibility(View.VISIBLE);
                    viewCourse_6.setVisibility(View.VISIBLE);

                }

            }
            class AddCoursesSem_1 {

                private SemCourseFormData AddCoursePopup(String getScale) {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_1 = dialog.findViewById(R.id.course_name_1);
                    final EditText courseCode_1 = dialog.findViewById(R.id.course_code_1);
                    final EditText courseScore_1 = dialog.findViewById(R.id.course_score_1);
                    final EditText creditHours_1 = dialog.findViewById(R.id.credit_hours_1);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_1 = courseName_1.getText().toString().trim();
                    String codeCourse_1 = courseCode_1.getText().toString().trim();
                    String scoreCourse_1 = courseScore_1.getText().toString().trim();
                    String hoursCredit_1 = creditHours_1.getText().toString().trim();

                    double score_1 = Double.parseDouble(scoreCourse_1);
                    double credit_1 = Double.parseDouble(getScale);
                    String gradeP = String.valueOf((score_1 * credit_1)/100);

                    if (TextUtils.isEmpty(headingSem)) {
                        semHeading.setError("Enter Semester Number");
                        return null;
                    }
                    if (TextUtils.isEmpty(nameCourse_1)) {
                        courseName_1.setError("Enter Course Title");
                        return null;
                    }
                    if (TextUtils.isEmpty(gradeP)) {
                        semHeading.setError("Enter Semester Number");
                        return null;
                    }
                    if (TextUtils.isEmpty(codeCourse_1)) {
                        courseCode_1.setError("Enter Course Code");
                        return null;
                    }
                    if (TextUtils.isEmpty(hoursCredit_1)) {
                        creditHours_1.setError("Enter Credit Hours");
                        return null;
                    }
        /*if (TextUtils.isEmpty(scoreCourse_1)){
            courseScore_1.setError("Specify End Date");
            return;
        }*/

                    SemCourseFormData dataSemCourse = new SemCourseFormData(headingSem, nameCourse_1, gradeP, codeCourse_1, scoreCourse_1, hoursCredit_1);
                    return dataSemCourse;

                }

                /*private SemCourseFormData AddCoursePopup_2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_2 = dialog.findViewById(R.id.course_name_2);
                    final EditText courseCode_2 = dialog.findViewById(R.id.course_code_2);
                    final EditText courseScore_2 = dialog.findViewById(R.id.course_score_2);
                    final EditText creditHours_2 = dialog.findViewById(R.id.credit_hours_2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_2 = courseName_2.getText().toString().trim();
                    String codeCourse_2 = courseCode_2.getText().toString().trim();
                    String scoreCourse_2 = courseScore_2.getText().toString().trim();
                    String hoursCredit_2 = creditHours_2.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_2)){
                        creditHours_2.setError("Specify Credit Hours");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_2 = new SemCourseFormData(headingSem, nameCourse_2, codeCourse_2, scoreCourse_2, hoursCredit_2);
                    return dataSemCourse_2;

                }

                private SemCourseFormData AddCoursePopup_3() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_3 = dialog.findViewById(R.id.course_name_3);
                    final EditText courseCode_3 = dialog.findViewById(R.id.course_code_3);
                    final EditText courseScore_3 = dialog.findViewById(R.id.course_score_3);
                    final EditText creditHours_3 = dialog.findViewById(R.id.credit_hours_3);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_3 = courseName_3.getText().toString().trim();
                    String codeCourse_3 = courseCode_3.getText().toString().trim();
                    String scoreCourse_3 = courseScore_3.getText().toString().trim();
                    String hoursCredit_3 = creditHours_3.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_3)){
                        creditHours_3.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_3 = new SemCourseFormData(headingSem, nameCourse_3, codeCourse_3, scoreCourse_3, hoursCredit_3);
                    return dataSemCourse_3;

                }

                private SemCourseFormData AddCoursePopup_4() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_4 = dialog.findViewById(R.id.course_name_4);
                    final EditText courseCode_4 = dialog.findViewById(R.id.course_code_4);
                    final EditText courseScore_4 = dialog.findViewById(R.id.course_score_4);
                    final EditText creditHours_4 = dialog.findViewById(R.id.credit_hours_4);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_4 = courseName_4.getText().toString().trim();
                    String codeCourse_4 = courseCode_4.getText().toString().trim();
                    String scoreCourse_4 = courseScore_4.getText().toString().trim();
                    String hoursCredit_4 = creditHours_4.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_4)){
                        creditHours_4.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_4 = new SemCourseFormData(headingSem, nameCourse_4, codeCourse_4, scoreCourse_4, hoursCredit_4);
                    return dataSemCourse_4;

                }

                private SemCourseFormData AddCoursePopup_5() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_5 = dialog.findViewById(R.id.course_name_5);
                    final EditText courseCode_5 = dialog.findViewById(R.id.course_code_5);
                    final EditText courseScore_5 = dialog.findViewById(R.id.course_score_5);
                    final EditText creditHours_5 = dialog.findViewById(R.id.credit_hours_5);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_5 = courseName_5.getText().toString().trim();
                    String codeCourse_5 = courseCode_5.getText().toString().trim();
                    String scoreCourse_5 = courseScore_5.getText().toString().trim();
                    String hoursCredit_5 = creditHours_5.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_5)){
                        creditHours_5.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_5 = new SemCourseFormData(headingSem, nameCourse_5, codeCourse_5, scoreCourse_5, hoursCredit_5);
                    return dataSemCourse_5;

                }

                private SemCourseFormData AddCoursePopup_6() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead);
                    final EditText courseName_6 = dialog.findViewById(R.id.course_name_6);
                    final EditText courseCode_6 = dialog.findViewById(R.id.course_code_6);
                    final EditText courseScore_6 = dialog.findViewById(R.id.course_score_6);
                    final EditText creditHours_6 = dialog.findViewById(R.id.credit_hours_6);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_6 = courseName_6.getText().toString().trim();
                    String codeCourse_6 = courseCode_6.getText().toString().trim();
                    String scoreCourse_6 = courseScore_6.getText().toString().trim();
                    String hoursCredit_6 = creditHours_6.getText().toString().trim();


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
                    if (TextUtils.isEmpty(hoursCredit_6)){
                        creditHours_6.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_6 = new SemCourseFormData(headingSem, nameCourse_6, codeCourse_6, scoreCourse_6, hoursCredit_6);
                    return dataSemCourse_6;

                }*/
            }
            class AddCoursesSem_2 {
               /* private SemCourseFormData AddCoursePopup_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_1 = dialog.findViewById(R.id.course_name_1_s2);
                    final EditText courseCode_1 = dialog.findViewById(R.id.course_code_1_s2);
                    final EditText courseScore_1 = dialog.findViewById(R.id.course_score_1_s2);
                    final EditText creditHours_1 = dialog.findViewById(R.id.credit_hours_1_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_1 = courseName_1.getText().toString().trim();
                    String codeCourse_1 = courseCode_1.getText().toString().trim();
                    String scoreCourse_1 = courseScore_1.getText().toString().trim();
                    String hoursCredit_1 = creditHours_1.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_1)){
                        creditHours_1.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_s2 = new SemCourseFormData(headingSem, nameCourse_1, codeCourse_1, scoreCourse_1, hoursCredit_1);
                    return dataSemCourse_s2;

                }

                private SemCourseFormData AddCoursePopup_2_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_2 = dialog.findViewById(R.id.course_name_2_s2);
                    final EditText courseCode_2 = dialog.findViewById(R.id.course_code_2_s2);
                    final EditText courseScore_2 = dialog.findViewById(R.id.course_score_2_s2);
                    final EditText creditHours_2 = dialog.findViewById(R.id.credit_hours_2_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_2 = courseName_2.getText().toString().trim();
                    String codeCourse_2 = courseCode_2.getText().toString().trim();
                    String scoreCourse_2 = courseScore_2.getText().toString().trim();
                    String hoursCredit_2 = creditHours_2.getText().toString().trim();

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
                    if (TextUtils.isEmpty(hoursCredit_2)){
                        creditHours_2.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_2_s2 = new SemCourseFormData(headingSem, nameCourse_2, codeCourse_2, scoreCourse_2, hoursCredit_2);
                    return dataSemCourse_2_s2;

                }

                private SemCourseFormData AddCoursePopup_3_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_3 = dialog.findViewById(R.id.course_name_3_s2);
                    final EditText courseCode_3 = dialog.findViewById(R.id.course_code_3_s2);
                    final EditText courseScore_3 = dialog.findViewById(R.id.course_score_3_s2);
                    final EditText creditHours_3 = dialog.findViewById(R.id.credit_hours_3_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_3 = courseName_3.getText().toString().trim();
                    String codeCourse_3 = courseCode_3.getText().toString().trim();
                    String scoreCourse_3 = courseScore_3.getText().toString().trim();
                    String hoursCredit_3 = creditHours_3.getText().toString().trim();


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
                    if (TextUtils.isEmpty(hoursCredit_3)){
                        creditHours_3.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_3_s2 = new SemCourseFormData(headingSem, nameCourse_3, codeCourse_3, scoreCourse_3, hoursCredit_3);
                    return dataSemCourse_3_s2;

                }

                private SemCourseFormData AddCoursePopup_4_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_4 = dialog.findViewById(R.id.course_name_4_s2);
                    final EditText courseCode_4 = dialog.findViewById(R.id.course_code_4_s2);
                    final EditText courseScore_4 = dialog.findViewById(R.id.course_score_4_s2);
                    final EditText creditHours_4 = dialog.findViewById(R.id.credit_hours_4_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_4 = courseName_4.getText().toString().trim();
                    String codeCourse_4 = courseCode_4.getText().toString().trim();
                    String scoreCourse_4 = courseScore_4.getText().toString().trim();
                    String hoursCredit_4 = creditHours_4.getText().toString().trim();


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
                    if (TextUtils.isEmpty(hoursCredit_4)){
                        creditHours_4.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_4_s2 = new SemCourseFormData(headingSem, nameCourse_4, codeCourse_4, scoreCourse_4, hoursCredit_4);
                    return dataSemCourse_4_s2;

                }

                private SemCourseFormData AddCoursePopup_5_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_5 = dialog.findViewById(R.id.course_name_5_s2);
                    final EditText courseCode_5 = dialog.findViewById(R.id.course_code_5_s2);
                    final EditText courseScore_5 = dialog.findViewById(R.id.course_score_5_s2);
                    final EditText creditHours_5 = dialog.findViewById(R.id.credit_hours_5_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_5 = courseName_5.getText().toString().trim();
                    String codeCourse_5 = courseCode_5.getText().toString().trim();
                    String scoreCourse_5 = courseScore_5.getText().toString().trim();
                    String hoursCredit_5 = creditHours_5.getText().toString().trim();


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
                    if (TextUtils.isEmpty(hoursCredit_5)){
                        creditHours_5.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_5_s2 = new SemCourseFormData(headingSem, nameCourse_5, codeCourse_5, scoreCourse_5, hoursCredit_5);
                    return dataSemCourse_5_s2;

                }

                private SemCourseFormData AddCoursePopup_6_s2() {


                    final TextView semHeading = dialog.findViewById(R.id.semHead_2);
                    final EditText courseName_6 = dialog.findViewById(R.id.course_name_6_s2);
                    final EditText courseCode_6 = dialog.findViewById(R.id.course_code_6_s2);
                    final EditText courseScore_6 = dialog.findViewById(R.id.course_score_6_s2);
                    final EditText creditHours_6 = dialog.findViewById(R.id.credit_hours_6_s2);

                    String headingSem = semHeading.getText().toString().trim();
                    String nameCourse_6 = courseName_6.getText().toString().trim();
                    String codeCourse_6 = courseCode_6.getText().toString().trim();
                    String scoreCourse_6 = courseScore_6.getText().toString().trim();
                    String hoursCredit_6 = creditHours_6.getText().toString().trim();


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
                    if (TextUtils.isEmpty(hoursCredit_6)){
                        creditHours_6.setError("Specify End Date");
                        return null;
                    }

                    SemCourseFormData dataSemCourse_6_s2 = new SemCourseFormData(headingSem, nameCourse_6, codeCourse_6, scoreCourse_6, hoursCredit_6);
                    return dataSemCourse_6_s2;

                }*/
            }
            AddCoursesSem_1 inner_1 = new AddCoursesSem_1();
            AddCoursesSem_2 inner_2 = new AddCoursesSem_2();
            viewCourseDetailInput viewCourse = new viewCourseDetailInput();
            viewCourseDetailInput_2 viewCourse_2 = new viewCourseDetailInput_2();


            private void pushData() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uid = mUser.getUid();

                EditText schoolName = dialog.findViewById(R.id.schoolNameEditText);
                String nameOfSchool = schoolName.getText().toString().trim();

                TextView semHeading = dialog.findViewById(R.id.semHead);
                String headingSem = semHeading.getText().toString().trim();
                TextView semHeading_2 = dialog.findViewById(R.id.semHead_2);
                String headingSem_2 = semHeading_2.getText().toString().trim();



                /*EditText programmeName = dialog.findViewById(R.id.programEditText);
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
                String nameCourse_6 = courseName_6.getText().toString().trim();*/

                DatabaseReference mStudentData = FirebaseDatabase.getInstance().getReference().child("Students").child(uid);
                mSchoolData = mStudentData.child("Schools").child(nameOfSchool); // Generate a unique school ID
                mCoursesData = mSchoolData.child("Courses").child(nameOfSchool).child(headingSem); // Generate a unique semester ID
                mCoursesData_s2 = mSchoolData.child("Courses").child(nameOfSchool).child(headingSem_2); // Generate a unique semester ID

                // Pushing school data
                mSchoolData.setValue(AddSchoolPopup());

                // Pushing course data with unique keys
                DatabaseReference mCoursesData_1 = mCoursesData.push();
                mCoursesData_1.setValue(inner_1.AddCoursePopup(Arrays.toString(selectedGpaScale)));

                /*mCoursesData_2 = mCoursesData.push();
                mCoursesData_2.setValue(inner_1.AddCoursePopup_2());

                mCoursesData_3 = mCoursesData.push();
                mCoursesData_3.setValue(inner_1.AddCoursePopup_3());

                mCoursesData_4 = mCoursesData.push();
                mCoursesData_4.setValue(inner_1.AddCoursePopup_4());

                mCoursesData_5 = mCoursesData.push();
                mCoursesData_5.setValue(inner_1.AddCoursePopup_5());

                mCoursesData_6 = mCoursesData.push();
                mCoursesData_6.setValue(inner_1.AddCoursePopup_6());

                ///SEM_2
                DatabaseReference mCoursesData_2 = mCoursesData_s2.push();
                mCoursesData_2.setValue(inner_2.AddCoursePopup_s2());

                mCoursesData_2_s2 = mCoursesData_s2.push();
                mCoursesData_2_s2.setValue(inner_2.AddCoursePopup_2_s2());

                mCoursesData_3_s2 = mCoursesData_s2.push();
                mCoursesData_3_s2.setValue(inner_2.AddCoursePopup_3_s2());

                mCoursesData_4_s2 = mCoursesData_s2.push();
                mCoursesData_4_s2.setValue(inner_2.AddCoursePopup_4_s2());

                mCoursesData_5_s2 = mCoursesData_s2.push();
                mCoursesData_5_s2.setValue(inner_2.AddCoursePopup_5_s2());

                mCoursesData_6_s2 = mCoursesData_s2.push();
                mCoursesData_6_s2.setValue(inner_2.AddCoursePopup_6_s2());*/

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
        prevSchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show School_Info layout
                schoolInfoView.setVisibility(View.VISIBLE);
                // Hide Course_Info layout
                courseInfoView.setVisibility(View.GONE);
                courseInfoView_2.setVisibility(View.GONE);


            }
        });
        scrollSem_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.naviSem_2();
            }
        });
        scrollBckSem_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.naviSem_1();
            }
        });

        addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.pushData();
                //TOASTERS
                Toast.makeText(DrawerMenu.this, "Record Data Added", Toast.LENGTH_SHORT).show();
            }
        });
        addRecFrmSem_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inner.pushData();
                //TOASTERS
                Toast.makeText(DrawerMenu.this, "Record Data Added", Toast.LENGTH_SHORT).show();
            }
        });

        inner.addCourseDetailSelector();
        inner.addCourseDetailSelector_2();

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
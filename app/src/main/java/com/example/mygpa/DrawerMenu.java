package com.example.mygpa;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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

import java.text.DateFormat;
import java.util.Date;

public class DrawerMenu extends AppCompatActivity {
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private DatabaseReference mSchoolData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);




        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mSchoolData = FirebaseDatabase.getInstance().getReference().child("Schools").child("Class Year").child(uid);


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

        LinearLayout videoLayout = dialog.findViewById(R.id.add_course);
        LinearLayout shortsLayout = dialog.findViewById(R.id.cal_gpa);
        LinearLayout addRecord = dialog.findViewById(R.id.add_record);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showToast("Add GPA");
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the content view to your custom layout (addgparecord_popup)
        dialog.setContentView(R.layout.addgparecord_popup);

        final EditText schoolName = dialog.findViewById(R.id.schoolNameEditText);
        final EditText programmeName = dialog.findViewById(R.id.programEditText);
        final EditText startDate = dialog.findViewById(R.id.startDateEditText);
        final EditText endDate = dialog.findViewById(R.id.endDateEditText);
        final EditText semNum = dialog.findViewById(R.id.semestersEditText);
        final RadioGroup gpaRadioGroup = dialog.findViewById(R.id.gpaRadioGroup);
        final RadioButton radio4_0 = dialog.findViewById(R.id.radio_4_0);
        final RadioButton radio5_0 = dialog.findViewById(R.id.radio_5_0);

        // Find the "cancelButton" and set its click listener to dismiss the dialog
        Button closeButton = dialog.findViewById(R.id.cancelButton);
        Button saveSchoolButton = dialog.findViewById(R.id.addButton);

        saveSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameOfSchool = schoolName.getText().toString().trim();
                String programme = programmeName.getText().toString().trim();
                String dateStart = startDate.getText().toString().trim();
                String dateEnd = endDate.getText().toString().trim();
                String semNumber = semNum.getText().toString().trim();
                int selectedRadioButtonId = gpaRadioGroup.getCheckedRadioButtonId();
                String scale4 = radio4_0.getText().toString().trim();
                String scale5 = radio5_0.getText().toString().trim();


                if (TextUtils.isEmpty(nameOfSchool)){
                    schoolName.setError("Enter Name of School");
                    return;
                }
                if (TextUtils.isEmpty(programme)){
                    programmeName.setError("Specify Program of Study");
                    return;
                }
                if (TextUtils.isEmpty(dateStart)){
                    startDate.setError("Specify Start Date");
                    return;
                }
                if (TextUtils.isEmpty(dateEnd)){
                    endDate.setError("Specify End Date");
                    return;
                }

                String selectedGpaScale = "";

                if (selectedRadioButtonId == radio4_0.getId()) {
                    selectedGpaScale = "4.0";
                } else if (selectedRadioButtonId == radio5_0.getId()) {
                    selectedGpaScale = "5.0";
                } else {
                    Toast.makeText(DrawerMenu.this, "Select GPA scale", Toast.LENGTH_SHORT).show();
                    return;
                }


                //int numSem = Integer.parseInt(semNumber);
                //float scaleP = Float.parseFloat(selectedGpaScale);


                SchoolFormData data = new SchoolFormData(nameOfSchool, programme, dateStart, dateEnd, semNumber, selectedGpaScale);

                String id = mSchoolData.push().getKey();
                mSchoolData.child(id).setValue(data);

                Toast.makeText(DrawerMenu.this, "School Data Added", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Call a method to set up the appearance and behavior of the dialog (if needed)
        setupDialogAppearanceForm(dialog);

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
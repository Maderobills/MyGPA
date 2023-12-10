package com.example.mygpa;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
class SchoolsAdaptor extends RecyclerView.Adapter<SchoolsAdaptor.SchoolViewHolder> {
    private List<SchoolFormData> schoolsDataList;
    private Context contextSchool;
    private FirebaseAuth mAuth;
    String schName, gpaSc, nSem, dateS, dateE, idNo;

    SemCoursesAdaptor adapterCourses;
    ArrayList<SemCourseFormData> courseList;
    RecyclerView courseRecycler;

    public SchoolsAdaptor(ArrayList<SchoolFormData> schoolsDataList, Context contextSchool) {
        this.schoolsDataList = schoolsDataList;
        this.contextSchool = contextSchool;
    }

    @NonNull
    @Override
    public SchoolsAdaptor.SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schools_recycler_data, parent, false);
        return new SchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        SchoolFormData schoolData = schoolsDataList.get(position);
        holder.schoolName.setText(schoolData.getSchoolName());
        holder.program.setText(schoolData.getProgram());
        holder.scale.setText(schoolData.getGpaScale());
        holder.semesters.setText(schoolData.getNumberOfSemesters());

        boolean isVisible = schoolData.isVisibility();
//        holder.courseView.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the school data for the clicked item
                schName = schoolData.getSchoolName();
                gpaSc = schoolData.getGpaScale();
                nSem = schoolData.getNumberOfSemesters();
                dateS = schoolData.getStartDate();
                dateE = schoolData.getEndDate();

                // Navigate to SemesterCourses fragment and pass the school data
                FragmentManager fragmentManager = ((AppCompatActivity) contextSchool).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SemesterCourses semesterCoursesFragment = new SemesterCourses();
                semesterCoursesFragment.setSchoolData(schName, gpaSc, nSem, dateS, dateE);

                fragmentTransaction.replace(R.id.frame_layout, semesterCoursesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolsDataList.size();
    }

    public void updateData(List<SchoolFormData> updatedData) {
        schoolsDataList = updatedData;
        notifyDataSetChanged();
    }

    public class SchoolViewHolder extends RecyclerView.ViewHolder {

        TextView schoolName;
        TextView program;
        TextView scale;
        TextView semesters;
        LinearLayout schoolLayout;

        public SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            schoolLayout = itemView.findViewById(R.id.school_layout);
            schoolName = itemView.findViewById(R.id.School_name);
            program = itemView.findViewById(R.id.type_txt_income);
            scale = itemView.findViewById(R.id.scale_type);
            semesters = itemView.findViewById(R.id.sem_number);

        }
    }


}



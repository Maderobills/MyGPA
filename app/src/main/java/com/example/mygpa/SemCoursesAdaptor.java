package com.example.mygpa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SemCoursesAdaptor extends RecyclerView.Adapter<SemCoursesAdaptor.ViewHolder> {

    private List<SemCourseFormData> semCoursesList;
    private List<SchoolFormData> schoolList;
    private Context contextSemCourses;

    private String storedGpaSc;

    public SemCoursesAdaptor(Context contextSemCourses, List<SemCourseFormData> semCoursesList) {
        this.contextSemCourses = contextSemCourses;
        this.semCoursesList = semCoursesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextSemCourses).inflate(R.layout.courses_recycler_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SemCourseFormData semCourse = semCoursesList.get(position);

        // Set data to your views
        //holder.semHeadingTextView.setText(semCourse.getSemHeading());
        holder.courseNameTextView.setText(semCourse.getCourseName());
        holder.courseCodeTextView.setText(semCourse.getCourseCode());
        holder.courseScoreTextView.setText(semCourse.getCourseScore());
        holder.creditHoursTextView.setText(semCourse.getCreditHours());
        holder.gradePointTextView.setText(semCourse.getGradePoint());

    }

    @Override
    public int getItemCount() {
        return semCoursesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TextView semHeadingTextView;
        TextView courseNameTextView;
        TextView courseCodeTextView;
        TextView courseScoreTextView;
        TextView creditHoursTextView;
        TextView gradePointTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //semHeadingTextView = itemView.findViewById(R.id.numberOfSemester1);
            courseNameTextView = itemView.findViewById(R.id.nameOfCourse);
            gradePointTextView = itemView.findViewById(R.id.gradePointOfCourse);
            courseCodeTextView = itemView.findViewById(R.id.codeOfCourse);
            courseScoreTextView = itemView.findViewById(R.id.scoreOfCourse);
            creditHoursTextView = itemView.findViewById(R.id.creditHourCourse);
        }
    }
}


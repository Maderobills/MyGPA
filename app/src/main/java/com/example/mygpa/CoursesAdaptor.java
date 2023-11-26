package com.example.mygpa;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class CoursesAdaptor extends RecyclerView.Adapter<CoursesAdaptor.CoursesViewHolder> {
    private List<CoursesFormData> coursesDataList;
    private Context contextCourses;

    public CoursesAdaptor(List<CoursesFormData> coursesDataList, Context contextCourses) {
        this.coursesDataList = coursesDataList;
        this.contextCourses = contextCourses;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schools_recycler_data, parent, false);
        return new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        CoursesFormData coursesData = coursesDataList.get(position);
        holder.semNumber.setText(coursesData.getSemester());
        holder.courseName.setText(coursesData.getProgramName());


    }

    @Override
    public int getItemCount() {
        return coursesDataList.size();
    }

    public void updateData(List<CoursesFormData> updatedData) {
        coursesDataList = updatedData;
        notifyDataSetChanged();
    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView semNumber;
        TextView courseName;
        TextView courseScore;


        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.recycler_id_courses);
            semNumber = itemView.findViewById(R.id.numberOfSemester);
            courseName = itemView.findViewById(R.id.nameOfCourse);
            courseScore = itemView.findViewById(R.id.scoreOfCourse);
        }
    }
}

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
        holder.schoolName.setText(coursesData.getSchoolName());
        holder.program.setText(coursesData.getProgramName());

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

        TextView schoolName;
        TextView program;
        TextView scale;
        TextView semesters;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.recycler_id_schools);
            schoolName = itemView.findViewById(R.id.School_name);
            program = itemView.findViewById(R.id.type_txt_income);
            scale = itemView.findViewById(R.id.scale_type);
            semesters = itemView.findViewById(R.id.sem_number);
        }
    }
}

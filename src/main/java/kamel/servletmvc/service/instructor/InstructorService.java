package kamel.servletmvc.service.instructor;


import kamel.servletmvc.model.course.Course;
import kamel.servletmvc.model.grade.Grade;
import kamel.servletmvc.model.grade.GradeDTO;

import java.util.List;

public interface InstructorService {
    List<Course> getCourses(int instructorId);
    List<Grade> getCourseGrades(int courseID);
    boolean editGrade(GradeDTO grade);
}

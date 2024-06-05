package kamel.servletmvc.service.student;

import kamel.servletmvc.model.grade.Grade;

import java.util.List;

public interface StudentService {
    List<Grade> getGrades(int studentId);
    double getAverage(List<Integer> grades);
}

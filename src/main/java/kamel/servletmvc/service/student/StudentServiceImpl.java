package kamel.servletmvc.service.student;

import kamel.servletmvc.data.course.CoursesDao;
import kamel.servletmvc.data.course.JdbcCoursesDao;
import kamel.servletmvc.data.grade.GradesDao;
import kamel.servletmvc.data.grade.JdbcGradesDao;
import kamel.servletmvc.data.user.JdbcUsersDao;
import kamel.servletmvc.data.user.UsersDao;
import kamel.servletmvc.model.grade.Grade;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;

public class StudentServiceImpl implements StudentService {

    private final GradesDao gradesDao = new JdbcGradesDao();
    private final CoursesDao coursesDao = new JdbcCoursesDao();
    private final UsersDao usersDao = new JdbcUsersDao();

    @Override
    public List<Grade> getGrades(int studentId) {
        List<Grade> grades = new LinkedList<>();
        try {
            grades.addAll(
                    gradesDao.getGradesForStudent(studentId)
                    .stream()
                    .map(g -> {
                        try {
                            String courseName = coursesDao.findById(g.getCourseId()).getName();
                            String studentName = usersDao.findById(g.getStudentId()).getName();
                            return new Grade(courseName, studentName, g);
                        } catch (Exception e) {
                            return new Grade("N/A", "N/A", g);
                        }
                    }).toList());
        } catch (SQLException e) {
            grades.clear();
        }
        return grades;
    }

    @Override
    public double getAverage(List<Integer> grades) {
        OptionalDouble avg = grades.stream()
                .mapToInt(Integer::intValue)
                .average();
        if (avg.isEmpty()) return 0;
        return avg.getAsDouble();
    }
}

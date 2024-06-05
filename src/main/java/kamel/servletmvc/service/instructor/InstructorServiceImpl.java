package kamel.servletmvc.service.instructor;

import kamel.servletmvc.data.course.CoursesDao;
import kamel.servletmvc.data.course.JdbcCoursesDao;
import kamel.servletmvc.data.grade.GradesDao;
import kamel.servletmvc.data.grade.JdbcGradesDao;
import kamel.servletmvc.data.user.JdbcUsersDao;
import kamel.servletmvc.data.user.UsersDao;
import kamel.servletmvc.model.course.Course;
import kamel.servletmvc.model.grade.Grade;
import kamel.servletmvc.model.grade.GradeDTO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class InstructorServiceImpl implements InstructorService {

    private final CoursesDao coursesDao = new JdbcCoursesDao();
    private final GradesDao gradesDao = new JdbcGradesDao();
    private final UsersDao usersDao = new JdbcUsersDao();


    @Override
    public List<Course> getCourses(int instructorId) {
        List<Course> courses = new LinkedList<>();
        try {
            courses.addAll(coursesDao.findByInstructorId(instructorId));
        } catch (SQLException e) {
            courses.clear();
        }
        return courses;
    }

    @Override
    public List<Grade> getCourseGrades(int courseID) {
        List<Grade> grades = new LinkedList<>();
        try {
            grades.addAll(
                    gradesDao.getGradesForCourse(courseID).stream()
                            .map(g -> {
                                try {
                                    String courseName = coursesDao.findById(courseID).getName();
                                    String studentName = usersDao.findById(g.getStudentId()).getName();
                                    return new Grade(courseName, studentName, g);
                                } catch (Exception e) {
                                    return new Grade("N/A", "N/A", g);
                                }
                            })
                            .toList()
            );
        } catch (SQLException e) {
            grades.clear();
        }
        return grades;
    }

    @Override
    public boolean editGrade(GradeDTO grade) {
        try {
            return gradesDao.editGrade(grade);
        } catch (SQLException e) {
            return false;
        }
    }
}

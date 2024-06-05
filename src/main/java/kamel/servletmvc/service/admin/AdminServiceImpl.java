package kamel.servletmvc.service.admin;

import kamel.servletmvc.data.course.CoursesDao;
import kamel.servletmvc.data.course.JdbcCoursesDao;
import kamel.servletmvc.data.grade.GradesDao;
import kamel.servletmvc.data.grade.JdbcGradesDao;
import kamel.servletmvc.data.user.JdbcUsersDao;
import kamel.servletmvc.data.user.UsersDao;
import kamel.servletmvc.model.course.Course;
import kamel.servletmvc.model.grade.GradeDTO;
import kamel.servletmvc.model.user.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AdminServiceImpl implements AdminService {

    private final UsersDao usersDao = new JdbcUsersDao();
    private final CoursesDao coursesDao = new JdbcCoursesDao();
    private final GradesDao gradesDao = new JdbcGradesDao();

    @Override
    public List<User> getUsers() {
        try {
            return usersDao.findAll();
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    @Override
    public List<Course> getCourses() {
        try {
            return coursesDao.findAll();
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    @Override
    public boolean createCourse(Course course) {
        try {
            return coursesDao.insertCourse(course);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean createUser(User user) {
        try {
            return usersDao.insertUser(user);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean addStudentToCourse(int studentId, int courseId) {
        try {
            return gradesDao.insertGrade(new GradeDTO(courseId, studentId, 0));
        } catch (SQLException e) {
            return false;
        }
    }
}

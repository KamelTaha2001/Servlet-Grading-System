package kamel.servletmvc.data.course;



import kamel.servletmvc.model.course.Course;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class JdbcCoursesDao implements CoursesDao {

    private Connection connection = null;

    public JdbcCoursesDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/grading_system";
            String username = "root";
            String password = "Password123";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Wrong driver class!");
        } catch (SQLException e) {
            System.out.println("Error: Check your url, username and password!");
        }
    }

    @Override
    public Course findById(int id) throws NoSuchElementException, SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM COURSES WHERE id=?"
        );
        statement.setInt(1, id);
        return getCourse(statement);
    }

    @Override
    public List<Course> findByInstructorId(int id) throws NoSuchElementException, SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM COURSES WHERE instructorId=?"
        );
        statement.setInt(1, id);
        return getCourses(statement);
    }

    @Override
    public List<Course> findAll() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSES");
        return getCourses(statement);
    }

    private Course getCourse(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        Course course = null;
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int instructorId = resultSet.getInt("instructorId");
            course = new Course(id, name, instructorId);
        }
        if (course==null) throw new NoSuchElementException();
        return course;
    }

    private List<Course> getCourses(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        List<Course> courses = new LinkedList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int instructorId = resultSet.getInt("instructorId");
            courses.add(new Course(id, name, instructorId));
        }
        return courses;
    }

    @Override
    public boolean insertCourse(Course course) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO COURSES (name, instructorId) VALUES (?,?)"
        );
        statement.setString(1, course.getName());
        statement.setInt(2, course.getInstructorId());
        return statement.executeUpdate() > 0;
    }
}

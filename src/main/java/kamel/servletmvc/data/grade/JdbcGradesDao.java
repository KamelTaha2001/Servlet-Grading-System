package kamel.servletmvc.data.grade;



import kamel.servletmvc.model.grade.GradeDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class JdbcGradesDao implements GradesDao {

    private Connection connection = null;

    public JdbcGradesDao() {
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
    public List<GradeDTO> getGradesForCourse(int courseId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM GRADES WHERE courseId=?"
        );
        statement.setInt(1, courseId);
        return getGrades(statement);
    }

    @Override
    public List<GradeDTO> getGradesForStudent(int studentId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM GRADES WHERE studentId=?"
        );
        statement.setInt(1, studentId);
        return getGrades(statement);
    }

    private List<GradeDTO> getGrades(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        List<GradeDTO> grades = new LinkedList<>();
        while (resultSet.next()) {
            int courseId = resultSet.getInt("courseId");
            int studentId = resultSet.getInt("studentId");
            int grade = resultSet.getInt("grade");
            grades.add(new GradeDTO(courseId, studentId, grade));
        }
        return grades;
    }

    @Override
    public boolean insertGrade(GradeDTO grade) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO GRADES (courseId, studentId, grade) VALUES (?,?,?)"
        );
        statement.setInt(1, grade.getCourseId());
        statement.setInt(2, grade.getStudentId());
        statement.setInt(3, grade.getGrade());
        return statement.executeUpdate() > 0;
    }

    @Override
    public boolean editGrade(GradeDTO grade) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE GRADES SET grade=? WHERE courseId=? and studentId=?"
        );
        statement.setInt(1, grade.getGrade());
        statement.setInt(2, grade.getCourseId());
        statement.setInt(3, grade.getStudentId());
        return statement.executeUpdate() > 0;
    }
}

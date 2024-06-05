package kamel.servletmvc.data.user;



import kamel.servletmvc.model.user.User;
import kamel.servletmvc.model.user.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class JdbcUsersDao implements UsersDao{
    private Connection connection = null;

    public JdbcUsersDao() {
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
    public User findById(int userId) throws NoSuchElementException, SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE id=?");
        statement.setInt(1, userId);
        return getUser(statement);
    }

    @Override
    public User findByEmail(String userEmail) throws NoSuchElementException, SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE email=?");
        statement.setString(1, userEmail);
        return getUser(statement);
    }

    @Override
    public List<User> findAll() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS ORDER BY role");
        return getUsers(statement);
    }

    private User getUser(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        User user = null;
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            user = new User(id, email, name, password, UserRole.valueOf(role));
        }
        if (user==null) throw new SQLException();
        return user;
    }

    private List<User> getUsers(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        List<User> users = new LinkedList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            users.add(new User(id, email, name, password, UserRole.valueOf(role)));
        }
        return users;
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO USERS (email, name, password, role) VALUES (?,?,?,?)"
        );
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getName());
        statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        statement.setString(4, user.getRole().toString());
        return statement.executeUpdate() > 0;
    }
}

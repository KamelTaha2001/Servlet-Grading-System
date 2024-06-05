package kamel.servletmvc.data.user;



import kamel.servletmvc.model.user.User;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public interface UsersDao {
    User findById(int id) throws NoSuchElementException, SQLException;
    User findByEmail(String email) throws NoSuchElementException, SQLException;
    List<User> findAll() throws SQLException;
    boolean insertUser(User user) throws SQLException;
}

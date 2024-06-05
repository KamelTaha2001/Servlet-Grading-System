package kamel.servletmvc.service;


import kamel.servletmvc.data.user.JdbcUsersDao;
import kamel.servletmvc.data.user.UsersDao;
import kamel.servletmvc.model.user.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NameNotFoundException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

public class LoginService {

    private final UsersDao usersDao = new JdbcUsersDao();

    public User getUser(int userId) throws NameNotFoundException {
        try {
            return usersDao.findById(userId);
        } catch (Exception e) {
            throw new NameNotFoundException("User not found!");
        }
    }

    public User authenticate(String email, String password) throws AccessDeniedException {
        try {
            User user = usersDao.findByEmail(email);
            boolean success = BCrypt.checkpw(password, user.getPassword());
            if (success) return user;
            else throw new AccessDeniedException("Wrong Credentials");
        } catch (SQLException e) {
            throw new AccessDeniedException("Wrong Credentials");
        }
    }
}

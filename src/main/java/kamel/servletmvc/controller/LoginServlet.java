package kamel.servletmvc.controller;

import kamel.servletmvc.model.user.User;
import kamel.servletmvc.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private final LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getSession().removeAttribute("id");
        httpServletRequest.getSession().removeAttribute("role");
        httpServletRequest.getRequestDispatcher("views/login.jsp")
                .forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        try {
            User user = loginService.authenticate(email, password);
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("id", user.getId());
            session.setAttribute("role", user.getRole().name());
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + user.getRole().name().toLowerCase() + "_dashboard/" + user.getId()
            );
        } catch (Exception e) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
        }

    }
}

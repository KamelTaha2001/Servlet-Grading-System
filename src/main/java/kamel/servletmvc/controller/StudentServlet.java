package kamel.servletmvc.controller;

import kamel.servletmvc.model.grade.Grade;
import kamel.servletmvc.model.user.User;
import kamel.servletmvc.model.user.UserRole;
import kamel.servletmvc.service.student.StudentService;
import kamel.servletmvc.service.student.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@WebServlet(urlPatterns = "/student_dashboard/*")
public class StudentServlet extends HttpServlet {

    private final StudentService service = new StudentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!authenticate(httpServletRequest, UserRole.STUDENT)) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        HttpSession session = httpServletRequest.getSession();
        int userId = (int) session.getAttribute("id");
        List<Grade> grades = service.getGrades(userId);
        session.setAttribute("grades", grades);
        String average = String.valueOf(service.getAverage(
                grades.stream().map(g -> g.getDto().getGrade()).toList()
        ));
        httpServletRequest.setAttribute("average", average);
        httpServletRequest.getRequestDispatcher("/views/student_dashboard.jsp")
                .forward(httpServletRequest, httpServletResponse);
    }

    public static boolean authenticate(HttpServletRequest request, UserRole supposedRole) {
        try {
            HttpSession session = request.getSession();
            int pathId = Integer.parseInt(request.getPathInfo().substring(1));
            int loggedInId = (int) session.getAttribute("id");
            String role = (String) session.getAttribute("role");
            return role.equalsIgnoreCase(supposedRole.name()) && loggedInId == pathId;
        } catch (Exception e) {
            return false;
        }
    }
}

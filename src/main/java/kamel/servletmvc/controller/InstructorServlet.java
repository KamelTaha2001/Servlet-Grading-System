package kamel.servletmvc.controller;

import kamel.servletmvc.model.user.UserRole;
import kamel.servletmvc.service.instructor.InstructorService;
import kamel.servletmvc.service.instructor.InstructorServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@WebServlet(urlPatterns = "/instructor_dashboard/*")
public class InstructorServlet extends HttpServlet {

    private final InstructorService service = new InstructorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!authenticate(httpServletRequest, UserRole.INSTRUCTOR)) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        HttpSession session = httpServletRequest.getSession();
        int id = (int) session.getAttribute("id");
        session.setAttribute("courses", service.getCourses(id));
        httpServletRequest.getRequestDispatcher("/views/instructor_dashboard.jsp")
                .forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!authenticate(httpServletRequest, UserRole.INSTRUCTOR)) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        int courseId = Integer.parseInt(httpServletRequest.getParameter("courseId"));
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/courses/" + courseId);
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

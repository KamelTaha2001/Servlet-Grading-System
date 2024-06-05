package kamel.servletmvc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import kamel.servletmvc.model.course.Course;
import kamel.servletmvc.model.user.User;
import kamel.servletmvc.model.user.UserRole;
import kamel.servletmvc.service.admin.AdminService;
import kamel.servletmvc.service.admin.AdminServiceImpl;
import kamel.servletmvc.util.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(urlPatterns = "/admin_dashboard/*")
public class AdminServlet extends HttpServlet {

    private final AdminService service = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!authenticate(httpServletRequest, UserRole.ADMIN)) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        Iterable<User> users = service.getUsers();
        Iterable<Course> courses = service.getCourses();
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("users", users);
        session.setAttribute("courses", courses);
        httpServletRequest.getRequestDispatcher("/views/admin_dashboard.jsp")
                .forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!authenticate(httpServletRequest, UserRole.ADMIN)) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        JsonNode node = JsonReader.getNode(httpServletRequest.getReader());
        String requestType = node.get("requestType").asText();
        System.out.println(requestType);
        switch (requestType) {
            case "create_user" -> {
                String name = node.get("name").asText();
                String email = node.get("email").asText();
                String password = node.get("password").asText();
                String type = node.get("type").asText();
                User user = new User(-1, email, name, password, UserRole.valueOf(type));
                boolean success = service.createUser(user);
                if (success)
                    httpServletResponse.getWriter().println("User created!");
                else
                    httpServletResponse.getWriter().println("Failed!");
            }
            case "create_course" -> {
                String name = node.get("name").asText();
                int instructorId = node.get("instructorId").asInt();
                boolean success = service.createCourse(new Course(-1, name, instructorId));
                if (success)
                    httpServletResponse.getWriter().println("Course created!");
                else
                    httpServletResponse.getWriter().println("Failed!");
            }
            case "add_student" -> {
                int studentId = node.get("studentId").asInt();
                int courseId = node.get("courseId").asInt();
                boolean success = service.addStudentToCourse(studentId, courseId);
                if (success)
                    httpServletResponse.getWriter().println("Student added!");
                else
                    httpServletResponse.getWriter().println("Failed!");
            }
        }
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

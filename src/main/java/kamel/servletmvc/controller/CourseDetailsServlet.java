package kamel.servletmvc.controller;

import com.google.gson.Gson;
import kamel.servletmvc.model.course.Course;
import kamel.servletmvc.model.grade.GradeDTO;
import kamel.servletmvc.model.user.User;
import kamel.servletmvc.model.user.UserRole;
import kamel.servletmvc.service.instructor.InstructorService;
import kamel.servletmvc.service.instructor.InstructorServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/instructor_dashboard/courses/*")
public class CourseDetailsServlet extends HttpServlet {

    private final InstructorService service = new InstructorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(httpServletRequest.getParameter("courseId"));
            HttpSession session = httpServletRequest.getSession();
            int userId = (int) session.getAttribute("id");
            if (!authenticate(httpServletRequest, UserRole.INSTRUCTOR, service.getCourses(userId))) {
                httpServletRequest.setAttribute("errorMessage", "Access denied");
                httpServletRequest.getRequestDispatcher("/views/error.jsp")
                        .forward(httpServletRequest, httpServletResponse);
                return;
            }
            httpServletRequest.getSession().setAttribute("grades", service.getCourseGrades(courseId));
            httpServletRequest.getRequestDispatcher("/views/course_details.jsp")
                    .forward(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        HttpSession session = httpServletRequest.getSession();
        int userId = (int) session.getAttribute("id");
        if (!authenticate(httpServletRequest, UserRole.INSTRUCTOR, service.getCourses(userId))) {
            httpServletRequest.setAttribute("errorMessage", "Access denied");
            httpServletRequest.getRequestDispatcher("/views/error.jsp")
                    .forward(httpServletRequest, httpServletResponse);
            return;
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = httpServletRequest.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        GradeDTO grade = new Gson().fromJson(sb.toString(), GradeDTO.class);
        boolean success = service.editGrade(grade);
        if (success)
            httpServletResponse.getWriter().println("Grade edited!");
        else
            httpServletResponse.getWriter().println("Failed!");
    }

    public static boolean authenticate(HttpServletRequest request, UserRole supposedRole, List<Course> instructorCourses) {
        try {
            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("role");
            if (!role.equalsIgnoreCase(supposedRole.name()))
                return false;
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            return instructorCourses.stream().map(Course::getId).anyMatch(id -> id == courseId);
        } catch (Exception e) {
            return false;
        }
    }
}

<html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Instructor Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
            cursor: pointer;
        }
        li:hover {
            background-color: #f9f9f9;
        }
        a {
            text-decoration: none;
            color: #333;
        }
        a:hover {
            text-decoration: underline;
        }
        .course-button {
            display: block;
            width: 100%;
            border: none;
            background-color: transparent; /* Remove default button background */
            padding: 0;
            text-align: left; /* Align text to the left within the button */
        }
        .logout-button {
            position: absolute;
            top: 20px;
            right: 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        .logout-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <form action="http://localhost:8085/ServletMVC/login">
        <button type="submit" class="logout-button">Logout</button>
    </form>

    <h1>Instructor Dashboard</h1>

    <%@ page import="kamel.servletmvc.model.course.Course" %>

    <form id="courseForm" action="/courses/" method="get">
        <input type="hidden" name="courseId" id="courseId">
        <%
            Iterable<Course> courses = (Iterable<Course>) session.getAttribute("courses");
            out.print("<ul>");
            for (Course c : courses) {
                out.print("<li>" +
                "<button class='course-button'" +
                " type='submit'" +
                " name='courseId'" +
                                " onclick='setAction(" + c.getId() + ")'>" +
                c.getName() +
                "</button></li>"
                );
            }
            out.print("</ul>");
        %>
    </form>

    <script>
        function setAction(courseId) {
            document.getElementById('courseId').value = courseId;
            document.getElementById('courseForm').action = 'courses/' + courseId;
        }
    </script>
</div>
</body>
</html>

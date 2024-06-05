<html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
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
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center; /* Center the content horizontally */
        }

        h1 {
            text-align: center;
        }

        .options {
            list-style-type: none;
            padding: 0;
            margin: 0;
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
        }

        .options li {
            margin: 10px;
        }

        .button {
            display: inline-block;
            width: 180px; /* Adjust the width of the buttons as needed */
            padding: 10px;
            text-align: center;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .button:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            margin-bottom: 50px;
        }

        th, td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        caption {
            text-align: left;
            margin-bottom: 10px;
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

<dialog id="createUserDialog">
    <h2>Create User</h2>
    <input required type="text" id="userNameInput" placeholder="Enter name" name="userName"/>
    <input required type="text" id="userEmailInput" placeholder="Enter email" name="userEmail"/>
    <input required type="text" id="userPasswordInput" placeholder="Enter password" name="userPassword"/>
    <br>
    <input style="margin-top: 10px" checked type="radio" id="student" name="userType" value="STUDENT"/>
    <label for="Student">Student</label>
    <input type="radio" id="instructor" name="userType" value="INSTRUCTOR"/>
    <label for="Instructor">Instructor</label>
    <input type="radio" id="admin" name="userType" value="ADMIN"/>
    <label for="Admin">Admin</label><br>
    <br>
    <%
        int userId = -1;
        try {
            userId = (int) session.getAttribute("id");
        } catch (Exception e) {
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
            return;
        }
       out.println(
               "<button style=\"margin-top: 10px\" type=\"submit\" onclick=\"createUser(" + userId + ")\">Save</button>"
       );
    %>
    <button onclick="cancel('createUserDialog')">Cancel</button>
</dialog>

<dialog id="createCourseDialog">
    <h2>Create Course</h2>
    <input required type="text" id="courseNameInput" placeholder="Enter name" name="courseName"/>
    <input required type="text" id="instructorIdInput" placeholder="Enter instructor id" name="instructorId"/>
    <%
        try {
            userId = (int) session.getAttribute("id");
        } catch (Exception e) {
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
            return;
        }
        out.println(
                "<button style=\"margin-top: 10px\" type=\"submit\" onclick=\"createCourse(" + userId + ")\">Save</button>"
        );
    %>
    <button onclick="cancel('createCourseDialog')">Cancel</button>
</dialog>

<dialog id="addStudentDialog">
    <h2>Add Student</h2>
    <input required type="text" id="studentIdInput" placeholder="Enter student id" name="studentId"/>
    <input required type="text" id="courseIdInput" placeholder="Enter course id" name="courseId"/>
    <%
        try {
            userId = (int) session.getAttribute("id");
        } catch (Exception e) {
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
            return;
        }
        out.println(
                "<button style=\"margin-top: 10px\" type=\"submit\" onclick=\"addStudent(" + userId + ")\">Save</button>"
        );
    %>
    <button onclick="cancel('addStudentDialog')">Cancel</button>
</dialog>
<form action="http://localhost:8085/ServletMVC/login">
    <button type="submit" class="logout-button">Logout</button>
</form>

<script>
    function openDialog(dialogId) {
        var dialog = document.getElementById(dialogId);
        dialog.showModal();
    }

    function createUser(userId) {
        var userNameInput = document.getElementById('userNameInput');
        var userEmailInput = document.getElementById('userEmailInput');
        var userPasswordInput = document.getElementById('userPasswordInput');
        var userTypeInput = document.querySelector('input[name="userType"]:checked');
        var userName = userNameInput.value;
        var userEmail = userEmailInput.value;
        var userPassword = userPasswordInput.value;
        var userType = userTypeInput.value;
        var data = {
            name: userName,
            email: userEmail,
            password: userPassword,
            type: userType.toUpperCase(),
            requestType: "create_user"
        };
        save('createUserDialog', data, userId);
    }

    function createCourse(userId) {
        var courseNameInput = document.getElementById('courseNameInput');
        var iIdInput = document.getElementById('instructorIdInput');
        var courseName = courseNameInput.value;
        var iId = iIdInput.value;
        var data = {
            name: courseName,
            instructorId: iId,
            requestType: "create_course"
        };
        save('createCourseDialog', data, userId);
    }

    function addStudent(userId) {
        var sIdInput = document.getElementById('studentIdInput');
        var cIdInput = document.getElementById('courseIdInput');
        var sId = sIdInput.value;
        var cId = cIdInput.value;
        var data = {
            studentId: sId,
            courseId: cId,
            requestType: "add_student"
        };
        save('addStudentDialog', data, userId);
    }

    function save(dialogId, data, userId) {
        var options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };

        var success = false;
        fetch("/ServletMVC/admin_dashboard/" + userId, options).then(response => {
            if (response.ok) {
                success = true;
            }
            return response.text();
        })
            .then(responseText => {
                alert(responseText);
                if (success) {
                    window.location.reload(true);
                }
            }).catch(error => {
            // Handle network errors
            console.error('Error:', error);
        });
    }

    function cancel(dialogId) {
        closeDialog(dialogId);
    }

    function closeDialog(dialogId) {
        var dialog = document.getElementById(dialogId);
        dialog.close();
    }
</script>

<div class="container">
    <h1>Admin Dashboard</h1>
    <ul class="options">
        <li>
            <button class="button" onclick=<% out.print("\"openDialog('createUserDialog')\""); %>>Create User</button>
        </li>
        <li>
            <button class="button" onclick=<% out.print("\"openDialog('createCourseDialog')\""); %>>Create Course
            </button>
        </li>
        <li>
            <button class="button" onclick=<% out.print("\"openDialog('addStudentDialog')\""); %>>Add Student to
                Course
            </button>
        </li>
    </ul>


    <%@ page import="kamel.servletmvc.model.user.User" %>
    <%@ page import="java.io.IOException" %>
    <%!
        void printUserTable(Iterable<User> users, JspWriter out) {
            try {
                for (User user : users) {
                    out.print(
                            "<tr>" +
                                    "<td>" + user.getId() + "</td>" +
                                    "<td>" + user.getName() + "</td>" +
                                    "<td>" + user.getEmail() + "</td>" +
                                    "<td>" + user.getRole().name() + "</td>" +
                                    "</tr>"
                    );
                }
            } catch (IOException e) {
            }
        }
    %>

    <table>
        <caption>Students</caption>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
        </tr>
        </thead>
        <tbody>
        <%
            printUserTable((Iterable<User>) session.getAttribute("users"), out);
        %>
        </tbody>
    </table>

    <table>
        <caption>Courses</caption>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Instructor ID</th>
        </tr>
        </thead>
        <tbody>
        <%@ page import="kamel.servletmvc.model.course.Course" %>
        <%
            Iterable<Course> courses = (Iterable<Course>) session.getAttribute("courses");
            for (Course course : courses) {
                out.print(
                        "<tr>" +
                                "<td>" + course.getId() + "</td>" +
                                "<td>" + course.getName() + "</td>" +
                                "<td>" + course.getInstructorId() + "</td>" +
                                "</tr>"
                );
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>

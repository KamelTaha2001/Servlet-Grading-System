<html>
<head xmlns:th="http://www.w3.org/1999/xhtml">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Grades</title>
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

        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
        }

        .average {
            font-size: 19px;
            font-weight: bold;
            color: #333;
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

<form action="http://localhost:8085/ServletMVC/login">
    <button type="submit" class="logout-button">Logout</button>
</form>

<div class="container">
    <table>
        <tr>
            <th>Course Name</th>
            <th>Grade</th>
        </tr>
        <%@ page import="kamel.servletmvc.model.grade.Grade" %>
        <%
            Iterable<Grade> grades = (Iterable<Grade>) session.getAttribute("grades");
            for (Grade grade : grades) {
                out.print("<tr><td>" + grade.getCourseName() + "</td><td>" + grade.getDto().getGrade() + "</td></tr>");
            }
        %>
    </table>
    <h1 class="average">Average: <span>${requestScope.average}</span></h1>
</div>
</body>
</html>
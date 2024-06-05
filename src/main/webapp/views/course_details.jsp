<html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Details</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        dialog {
            width: 300px;
            padding: 20px;
            background-color: #f0f0f0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <dialog id="myDialog">
        <h2>Enter Grade</h2>
        <input type="number" id="gradeInput" placeholder="Enter grade" />
        <button onclick="saveGrade()">Save</button>
        <button onclick="cancelGrade()">Cancel</button>
    </dialog>
    <script th:inline="javascript">
        var gradeInput = document.getElementById('gradeInput');
        var dialog = document.getElementById('myDialog');
        var sId = 0;
        var cId = 0;

        function openDialog(sId, cId, initialGrade) {
            this.sId = sId;
            this.cId = cId;
            gradeInput.value = initialGrade;
            dialog.showModal();
        }

        function saveGrade() {
            var grade = gradeInput.value; // Get the value from the input field
            // Perform saving action (e.g., send data to server)
            editGrade(grade);
            closeDialog();
        }

        function editGrade(value) {
            // Define the URL to submit the form data to
            var url = '';

            // Define the data to be sent in the POST request (if needed)
            var data = {
                // Add any form data here
                studentId: sId,
                courseId: cId,
                grade: value
            };

            // Define the fetch options
            var options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // Set the content type as JSON if sending JSON data
                },
                body: JSON.stringify(data) // Convert the data to JSON format
            };

            // Send the POST request using fetch API
            fetch(url, options)
            .then(response => {
                // Check if the response is successful (status code in the range 200-299)
                if (response.ok) {
                    // Parse the response body as text
                    return response.text();
                } else {
                    // Handle error responses
                    console.error('Error:', response.statusText);
                }
            })
            .then(message => {
                // Print the message to the console
                console.log(message);
                // window.location.reload(true);
            })
            .catch(error => {
                // Handle network errors
                console.error('Error:', error);
            });
        }

        function cancelGrade() {
            closeDialog();
        }

        function closeDialog() {
            gradeInput.value ="";
            dialog.close();
        }
    </script>

<h1>Course Details</h1>
<table>
    <thead>
    <tr>
        <th>Student ID</th>
        <th>Student Name</th>
        <th>Grade</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%@ page import="kamel.servletmvc.model.grade.Grade" %>
    <%@ page import="java.lang.Iterable" %>
    <%
        Iterable<Grade> grades = (Iterable<Grade>) session.getAttribute("grades");
        for (Grade grade : grades) {
            int studentId = grade.getDto().getStudentId();
            int gradeValue = grade.getDto().getGrade();
            String studentName = grade.getStudentName();
            out.print(
                "<tr>" +
                "<td>" + studentId + "</td>" +
                "<td>" + studentName + "</td>" +
                "<td>" + gradeValue + "</td>" +
                "<td><button onclick='openDialog(" +
                studentId + ", " +
                request.getParameter("id") + ", " +
                gradeValue + ");'" +
                " class='edit-button'>Edit</button></td>" +
                "</tr>"
            );
        }
    %>
    </tbody>
</table>
</body>
</html>

package kamel.servletmvc.model;

public class EditGradeRequest {
    private int studentId;
    private int courseId;
    private int grade;

    public EditGradeRequest(int studentId, int courseId, int grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getGrade() {
        return grade;
    }
}

public class Course {
    private String courseCode;
    private String courseName;
    private String instructor;
    private int maxCapacity;
    private int currentEnrollment;

    // Constructor
    public Course(String courseCode, String courseName, String instructor, int maxCapacity, int currentEnrollment) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }

    // Getters and setters
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    public void setCurrentEnrollment(int currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public int getAvailableSlots() {
        return maxCapacity - currentEnrollment;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " (" + currentEnrollment + "/" + maxCapacity + ")";
    }
}

// Student class to store student information
class Student {
    private String studentId;
    private String name;
    private String email;
    private String program;
    private int year;

    // Constructor
    public Student(String studentId, String name, String email, String program, int year) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.program = program;
        this.year = year;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProgram() {
        return program;
    }

    public int getYear() {
        return year;
    }
}
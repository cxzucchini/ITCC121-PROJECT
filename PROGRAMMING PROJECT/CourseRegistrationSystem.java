import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CourseRegistrationSystem {
    // List to store available courses
    private static ArrayList<Course> availableCourses = new ArrayList<>();

    // Main method to start the application
    public static void main(String[] args) {
        // Initialize courses
        initializeCourses();

        // Start with the questionnaire form
        SwingUtilities.invokeLater(() -> {
            StudentQuestionnaire questionnaire = new StudentQuestionnaire();
            questionnaire.setVisible(true);
        });
    }

    // Method to initialize sample courses
    private static void initializeCourses() {
        availableCourses.add(new Course("CS101", "Introduction to Programming", "Prof. Smith", 30, 0));
        availableCourses.add(new Course("CS102", "Data Structures", "Prof. Johnson", 25, 0));
        availableCourses.add(new Course("MATH201", "Calculus I", "Prof. Williams", 35, 0));
        availableCourses.add(new Course("ENG101", "English Composition", "Prof. Davis", 40, 0));
        availableCourses.add(new Course("PHYS101", "Physics I", "Prof. Brown", 20, 0));
    }

    // Method to get available courses
    public static ArrayList<Course> getAvailableCourses() {
        return availableCourses;
    }

    // Method to update course enrollment
    public static boolean enrollInCourse(String courseCode, Student student) {
        for (Course course : availableCourses) {
            if (course.getCourseCode().equals(courseCode)) {
                if (course.getCurrentEnrollment() < course.getMaxCapacity()) {
                    course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
                    return true;
                } else {
                    return false; // Course is full
                }
            }
        }
        return false; // Course not found
    }
}
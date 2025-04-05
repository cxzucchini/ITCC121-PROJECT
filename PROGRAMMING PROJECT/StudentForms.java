import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// First form: Student Questionnaire
class StudentQuestionnaire extends JFrame {
    private JTextField studentIdField, nameField, emailField;
    private JComboBox<String> programComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton nextButton;

    public StudentQuestionnaire() {
        setTitle("Student Information");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        formPanel.add(studentIdField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Program:"));
        String[] programs = {"Computer Science", "Engineering", "Business", "Arts", "Sciences"};
        programComboBox = new JComboBox<>(programs);
        formPanel.add(programComboBox);

        formPanel.add(new JLabel("Year:"));
        Integer[] years = {1, 2, 3, 4};
        yearComboBox = new JComboBox<>(years);
        formPanel.add(yearComboBox);

        nextButton = new JButton("Next");

        // Add action listener to the button
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    Student student = new Student(
                            studentIdField.getText(),
                            nameField.getText(),
                            emailField.getText(),
                            (String) programComboBox.getSelectedItem(),
                            (Integer) yearComboBox.getSelectedItem()
                    );

                    // Open the course registration form
                    CourseRegistrationForm registrationForm = new CourseRegistrationForm(student);
                    registrationForm.setVisible(true);
                    dispose(); // Close the current form
                }
            }
        });

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Student Information Questionnaire", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Method to validate the form
    private boolean validateForm() {
        if (studentIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

// Second form: Course Registration
class CourseRegistrationForm extends JFrame {
    private Student student;
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JButton registerButton;
    private JButton viewRegisteredButton;
    private ArrayList<String> selectedCourses = new ArrayList<>();

    public CourseRegistrationForm(Student student) {
        this.student = student;

        setTitle("Course Registration");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + "! Select courses to register:");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create table for courses
        String[] columnNames = {"Select", "Course Code", "Course Name", "Instructor", "Available Slots"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        coursesTable = new JTable(tableModel);
        coursesTable.getColumnModel().getColumn(0).setMaxWidth(50);

        // Populate table with courses
        for (Course course : CourseRegistrationSystem.getAvailableCourses()) {
            tableModel.addRow(new Object[] {
                    false,
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getInstructor(),
                    course.getAvailableSlots() + "/" + course.getMaxCapacity()
            });
        }

        JScrollPane scrollPane = new JScrollPane(coursesTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerButton = new JButton("Register Selected Courses");
        viewRegisteredButton = new JButton("View Registered Courses");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerSelectedCourses();
            }
        });

        viewRegisteredButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCourses.isEmpty()) {
                    JOptionPane.showMessageDialog(CourseRegistrationForm.this,
                            "You haven't registered for any courses yet.",
                            "No Courses Registered", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Open the confirmation form
                    RegistrationConfirmation confirmationForm = new RegistrationConfirmation(student, selectedCourses);
                    confirmationForm.setVisible(true);
                    dispose(); // Close the current form
                }
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(viewRegisteredButton);

        // Add components to main panel
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Method to register selected courses
    private void registerSelectedCourses() {
        boolean anySelected = false;
        boolean anyFailed = false;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) tableModel.getValueAt(i, 0);

            if (selected) {
                anySelected = true;
                String courseCode = (String) tableModel.getValueAt(i, 1);

                if (CourseRegistrationSystem.enrollInCourse(courseCode, student)) {
                    // Add to selected courses if not already there
                    if (!selectedCourses.contains(courseCode)) {
                        selectedCourses.add(courseCode);
                    }

                    // Update the table
                    for (Course course : CourseRegistrationSystem.getAvailableCourses()) {
                        if (course.getCourseCode().equals(courseCode)) {
                            tableModel.setValueAt(course.getAvailableSlots() + "/" + course.getMaxCapacity(), i, 4);
                            break;
                        }
                    }
                } else {
                    anyFailed = true;
                }

                // Reset checkbox
                tableModel.setValueAt(false, i, 0);
            }
        }

        if (!anySelected) {
            JOptionPane.showMessageDialog(this, "Please select at least one course to register.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        } else if (anyFailed) {
            JOptionPane.showMessageDialog(this, "Some courses could not be registered. They may be full.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Courses registered successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

// Third form: Registration Confirmation
class RegistrationConfirmation extends JFrame {
    private Student student;
    private ArrayList<String> registeredCourses;

    public RegistrationConfirmation(Student student, ArrayList<String> registeredCourses) {
        this.student = student;
        this.registeredCourses = registeredCourses;

        setTitle("Registration Confirmation");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("Registration Confirmation", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Create confirmation panel
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setLayout(new BoxLayout(confirmationPanel, BoxLayout.Y_AXIS));

        // Student information
        JPanel studentInfoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        studentInfoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        studentInfoPanel.add(new JLabel("Student ID:"));
        studentInfoPanel.add(new JLabel(student.getStudentId()));

        studentInfoPanel.add(new JLabel("Name:"));
        studentInfoPanel.add(new JLabel(student.getName()));

        studentInfoPanel.add(new JLabel("Email:"));
        studentInfoPanel.add(new JLabel(student.getEmail()));

        studentInfoPanel.add(new JLabel("Program:"));
        studentInfoPanel.add(new JLabel(student.getProgram()));

        studentInfoPanel.add(new JLabel("Year:"));
        studentInfoPanel.add(new JLabel(String.valueOf(student.getYear())));

        // Course information
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesPanel.setBorder(BorderFactory.createTitledBorder("Registered Courses"));

        DefaultTableModel courseTableModel = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Instructor"}, 0);
        JTable coursesTable = new JTable(courseTableModel);

        // Add registered courses to the table
        for (String courseCode : registeredCourses) {
            for (Course course : CourseRegistrationSystem.getAvailableCourses()) {
                if (course.getCourseCode().equals(courseCode)) {
                    courseTableModel.addRow(new Object[]{
                            course.getCourseCode(),
                            course.getCourseName(),
                            course.getInstructor()
                    });
                    break;
                }
            }
        }

        JScrollPane courseScrollPane = new JScrollPane(coursesTable);
        coursesPanel.add(courseScrollPane, BorderLayout.CENTER);

        // Registration information
        JPanel registrationInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registrationInfoPanel.setBorder(BorderFactory.createTitledBorder("Registration Information"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        JLabel dateLabel = new JLabel("Registration Date: " + dateFormat.format(new Date()));
        registrationInfoPanel.add(dateLabel);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to continue registration
        JButton backButton = new JButton("Register More Courses");
        backButton.addActionListener(e -> {
            CourseRegistrationForm registrationForm = new CourseRegistrationForm(student);
            registrationForm.setVisible(true);
            dispose();
        });

        // Print button (simulated)
        JButton printButton = new JButton("Print Confirmation");
        printButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Printing confirmation... (This would send to printer in a real application)",
                    "Print", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        buttonPanel.add(printButton);
        buttonPanel.add(exitButton);

        // Add all panels to main panel
        confirmationPanel.add(studentInfoPanel);
        confirmationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        confirmationPanel.add(coursesPanel);
        confirmationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        confirmationPanel.add(registrationInfoPanel);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(confirmationPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
import javax.swing.*;
import java.awt.*;//abstract window toolkit
import java.io.*;
import java.util.ArrayList;

class Employee {

    int id;
    String name, department;
    double salary;

    // Default constructor
    Employee() {
        id = 0;
        name = "";
        salary = 0;
        department = "";
    }

    // Parameterized constructor
    Employee(int id, String name, double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    // Copy constructor
    Employee(Employee e) {
        id = e.id;
        name = e.name;
        salary = e.salary;
        department = e.department;
    }

    double allowance() {
        return salary * 0.10;   // 10% allowance
    }

    double deduction() {
        return salary * 0.05;   // 5% deduction
    }

    double netSalary() {
        return salary + allowance() - deduction();
    }

    public String toString() {
        return "ID: " + id
            + " | Name: " + name
            + " | Department: " + department
            + " | Basic Salary: Rs. " + salary
            + " | Net Salary: Rs. " + netSalary();
    }
}

public class EmployeePayrollSystem extends JFrame {

    JTextField idField, nameField, salaryField;
    JComboBox<String> deptBox;
    JTextArea output;

    ArrayList<Employee> employees = new ArrayList<>();

    String fileName = "employees.txt";

    EmployeePayrollSystem() {

        setTitle("Employee Payroll System");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(
            "EMPLOYEE PAYROLL SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        idField = new JTextField();
        nameField = new JTextField();
        salaryField = new JTextField();

        deptBox = new JComboBox<>(new String[] {
            "IT", "HR", "Sales", "Finance"
        });

        form.add(new JLabel("Employee ID:"));
        form.add(idField);

        form.add(new JLabel("Employee Name:"));
        form.add(nameField);

        form.add(new JLabel("Basic Salary:"));
        form.add(salaryField);

        form.add(new JLabel("Department:"));
        form.add(deptBox);

        JButton addButton = new JButton("Add Employee");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton viewButton = new JButton("View All");

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(saveButton);
        buttons.add(loadButton);
        buttons.add(viewButton);

        output = new JTextArea(12, 50);
        output.setEditable(false);

        setLayout(new BorderLayout(5, 5));
        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(new JScrollPane(output), BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addEmployee());
        saveButton.addActionListener(e -> saveEmployees());
        loadButton.addActionListener(e -> loadEmployees());
        viewButton.addActionListener(e -> viewEmployees());

        setVisible(true);
    }

    // Add Employee
    void addEmployee() {

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            double salary = Double.parseDouble(
                salaryField.getText().trim());
            String dept = (String) deptBox.getSelectedItem();

            if (id <= 0 || name.isEmpty() || salary <= 0
                    || name.contains("#")) {
                JOptionPane.showMessageDialog(
                    this, "Enter valid employee details!");
                return;
            }

            for (Employee e : employees) {
                if (e.id == id) {
                    JOptionPane.showMessageDialog(
                        this, "Employee ID already exists!");
                    return;
                }
            }

            Employee e = new Employee(id, name, salary, dept);

            // Use copy constructor
            Employee copy = new Employee(e);
            employees.add(copy);

            JOptionPane.showMessageDialog(
                this, "Employee added successfully!\n"
                + "Net Salary: Rs. " + copy.netSalary());

            idField.setText("");
            nameField.setText("");
            salaryField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "ID must be an integer and salary must be numeric!");
        }
    }

    // Save records in employees.txt
    void saveEmployees() {

        try {
            BufferedWriter bw = new BufferedWriter(
                new FileWriter(fileName));

            for (Employee e : employees) {
                bw.write(e.id + "#" + e.name + "#"
                    + e.salary + "#" + e.department);
                bw.newLine();
            }

            bw.close();

            JOptionPane.showMessageDialog(
                this, "Employee records saved successfully!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this, "Error saving file: " + ex.getMessage());
        }
    }

    // Load records from employees.txt
    void loadEmployees() {

        employees.clear();

        try {
            BufferedReader br = new BufferedReader(
                new FileReader(fileName));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("#", -1);

                if (data.length == 4) {

                    // Use default constructor
                    Employee e = new Employee();

                    e.id = Integer.parseInt(data[0]);
                    e.name = data[1];
                    e.salary = Double.parseDouble(data[2]);
                    e.department = data[3];

                    employees.add(e);
                }
            }

            br.close();

            JOptionPane.showMessageDialog(
                this, "Employee records loaded successfully!");

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
                this, "employees.txt not found! Save records first.");
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Error loading employee records!");
        }
    }

    // View all employees
    void viewEmployees() {

        output.setText("");

        if (employees.isEmpty()) {
            output.setText("No employee records available.");
            return;
        }

        for (Employee e : employees) {
            output.append(e.toString() + "\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            () -> new EmployeePayrollSystem());
    }
}

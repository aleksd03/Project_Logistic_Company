package org.informatics.service;

import org.informatics.dao.EmployeeDao;
import org.informatics.entity.Employee;
import org.informatics.entity.Company;
import org.informatics.entity.Office;
import org.informatics.entity.User;
import org.informatics.entity.enums.EmployeeType;

import java.util.List;

/**
 * Service layer for Employee-related operations.
 * Handles validation and delegates persistence to EmployeeDao.
 */
public class EmployeeService {

    // DAO responsible for Employee persistence
    private final EmployeeDao repo = new EmployeeDao();

    /**
     * Creates an Employee associated with a given User, Company, and Office.
     */
    public Employee createForUser(User user, Company company, Office office) {

        // User is mandatory for Employee creation
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Employee e = new Employee();
        e.setUser(user);
        e.setCompany(company);
        e.setOffice(office);

        // Persist employee
        Employee saved = repo.save(e);
        System.out.println("Employee created for user: " + user.getEmail());

        return saved;
    }

    /**
     * Retrieves an Employee associated with a specific User ID.
     */
    public Employee getEmployeeByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return repo.findByUserId(userId);
    }

    /**
     * Retrieves an Employee by its primary key.
     */
    public Employee getEmployeeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return repo.findById(id);
    }

    /**
     * Returns all employees in the system.
     */
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    /**
     * Updates an existing Employee entity.
     */
    public Employee updateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        System.out.println("📝 Updating employee: " + employee.getId());
        return repo.update(employee);
    }

    /**
     * Deletes an Employee by its ID.
     */
    public void deleteEmployee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        System.out.println("🗑️ Deleting employee with ID: " + id);

        try {
            repo.deleteById(id);
            System.out.println("✅ Employee deleted successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to delete employee: " + e.getMessage());
            throw new RuntimeException(
                    "Грешка при изтриване на служителя: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Create new employee (for testing purposes)
     * @param user User entity
     * @param employeeType Type of employee
     * @return Created Employee
     */
    public Employee createEmployee(User user, EmployeeType employeeType) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (employeeType == null) {
            throw new IllegalArgumentException("Employee type cannot be null");
        }

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setEmployeeType(employeeType);
        employee.setCompany(null);
        employee.setOffice(null);

        return repo.save(employee);
    }
}

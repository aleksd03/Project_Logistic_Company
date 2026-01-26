package org.informatics.service;

import org.informatics.dao.EmployeeDao;
import org.informatics.entity.Employee;
import org.informatics.entity.Company;
import org.informatics.entity.Office;
import org.informatics.entity.User;

public class EmployeeService {
    private final EmployeeDao repo = new EmployeeDao();

    public void createForUser(User user, Company company, Office office) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Employee e = new Employee();
        e.setUser(user);
        e.setCompany(company);
        e.setOffice(office);

        repo.save(e);
        System.out.println("Employee created for user: " + user.getEmail());
    }

    public Employee getEmployeeByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return repo.findByUserId(userId);
    }

    public Employee getEmployeeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return repo.findById(id);
    }
}
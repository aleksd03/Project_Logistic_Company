package org.informatics.logistic.service;

import org.informatics.logistic.dao.EmployeeRepository;
import org.informatics.logistic.entity.Employee;
import org.informatics.logistic.entity.LogisticCompany;
import org.informatics.logistic.entity.User;

public class EmployeeService {

    private final EmployeeRepository repo = new EmployeeRepository();

    public void createForUser(User user, LogisticCompany company) {
        Employee e = new Employee();
        e.setUser(user);
        e.setCompany(company);
        repo.save(e);
    }
}

package org.informatics.service;

import org.informatics.dao.CompanyDao;
import org.informatics.entity.Company;

public class CompanyService {
    private final CompanyDao repo = new CompanyDao();

    public Company createCompany(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }

        Company company = new Company();
        company.setName(name.trim());
        return repo.save(company);
    }

    public Company getCompanyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
        return repo.findById(id);
    }

    public Company getDefaultCompany() {
        Company defaultCompany = repo.findByName("ALVAS Logistics");
        if (defaultCompany != null) {
            return defaultCompany;
        }
        System.out.println("Creating default company: ALVAS Logistics");
        return createCompany("ALVAS Logistics");
    }

    public java.util.List<Company> getAllCompanies() {
        return repo.findAll();
    }
}

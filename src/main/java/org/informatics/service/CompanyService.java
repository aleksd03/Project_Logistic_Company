package org.informatics.service;

import org.informatics.dao.CompanyDao;
import org.informatics.entity.Company;

import java.util.List;

/**
 * Service layer for Company-related operations.
 * Handles validation and delegates persistence to CompanyDao.
 */
public class CompanyService {

    // DAO responsible for Company persistence
    private final CompanyDao repo = new CompanyDao();

    /**
     * Creates and persists a new Company.
     */
    public Company createCompany(String name) {
        // Validate company name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }

        Company company = new Company();
        company.setName(name.trim());

        return repo.save(company);
    }

    /**
     * Retrieves a Company by its primary key.
     */
    public Company getCompanyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
        return repo.findById(id);
    }

    /**
     * Retrieves the default system company.
     * Creates it if it does not already exist.
     */
    public Company getDefaultCompany() {
        Company defaultCompany = repo.findByName("ALVAS Logistics");
        if (defaultCompany != null) {
            return defaultCompany;
        }

        System.out.println("Creating default company: ALVAS Logistics");
        return createCompany("ALVAS Logistics");
    }

    /**
     * Returns all companies in the system.
     */
    public List<Company> getAllCompanies() {
        return repo.findAll();
    }

    /**
     * Updates an existing Company entity.
     */
    public Company updateCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }
        return repo.update(company);
    }

    /**
     * Deletes a Company by its ID.
     * Offices are deleted automatically via cascade.
     */
    public void deleteCompany(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }

        System.out.println("🗑️ Attempting to delete company with ID: " + id);

        try {
            repo.deleteById(id);
            System.out.println("✅ Company deleted successfully (with cascade to offices)!");
        } catch (Exception e) {
            System.err.println("❌ Failed to delete company: " + e.getMessage());
            throw new RuntimeException(
                    "Грешка при изтриване на компанията: " + e.getMessage(),
                    e
            );
        }
    }
}


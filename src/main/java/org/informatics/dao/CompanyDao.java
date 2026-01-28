package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Company;

import java.util.List;

/**
 * Data Access Object (DAO) for Company entities.
 * Handles all persistence operations related to Company.
 */
public class CompanyDao {

    /**
     * Persists a new Company entity in the database.
     */
    public Company save(Company company) {
        Transaction tx = null;
        Session session = null;
        try {
            // Open a new Hibernate session
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // Begin transaction
            tx = session.beginTransaction();

            // Persist makes the entity managed and schedules INSERT
            session.persist(company);

            // Commit transaction (executes INSERT)
            tx.commit();

            System.out.println("Company saved successfully with ID: " + company.getId());
            return company;

        } catch (Exception e) {
            // Roll back transaction on failure
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving company: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save company", e);

        } finally {
            // Always close session
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Updates an existing Company entity.
     */
    public Company update(Company company) {
        Transaction tx = null;

        // try-with-resources ensures session is closed automatically
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // merge() attaches detached entity and returns managed instance
            Company updated = session.merge(company);

            tx.commit();
            return updated;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR updating company: " + e.getMessage());
            throw new RuntimeException("Could not update company", e);
        }
    }

    /**
     * Deletes a Company by its ID.
     * Cascade settings handle deletion of related Office entities.
     */
    public void deleteById(Long id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Load Company entity by primary key
            Company company = session.find(Company.class, id);
            if (company != null) {
                // CASCADE removes related offices automatically
                session.remove(company);
                System.out.println("✅ Company deleted (cascade to offices): " + id);
            } else {
                System.err.println("❌ Company not found: " + id);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting company: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete company: " + e.getMessage(), e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds a Company by its primary key.
     */
    public Company findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Company.class, id);
        } catch (Exception e) {
            System.err.println("ERROR finding company by ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finds a Company entity by its unique name.
     */
    public Company findByName(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Company c WHERE c.name = :name",
                            Company.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("ERROR finding company by name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all Company entities from the database.
     */
    public List<Company> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company", Company.class).list();
        } catch (Exception e) {
            System.err.println("ERROR finding all companies: " + e.getMessage());
            return List.of();
        }
    }
}

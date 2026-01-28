package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Employee;

import java.util.List;

/**
 * Data Access Object (DAO) for Employee entities.
 * Provides CRUD operations for Employee.
 */
public class EmployeeDao {

    /**
     * Saves a new Employee or updates it if already existing.
     * merge() handles both transient and detached entities.
     */
    public Employee save(Employee employee) {
        Transaction tx = null;
        Session session = null;
        try {
            // Open Hibernate session
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // Start transaction
            tx = session.beginTransaction();

            // merge returns a managed instance attached to the session
            Employee merged = session.merge(employee);

            // Commit transaction
            tx.commit();

            System.out.println("Employee saved successfully with ID: " + merged.getId());
            return merged;

        } catch (Exception e) {
            // Roll back transaction on failure
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save employee", e);

        } finally {
            // Ensure session is closed
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Updates an existing Employee entity.
     */
    public Employee update(Employee employee) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Attach entity to persistence context and update it
            Employee updated = session.merge(employee);

            tx.commit();
            System.out.println("✅ Employee updated: " + updated.getId());
            return updated;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR updating employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not update employee", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds an Employee by its primary key.
     */
    public Employee findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    /**
     * Finds an Employee associated with a given User ID.
     */
    public Employee findByUserId(Long userId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            // Query Employee via its related User entity
            return session.createQuery(
                            "SELECT e FROM Employee e JOIN e.user u WHERE u.id = :userId",
                            Employee.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println("Error finding employee by user ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all Employee entities.
     */
    public List<Employee> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee", Employee.class).list();
        }
    }

    /**
     * Deletes an Employee entity using a managed instance.
     */
    public void delete(Employee employee) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Ensure entity is managed before removal
            Employee managedEmployee = session.merge(employee);
            session.remove(managedEmployee);

            tx.commit();
            System.out.println("✅ Employee deleted: " + managedEmployee.getId());

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete employee", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes an Employee entity by its ID.
     */
    public void deleteById(Long id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Find entity by ID
            Employee employee = session.find(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                System.out.println("✅ Employee deleted: " + id);
            } else {
                System.err.println("❌ Employee not found: " + id);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete employee: " + e.getMessage(), e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}


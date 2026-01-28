package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Office;

import java.util.List;

/**
 * Data Access Object (DAO) for Office entities.
 * Responsible for all persistence operations related to Office.
 */
public class OfficeDao {

    /**
     * Persists a new Office entity in the database.
     */
    public Office save(Office office) {
        Transaction tx = null;
        Session session = null;
        try {
            // Open Hibernate session
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // Begin transaction
            tx = session.beginTransaction();

            // Persist makes the entity managed and schedules INSERT
            session.persist(office);

            // Commit transaction
            tx.commit();

            System.out.println("Office saved successfully with ID: " + office.getId());
            return office;

        } catch (Exception e) {
            // Roll back transaction on error
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving office: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save office", e);

        } finally {
            // Always close session
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Updates an existing Office entity.
     */
    public Office update(Office office) {
        Transaction tx = null;

        // try-with-resources ensures automatic session closure
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // merge() attaches detached entity and returns managed instance
            Office updated = session.merge(office);

            tx.commit();
            return updated;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR updating office: " + e.getMessage());
            throw new RuntimeException("Could not update office", e);
        }
    }

    /**
     * Deletes an Office entity using a managed instance.
     */
    public void delete(Office office) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Ensure entity is managed before removal
            Office managedOffice = session.merge(office);
            session.remove(managedOffice);

            tx.commit();
            System.out.println("✅ Office deleted: " + office.getId());

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting office: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete office", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes an Office entity by its primary key.
     */
    public void deleteById(Long id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Find Office entity by ID
            Office office = session.find(Office.class, id);
            if (office != null) {
                session.remove(office);
                System.out.println("✅ Office deleted: " + id);
            } else {
                System.err.println("❌ Office not found: " + id);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting office: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete office: " + e.getMessage(), e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds an Office by its primary key.
     */
    public Office findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Office.class, id);
        } catch (Exception e) {
            System.err.println("ERROR finding office by ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns all Office entities ordered by ID.
     */
    public List<Office> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Office ORDER BY id",
                    Office.class
            ).list();
        } catch (Exception e) {
            System.err.println("ERROR finding all offices: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Returns all offices belonging to a specific company.
     */
    public List<Office> findByCompanyId(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Office o WHERE o.company.id = :companyId ORDER BY o.address",
                            Office.class)
                    .setParameter("companyId", companyId)
                    .list();
        } catch (Exception e) {
            System.err.println("ERROR finding offices by company ID: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds an Office by its unique address.
     */
    public Office findByAddress(String address) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Office o WHERE o.address = :address",
                            Office.class)
                    .setParameter("address", address)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("ERROR finding office by address: " + e.getMessage());
            return null;
        }
    }
}

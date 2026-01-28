package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Client;

import java.util.List;

/**
 * Data Access Object (DAO) for Client entities.
 * Encapsulates all database operations related to Client.
 */
public class ClientDao {

    /**
     * Saves a new Client or updates it if it already exists.
     * Uses merge() to handle both transient and detached entities.
     */
    public Client save(Client client) {
        Transaction tx = null;
        Session session = null;
        try {
            // Open a new Hibernate session
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // Begin database transaction
            tx = session.beginTransaction();

            // merge() returns a managed instance attached to the session
            Client merged = session.merge(client);

            // Commit transaction (flushes changes to DB)
            tx.commit();

            System.out.println("Client saved successfully with ID: " + merged.getId());
            return merged;

        } catch (Exception e) {
            // Roll back transaction if something goes wrong
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving client: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save client", e);

        } finally {
            // Always close the session to free DB resources
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Updates an existing Client entity in the database.
     */
    public Client update(Client client) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // merge() ensures the entity becomes managed before update
            Client updated = session.merge(client);

            tx.commit();
            return updated;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR updating client: " + e.getMessage());
            throw new RuntimeException("Could not update client", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes a Client entity using a managed instance.
     */
    public void delete(Client client) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Ensure entity is managed before deletion
            Client managedClient = session.merge(client);
            session.remove(managedClient);

            tx.commit();
            System.out.println("✅ Client deleted: " + client.getId());

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting client: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete client", e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes a Client entity by its database ID.
     */
    public void deleteById(Long id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Load entity by ID
            Client client = session.find(Client.class, id);
            if (client != null) {
                session.remove(client);
                System.out.println("✅ Client deleted: " + id);
            } else {
                System.err.println("❌ Client not found: " + id);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ ERROR deleting client: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not delete client: " + e.getMessage(), e);

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds a Client by its primary key.
     */
    public Client findById(Long id) {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Client.class, id);

        } catch (Exception e) {
            System.err.println("ERROR finding client by ID: " + e.getMessage());
            return null;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Returns all clients with associated User and Company eagerly loaded.
     * DISTINCT avoids duplicate results caused by JOIN FETCH.
     */
    public List<Client> findAll() {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // JPQL query with JOIN FETCH to avoid LazyInitializationException
            return session.createQuery(
                            "SELECT DISTINCT c FROM Client c " +
                                    "LEFT JOIN FETCH c.user " +
                                    "LEFT JOIN FETCH c.company",
                            Client.class)
                    .list();

        } catch (Exception e) {
            System.err.println("ERROR finding all clients: " + e.getMessage());
            e.printStackTrace();
            return List.of();

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds a Client associated with a specific User ID.
     */
    public Client findByUserId(Long userId) {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();

            // Query Client via its associated User entity
            return session.createQuery(
                            "SELECT c FROM Client c JOIN c.user u WHERE u.id = :userId",
                            Client.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println("ERROR finding client by user ID: " + e.getMessage());
            e.printStackTrace();
            return null;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

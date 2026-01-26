package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Shipment;

import java.util.List;

public class ShipmentDao {

    public Shipment save(Shipment shipment) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(shipment);
            tx.commit();
            System.out.println("Shipment saved successfully with ID: " + shipment.getId());
            return shipment;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving shipment: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save shipment", e);
        }
    }

    public Shipment update(Shipment shipment) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Shipment updated = session.merge(shipment);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR updating shipment: " + e.getMessage());
            throw new RuntimeException("Could not update shipment", e);
        }
    }

    public void delete(Shipment shipment) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(shipment);
            tx.commit();
            System.out.println("Shipment deleted: " + shipment.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR deleting shipment: " + e.getMessage());
            throw new RuntimeException("Could not delete shipment", e);
        }
    }

    public Shipment findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Shipment.class, id);
        } catch (Exception e) {
            System.err.println("ERROR finding shipment by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Shipment> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Shipment ORDER BY registrationDate DESC", Shipment.class).list();
        } catch (Exception e) {
            System.err.println("ERROR finding all shipments: " + e.getMessage());
            return List.of();
        }
    }

    public List<Shipment> findBySenderId(Long senderId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Shipment s WHERE s.sender.id = :senderId ORDER BY s.registrationDate DESC",
                            Shipment.class)
                    .setParameter("senderId", senderId)
                    .list();
        } catch (Exception e) {
            System.err.println("Error finding shipments by sender ID: " + e.getMessage());
            return List.of();
        }
    }

    public List<Shipment> findByReceiverId(Long receiverId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Shipment s WHERE s.receiver.id = :receiverId ORDER BY s.registrationDate DESC",
                            Shipment.class)
                    .setParameter("receiverId", receiverId)
                    .list();
        } catch (Exception e) {
            System.err.println("Error finding shipments by receiver ID: " + e.getMessage());
            return List.of();
        }
    }

    public List<Shipment> findByRegisteredBy(Long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Shipment s WHERE s.registeredBy.id = :employeeId ORDER BY s.registrationDate DESC",
                            Shipment.class)
                    .setParameter("employeeId", employeeId)
                    .list();
        } catch (Exception e) {
            System.err.println("Error finding shipments by employee ID: " + e.getMessage());
            return List.of();
        }
    }

    public List<Shipment> findUndelivered() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Shipment s WHERE s.status = 'SENT' ORDER BY s.registrationDate DESC",
                            Shipment.class)
                    .list();
        } catch (Exception e) {
            System.err.println("Error finding undelivered shipments: " + e.getMessage());
            return List.of();
        }
    }
}
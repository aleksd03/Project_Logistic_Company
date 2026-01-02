package org.informatics.logistic.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.logistic.configuration.SessionFactoryUtil;
import org.informatics.logistic.entity.Shipment;
import org.informatics.logistic.entity.ShipmentStatus;

import java.util.List;

public class ShipmentRepository {

    public void save(Shipment shipment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(shipment);
            tx.commit();
        }
    }

    public Shipment findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Shipment.class, id);
        }
    }

    public List<Shipment> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Shipment", Shipment.class).list();
        }
    }

    public List<Shipment> findSentNotReceived() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Shipment s where s.status = :st", Shipment.class
            ).setParameter("st", ShipmentStatus.SENT)
             .list();
        }
    }

    public List<Shipment> findByEmployee(Long employeeId) {
    try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
        return session.createQuery(
                "from Shipment s where s.registeredBy.id = :eid",
                Shipment.class
        ).setParameter("eid", employeeId)
         .list();
    }
}

public List<Shipment> findByClient(Long clientId) {
    try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
        return session.createQuery(
                "from Shipment s where s.sender.id = :cid or s.receiver.id = :cid",
                Shipment.class
        ).setParameter("cid", clientId)
         .list();
    }
}

public Double totalRevenue() {
    try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
        return session.createQuery(
                "select sum(s.price) from Shipment s where s.status = 'RECEIVED'",
                Double.class
        ).uniqueResult();
    }
}

}

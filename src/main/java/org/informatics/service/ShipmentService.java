package org.informatics.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.dao.ShipmentDao;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.ShipmentStatus;

import java.util.List;

public class ShipmentService {
    private final ShipmentDao repo = new ShipmentDao();

    public void registerShipment(
            Client sender,
            Client receiver,
            Employee employee,
            double price
    ) {
        Shipment s = new Shipment();
        s.setSender(sender);
        s.setReceiver(receiver);
        s.setRegisteredBy(employee);
        s.setPrice(price);
        s.setStatus(ShipmentStatus.SENT);

        repo.save(s);
    }

    public void markReceived(Long shipmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Shipment s = session.find(Shipment.class, shipmentId);
            if (s != null) {
                s.setStatus(ShipmentStatus.RECEIVED);
            }
            tx.commit();
        }
    }

    public List<Shipment> getShipmentsBySender(Long senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("Sender ID cannot be null");
        }
        return repo.findBySenderId(senderId);
    }

    public List<Shipment> getShipmentsByReceiver(Long receiverId) {
        if (receiverId == null) {
            throw new IllegalArgumentException("Receiver ID cannot be null");
        }
        return repo.findByReceiverId(receiverId);
    }

    public Shipment getShipmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Shipment ID cannot be null");
        }
        return repo.findById(id);
    }

    public List<Shipment> getAllShipments() {
        return repo.findAll();
    }
}

package org.informatics.logistic.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.logistic.configuration.SessionFactoryUtil;
import org.informatics.logistic.dao.ShipmentRepository;
import org.informatics.logistic.entity.*;

public class ShipmentService {

    private final ShipmentRepository repo = new ShipmentRepository();

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
            Shipment s = session.get(Shipment.class, shipmentId);
            s.setStatus(ShipmentStatus.RECEIVED);
            tx.commit();
        }
    }
}

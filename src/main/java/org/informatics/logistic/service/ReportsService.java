package org.informatics.logistic.service;

import org.informatics.logistic.dao.*;
import org.informatics.logistic.entity.*;

import java.util.List;

public class ReportsService {

    private final ShipmentRepository shipmentRepo = new ShipmentRepository();
    private final EmployeeRepository employeeRepo = new EmployeeRepository();
    private final ClientRepository clientRepo = new ClientRepository();

    public List<Employee> allEmployees() {
        return employeeRepo.findAll();
    }

    public List<Client> allClients() {
        return clientRepo.findAll();
    }

    public List<Shipment> allShipments() {
        return shipmentRepo.findAll();
    }

    public List<Shipment> shipmentsByEmployee(Long employeeId) {
        return shipmentRepo.findByEmployee(employeeId);
    }

    public List<Shipment> shipmentsByClient(Long clientId) {
        return shipmentRepo.findByClient(clientId);
    }

    public List<Shipment> sentNotReceived() {
        return shipmentRepo.findSentNotReceived();
    }

    public Double revenue() {
        Double r = shipmentRepo.totalRevenue();
        return r == null ? 0.0 : r;
    }
}

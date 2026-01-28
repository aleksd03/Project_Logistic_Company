package org.informatics.service;

import org.informatics.dao.ClientDao;
import org.informatics.dao.EmployeeDao;
import org.informatics.dao.ShipmentDao;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Shipment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for generating reports and statistics
 * based on shipments, employees, and clients.
 */
public class ReportService {

    // DAO repositories used for data retrieval
    private final ShipmentDao shipmentRepo = new ShipmentDao();
    private final EmployeeDao employeeRepo = new EmployeeDao();
    private final ClientDao clientRepo = new ClientDao();

    /**
     * Returns all employees in the system.
     */
    public List<Employee> allEmployees() {
        return employeeRepo.findAll();
    }

    /**
     * Returns all clients in the system.
     */
    public List<Client> allClients() {
        return clientRepo.findAll();
    }

    /**
     * Returns all shipments in the system.
     */
    public List<Shipment> allShipments() {
        return shipmentRepo.findAll();
    }

    /**
     * Returns all shipments registered by a specific employee.
     */
    public List<Shipment> shipmentsByEmployee(Long employeeId) {
        return shipmentRepo.findByRegisteredBy(employeeId);
    }

    /**
     * Returns all shipments sent by a specific client.
     */
    public List<Shipment> shipmentsSentByClient(Long clientId) {
        return shipmentRepo.findBySenderId(clientId);
    }

    /**
     * Returns all shipments received by a specific client.
     */
    public List<Shipment> shipmentsReceivedByClient(Long clientId) {
        return shipmentRepo.findByReceiverId(clientId);
    }

    /**
     * Returns all shipments that have been sent but not yet received.
     */
    public List<Shipment> sentNotReceived() {
        return shipmentRepo.findUndelivered();
    }

    /**
     * Calculates the total revenue from all shipments.
     */
    public Double totalRevenue() {
        List<Shipment> allShipments = shipmentRepo.findAll();
        return allShipments.stream()
                .mapToDouble(Shipment::getPrice)
                .sum();
    }

    /**
     * Calculates revenue for shipments registered within a given time period.
     */
    public Double revenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Shipment> allShipments = shipmentRepo.findAll();
        return allShipments.stream()
                // Include only shipments registered between the given dates
                .filter(s -> s.getRegistrationDate().isAfter(startDate)
                        && s.getRegistrationDate().isBefore(endDate))
                .mapToDouble(Shipment::getPrice)
                .sum();
    }

    /**
     * Counts shipments registered by a specific employee.
     */
    public int countShipmentsByEmployee(Long employeeId) {
        return shipmentRepo.findByRegisteredBy(employeeId).size();
    }

    /**
     * Counts shipments sent by a specific client.
     */
    public int countShipmentsSentByClient(Long clientId) {
        return shipmentRepo.findBySenderId(clientId).size();
    }

    /**
     * Counts shipments received by a specific client.
     */
    public int countShipmentsReceivedByClient(Long clientId) {
        return shipmentRepo.findByReceiverId(clientId).size();
    }
}

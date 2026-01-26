package org.informatics.service;

import org.informatics.dao.ClientDao;
import org.informatics.dao.EmployeeDao;
import org.informatics.dao.ShipmentDao;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Shipment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {

    private final ShipmentDao shipmentRepo = new ShipmentDao();
    private final EmployeeDao employeeRepo = new EmployeeDao();
    private final ClientDao clientRepo = new ClientDao();

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
        return shipmentRepo.findByRegisteredBy(employeeId);
    }

    public List<Shipment> shipmentsSentByClient(Long clientId) {
        return shipmentRepo.findBySenderId(clientId);
    }

    public List<Shipment> shipmentsReceivedByClient(Long clientId) {
        return shipmentRepo.findByReceiverId(clientId);
    }

    public List<Shipment> sentNotReceived() {
        return shipmentRepo.findUndelivered();
    }

    public Double totalRevenue() {
        List<Shipment> allShipments = shipmentRepo.findAll();
        return allShipments.stream()
                .mapToDouble(Shipment::getPrice)
                .sum();
    }

    public Double revenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Shipment> allShipments = shipmentRepo.findAll();
        return allShipments.stream()
                .filter(s -> s.getRegistrationDate().isAfter(startDate) &&
                        s.getRegistrationDate().isBefore(endDate))
                .mapToDouble(Shipment::getPrice)
                .sum();
    }

    public int countShipmentsByEmployee(Long employeeId) {
        return shipmentRepo.findByRegisteredBy(employeeId).size();
    }

    public int countShipmentsSentByClient(Long clientId) {
        return shipmentRepo.findBySenderId(clientId).size();
    }

    public int countShipmentsReceivedByClient(Long clientId) {
        return shipmentRepo.findByReceiverId(clientId).size();
    }
}
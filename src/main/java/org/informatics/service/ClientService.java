package org.informatics.service;

import org.informatics.dao.ClientDao;
import org.informatics.entity.Company;
import org.informatics.entity.Client;
import org.informatics.entity.User;

import java.util.List;

public class ClientService {
    private final ClientDao repo = new ClientDao();

    public void createForUser(User user, Company company) {
        Client c = new Client();
        c.setUser(user);
        c.setCompany(company);
        repo.save(c);
    }

    public Client getClientByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return repo.findByUserId(userId);
    }

    public Client getClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        return repo.findById(id);
    }

    public List<Client> getAllClients() {
        return repo.findAll();
    }
}
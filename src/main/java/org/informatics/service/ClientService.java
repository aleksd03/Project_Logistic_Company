package org.informatics.service;

import org.informatics.dao.ClientDao;
import org.informatics.entity.Client;
import org.informatics.entity.Company;
import org.informatics.entity.User;

import java.util.List;

/**
 * Service layer for Client-related operations.
 * Acts as a bridge between controllers and the Client DAO.
 */
public class ClientService {

    // DAO responsible for Client persistence
    private final ClientDao repo = new ClientDao();

    /**
     * Creates a Client entity associated with a given User and Company.
     */
    public Client createForUser(User user, Company company) {
        Client client = new Client();
        client.setUser(user);
        client.setCompany(company);
        return repo.save(client);
    }

    /**
     * Convenience wrapper for creating a Client.
     * Delegates to createForUser().
     */
    public Client createClient(User user, Company company) {
        return createForUser(user, company);
    }

    /**
     * Retrieves a Client by its primary key.
     */
    public Client getClientById(Long id) {
        return repo.findById(id);
    }

    /**
     * Returns all clients in the system.
     */
    public List<Client> getAllClients() {
        return repo.findAll();
    }

    /**
     * Retrieves a Client associated with a specific User ID.
     */
    public Client getClientByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    /**
     * Updates an existing Client.
     */
    public Client updateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        System.out.println("📝 Updating client: " + client.getId());
        return repo.update(client);
    }

    /**
     * Deletes a Client by its ID.
     */
    public void deleteClient(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }

        System.out.println("🗑️ Deleting client with ID: " + id);

        try {
            repo.deleteById(id);
            System.out.println("✅ Client deleted successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to delete client: " + e.getMessage());
            throw new RuntimeException(
                    "Грешка при изтриване на клиента: " + e.getMessage(),
                    e
            );
        }
    }
}

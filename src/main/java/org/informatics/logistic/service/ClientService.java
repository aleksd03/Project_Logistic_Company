package org.informatics.logistic.service;

import org.informatics.logistic.dao.ClientRepository;
import org.informatics.logistic.entity.Client;
import org.informatics.logistic.entity.LogisticCompany;
import org.informatics.logistic.entity.User;

public class ClientService {

    private final ClientRepository repo = new ClientRepository();

    public void createForUser(User user, LogisticCompany company) {
        Client c = new Client();
        c.setUser(user);
        c.setCompany(company);
        repo.save(c);
    }
}

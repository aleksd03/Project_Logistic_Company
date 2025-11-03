package org.informatics.logistic.data;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.informatics.logistic.configuration.SessionFactoryUtil;
import org.informatics.logistic.entity.User;

import java.util.Optional;

public class UserRepository {
    public void save(User user) {
        try (Session s = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(user);
            tx.commit();
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Session s = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> q = s.createQuery("from User u where u.email = :e", User.class);
            q.setParameter("e", email);
            return q.uniqueResultOptional();
        }
    }
}

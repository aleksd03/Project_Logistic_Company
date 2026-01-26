package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Company;

import java.util.List;

public class CompanyDao {
    public Company save(Company company) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
            System.out.println("Company saved successfully with ID: " + company.getId());
            return company;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving company: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save company", e);
        }
    }

    public Company update(Company company) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Company updated = session.merge(company);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR updating company: " + e.getMessage());
            throw new RuntimeException("Could not update company", e);
        }
    }

    public void delete(Company company) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(company);
            tx.commit();
            System.out.println("Company deleted: " + company.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR deleting company: " + e.getMessage());
            throw new RuntimeException("Could not delete company", e);
        }
    }

    public Company findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Company.class, id);
        } catch (Exception e) {
            System.err.println("ERROR finding company by ID: " + e.getMessage());
            return null;
        }
    }

    public Company findByName(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Company c WHERE c.name = :name",
                            Company.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("ERROR finding company by name: " + e.getMessage());
            return null;
        }
    }

    public List<Company> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company", Company.class).list();
        } catch (Exception e) {
            System.err.println("ERROR finding all companies: " + e.getMessage());
            return List.of();
        }
    }
}
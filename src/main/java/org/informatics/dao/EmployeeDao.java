package org.informatics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Employee;

import java.util.List;

public class EmployeeDao {

    public void save(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
            System.out.println("Employee saved successfully with ID: " + employee.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("ERROR saving employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save employee", e);
        }
    }

    public void update(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Could not update employee", e);
        }
    }

    public Employee findById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    public Employee findByUserId(Long userId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Employee e WHERE e.user.id = :userId",
                            Employee.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("Error finding employee by user ID: " + e.getMessage());
            return null;
        }
    }

    public List<Employee> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee", Employee.class).list();
        }
    }

    public void delete(Employee employee) {
        Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Could not delete employee", e);
        }
    }
}

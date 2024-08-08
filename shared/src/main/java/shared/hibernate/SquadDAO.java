package shared.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import shared.model.Squad;
import java.util.List;

public class SquadDAO {
    public void saveSquad(Squad squad) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(squad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Squad> getAllSquads() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Squad", Squad.class).list();
        }
    }

    public Squad getSquadById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select s from Squad s left join fetch s.members where s.id = :id", Squad.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public void updateSquad(Squad squad) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(squad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteSquad(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Squad squad = session.get(Squad.class, id);
            if (squad != null) {
                session.delete(squad);
                System.out.println("Squad is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}

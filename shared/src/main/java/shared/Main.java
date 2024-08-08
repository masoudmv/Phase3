package shared;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import shared.hibernate.HibernateUtil;
import shared.hibernate.PlayerDAO;
import shared.hibernate.SquadDAO;
import shared.model.Player;
import shared.model.Squad;
import shared.model.Status;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();


        Player player = session.get(Player.class, 90);
        System.out.println(player.getXP());


        // Close session factory
        HibernateUtil.getSessionFactory().close();
    }
}

package server;

import game.model.charactersModel.*;
import game.model.charactersModel.blackOrb.Orb;
import game.model.charactersModel.smiley.Hand;
import game.model.charactersModel.smiley.LeftHand;
import game.model.charactersModel.smiley.Smiley;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import picocli.CommandLine;
import server.cli.ServerCli;
import server.socket.SocketStarter;
import shared.hibernate.HibernateUtil;
import shared.model.Player;
import shared.model.Squad;
import shared.model.Status;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();


        ////

        LeftHand.loadImage();
        Hand.loadImage();
        Smiley.loadImage();
        SquarantineModel.loadImage();
        TrigorathModel.loadImage();
        OmenoctModel.loadImage();
        NecropickModel.loadImage();
        BabyArchmire.loadImage();
        ArchmireModel.loadImage();
        Orb.loadImage();
        BarricadosModel.loadImage();
        EpsilonModel.loadImage();


//        new Profile();
//        new Game(GameType.monomachia);
//        Skill.initializeSkills();


        updatePlayersFromDatabase();







        // Start the server
        SocketStarter socketStarter = new SocketStarter(dataBase);
        socketStarter.start();

        // Start the CLI in a separate thread
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            CommandLine commandLine = new CommandLine(new ServerCli(dataBase));
            while (true) {
                System.out.print("Enter command: ");
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    System.out.println("Exiting CLI...");
                    break;
                }
                commandLine.execute(command.split(" "));
            }
        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (Player player : dataBase.getAllPlayers()){
                        double now = System.currentTimeMillis();
                        double diff = now - player.getLastOnlineTime();
                        if (diff > 2000) player.setStatus(Status.offline);
                    }


                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


//    public static void main(String[] args) {
//
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        Session session = sessionFactory.openSession();
//        Transaction t = session.beginTransaction();
//
//        Player player = session.get(Player.class, "11111");
//
////        session.save(player);
////        t.commit();
//
//        System.out.println(player.getMacAddress());
//
//
//        HibernateUtil.getSessionFactory().close();
//    }


    public static void updatePlayersFromDatabase() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Fetch players from database
            List<Player> players = session.createQuery("from Player", Player.class).list();
            List<Squad> squads = session.createQuery("from Squad ", Squad.class).list();

            for ( Squad squad : squads ) {
                for (Player player : squad.getMembers()) {
                    player.setSquad(squad);
                }
            }

            // Update the in-memory database
            DataBase.getDataBase().setSquads(squads);
            DataBase.getDataBase().setPlayers(players);


            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to update players from database: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}

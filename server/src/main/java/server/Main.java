package server;

import game.controller.Game;
import game.controller.GameType;
import server.database.DataBase;
import game.model.charactersModel.*;
import game.model.charactersModel.blackOrb.Orb;
import game.model.charactersModel.smiley.Hand;
import game.model.charactersModel.smiley.LeftHand;
import game.model.charactersModel.smiley.Smiley;
import game.model.entities.Profile;
import game.model.entities.Skill;

import picocli.CommandLine;
import server.cli.ServerCli;
import server.socket.SocketStarter;
import shared.hibernate.HibernateUtil;
import shared.hibernate.PlayerDAO;
import shared.hibernate.SquadDAO;
import shared.model.Player;
import shared.model.Squad;
import shared.model.Status;

import java.util.Scanner;

public class Main {
//    public static void main(String[] args) {
//        DataBase dataBase = new DataBase();
//
//
//        ////
//
//        LeftHand.loadImage();
//        Hand.loadImage();
//        Smiley.loadImage();
//        SquarantineModel.loadImage();
//        TrigorathModel.loadImage();
//        OmenoctModel.loadImage();
//        NecropickModel.loadImage();
//        BabyArchmire.loadImage();
//        ArchmireModel.loadImage();
//        Orb.loadImage();
//        BarricadosModel.loadImage();
//        EpsilonModel.loadImage();
//
//
////        new Profile();
////        new Game(GameType.monomachia);
////        Skill.initializeSkills();
//
//
//
//
//
//
//
//        // Start the server
//        SocketStarter socketStarter = new SocketStarter(dataBase);
//        socketStarter.start();
//
//        // Start the CLI in a separate thread
//        new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            CommandLine commandLine = new CommandLine(new ServerCli(dataBase));
//            while (true) {
//                System.out.print("Enter command: ");
//                String command = scanner.nextLine();
//                if ("exit".equalsIgnoreCase(command)) {
//                    System.out.println("Exiting CLI...");
//                    break;
//                }
//                commandLine.execute(command.split(" "));
//            }
//        }).start();
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    for (Player player : dataBase.getAllPlayers()){
//                        double now = System.currentTimeMillis();
//                        double diff = now - player.getLastOnlineTime();
//                        if (diff > 2000) player.setStatus(Status.offline);
//                    }
//
//
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }

}

package server;

import game.controller.Game;
import game.example.GraphicalObject;
import game.model.charactersModel.blackOrb.BlackOrb;
import shared.Model.MyPolygon;
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

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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

        BufferedImage b = EpsilonModel.loadImage();
        GraphicalObject bows = new GraphicalObject(b);
        MyPolygon pol = bows.getMyBoundingPolygon();
        new EpsilonModel(new Point2D.Double(1000, 700), pol);

//        new SquarantineModel(new Point2D.Double(400, 500));
//        new OmenoctModel(new Point2D.Double(1400, 500));




        new Profile();
        new Game(); // uncoment this todo
        Skill.initializeSkills();


        new BlackOrb();
        new SquarantineModel(new Point2D.Double(200, 500));









        ////


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
    }
}

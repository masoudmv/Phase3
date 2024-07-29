package server;

import picocli.CommandLine;
import server.cli.ServerCli;
import server.socket.SocketStarter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();

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

package server.socket;

import server.ServersideReqHandler;
import server.database.DataBase;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketStarter extends Thread {
    private DataBase dataBase;
    private ServerSocket serverSocket;

    public SocketStarter(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                new ServersideReqHandler(new SocketResponseSender(socket), dataBase).start();
            }
        } catch (Exception e) {
            System.out.println('d');
        }
    }
}

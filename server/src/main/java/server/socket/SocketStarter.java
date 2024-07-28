package server.socket;

import server.ServersideReqHandler;
import server.DataBase;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketStarter extends Thread {
    private DataBase dataBase = new DataBase();

    private ServerSocket serverSocket;


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

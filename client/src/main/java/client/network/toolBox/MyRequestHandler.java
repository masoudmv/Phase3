package client.network.toolBox;

import client.network.socket.SocketResponseSender;
import shared.response.Response;

public class MyRequestHandler extends Thread{
    private SocketResponseSender socketResponseSender;

    public MyRequestHandler(SocketResponseSender socketResponseSender) {
        this.socketResponseSender = socketResponseSender;
    }

//    @Override
//    public void run() {
//        try {
//            while (true) {
//                Response response = socketResponseSender.getRequest().run(this);
//                socketResponseSender.sendResponse(response);
//            }
//        } catch (Exception e) {
//            socketResponseSender.close();
//        }
//    }
}

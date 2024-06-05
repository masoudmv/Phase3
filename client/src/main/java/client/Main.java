package client;


import client.socket.SocketRequestSender;
import shared.request.LoginRequest;
import shared.response.HiResponse;
import shared.response.ResponseHandler;

import java.io.IOException;

public class Main implements ResponseHandler {
    public static void main(String[] args) throws IOException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        socketRequestSender.sendRequest(new LoginRequest("asghar", "5689"));

    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }
}
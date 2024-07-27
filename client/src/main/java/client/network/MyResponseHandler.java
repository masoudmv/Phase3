package client.network;

import client.network.socket.SocketRequestSender;
import shared.response.HiResponse;
import shared.response.ResponseHandler;

public class MyResponseHandler implements ResponseHandler {
    private final SocketRequestSender socketRequestSender;

    public MyResponseHandler(SocketRequestSender socketRequestSender) {
        this.socketRequestSender = socketRequestSender;
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }
}

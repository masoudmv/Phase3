package client.network;

import client.network.socket.SocketRequestSender;
import client.network.toolBox.MyRequestHandler;
import shared.Model.Skill;
import shared.request.IdentificationRequest;
import shared.request.PurchaseSkillRequest;
import shared.response.ResponseHandler;

import java.io.IOException;

public class RequestFactory {
    private static final Status status = Status.getINSTANCE();
    private static SocketRequestSender socketRequestSender = status.getSocket();
    private  static final ClientsideResHandler requestHandler = status.getResponseHandler();
    private static final String macAddress = status.getPlayer().getMacAddress();



    public static void createIdentificateReq(){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new IdentificationRequest(macAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("Identification Req was not sent ...");
            throw new RuntimeException(e);
        }

    }

    public static void createIdentificateReq(String username){
        try {
            socketRequestSender.sendRequest(new IdentificationRequest(macAddress, username)).run(requestHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void createPurchaseSkillReq(Skill skill){
        try {
            socketRequestSender.sendRequest(new PurchaseSkillRequest(macAddress, skill)).run(requestHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

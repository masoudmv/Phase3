package client.network;

import client.network.socket.SocketRequestSender;

import shared.Model.NotificationType;
import shared.Model.Skill;
import shared.request.*;
import shared.request.leader.JoinDemandStatusReq;
import shared.request.leader.KickPlayerReq;
import shared.request.leader.PurchaseSkillRequest;
import shared.request.member.AskForSthRequest;
import shared.request.member.ReportAskedPleaRequest;
import shared.request.nonmember.CreateSquadRequest;
import shared.request.member.LeaveSquadReq;
import shared.request.nonmember.GetSquadsListRequest;
import shared.request.nonmember.JoinSquadReq;

import java.io.IOException;

public class RequestFactory {
    private static final Status status = Status.getINSTANCE();
    private  static final ClientsideResHandler requestHandler = status.getResponseHandler();
    private static final String macAddress = status.getPlayer().getMacAddress();
    private static SocketRequestSender socketRequestSender = status.getSocket();



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
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new IdentificationRequest(macAddress, username)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("Identification Req was not sent ...");
            throw new RuntimeException(e);
        }

    }

    public static void createPurchaseSkillReq(Skill skill){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new PurchaseSkillRequest(macAddress, skill)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("PurchaseSkill Req was not sent ...");
            throw new RuntimeException(e);
        }
    }

    public static void createCreateSquadReq(){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new CreateSquadRequest(macAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("createSquad Req was not sent ... ");
            throw new RuntimeException(e);
        }
    }

    public static void createGetSquadListReq(){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new GetSquadsListRequest(macAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("getSquadList Req was not sent ... ");
            throw new RuntimeException(e);
        }
    }

    public static void createLeaveSquadReq(){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new LeaveSquadReq(macAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("LeaveSquad Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }

    public static void createJoinSquadReq(String ownerMacAddress){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new JoinSquadReq(macAddress, ownerMacAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("JoinSquad Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }




    public static void createJoinDemandStatusReq(String demanderMacAddress, boolean accepted){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new JoinDemandStatusReq(macAddress, demanderMacAddress, accepted)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("JoinDemandStatusReq Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }

    public static void createKickPlayerReq(String kickedMacAddress){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new KickPlayerReq(macAddress, kickedMacAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("KickPlayerReq Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }


    public static void createAskForSthRequest(NotificationType type, String opponentMacAddress){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new AskForSthRequest(type, macAddress, opponentMacAddress)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("InitMonomachiaReq Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }


    public static void createReportAskedPleaRequest(NotificationType type, String requesterMacAddress, boolean accepted){
        socketRequestSender = status.getSocket();
        try {
            socketRequestSender.sendRequest(new ReportAskedPleaRequest(type, requesterMacAddress, macAddress, accepted)).run(requestHandler);
        } catch (IOException e) {
            System.out.println("InitMonomachiaReq Req was not sent ... ")  ;
            throw new RuntimeException(e);
        }
    }

}

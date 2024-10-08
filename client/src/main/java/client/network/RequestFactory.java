package client.network;

import client.network.socket.SocketRequestSender;

import shared.model.Input;
import shared.model.NotificationType;
import shared.model.Skill;
import shared.request.*;
import shared.request.game.*;
import shared.request.leader.JoinDemandStatusReq;
import shared.request.leader.KickPlayerReq;
import shared.request.leader.PurchaseSkillRequest;
import shared.request.member.AskForSthRequest;
import shared.request.member.ReportAskedPleaRequest;
import shared.request.nonmember.CreateSquadRequest;
import shared.request.member.LeaveSquadReq;
import shared.request.nonmember.GetSquadsListRequest;
import shared.request.nonmember.JoinSquadReq;
import shared.response.Response;

import java.awt.*;
import java.io.IOException;

import static java.awt.desktop.UserSessionEvent.Reason.LOCK;

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

    public static void createMoveRequest(Input input){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new MoveRequest(macAddress, input)).run(requestHandler);
            } catch (IOException e) {
                System.out.println("Move Req was not sent ... ")  ;
                throw new RuntimeException(e);
            }
        }
    }

    public static void createStateRequest(){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                Response response = socketRequestSender.sendRequest(new StateRequest());
                response.run(requestHandler);

            } catch (IOException e) {
                System.out.println("State Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }

    public static void createClickedRequest(Point position){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new ClickedRequest(position)).run(requestHandler);
            } catch (IOException e) {
                System.out.println("Clicked Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }



    public static void createPauseRequest(){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new PauseRequest(macAddress)).run(requestHandler);
            } catch (IOException e) {
                System.out.println("Pause Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }

    public static void createActivateAbilityRequest(){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new ActivateAbilityRequest(macAddress)).run(requestHandler);
                System.out.println("Activate Ability Request was sent ... ");
            } catch (IOException e) {
                System.out.println("Activate Ability Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }


    public static void createActivateSkillRequest(){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new ActivateSkillRequest(macAddress)).run(requestHandler);
                System.out.println("Activate Skill Request was sent ... ");
            } catch (IOException e) {
                System.out.println("Activate Skill Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }

    public static void createBuyAbilityRequest(String ability){
        synchronized (LOCK){
            socketRequestSender = status.getSocket();
            try {
                socketRequestSender.sendRequest(new BuyAbilityRequest(macAddress, ability)).run(requestHandler);
                System.out.println("buy ability Request was sent ... ");
            } catch (IOException e) {
                System.out.println("buy ability Request was not sent ... ");
                throw new RuntimeException(e);
            }
        }
    }
}

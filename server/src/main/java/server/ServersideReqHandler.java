package server;

import server.socket.SocketResponseSender;
import shared.Model.*;
import shared.request.*;
import shared.request.leader.JoinDemandStatusReq;
import shared.request.leader.KickPlayerReq;
import shared.request.leader.PurchaseSkillRequest;
import shared.request.member.AskForSthRequest;
import shared.request.member.ReportAskedPleaRequest;
import shared.request.nonmember.CreateSquadRequest;
import shared.request.member.DonateRequest;
import shared.request.member.LeaveSquadReq;
import shared.request.nonmember.GetSquadsListRequest;
import shared.request.nonmember.JoinSquadReq;
import shared.response.*;

public class ServersideReqHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;

    public ServersideReqHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
        this.dataBase = dataBase;
        this.socketResponseSender = socketResponseSender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = socketResponseSender.getRequest();
                if (request != null) {
                    synchronized (dataBase) {
                        Response response = request.run(this);
                        socketResponseSender.sendResponse(response);
                    }
                }
            }
        } catch (Exception e) {
            socketResponseSender.close();
        }
    }

    @Override
    public Response handleHiRequest(HiRequest hiRequest) {
        System.out.println("naisbdaVGFYDVS");
        return new HiResponse();
    }

    @Override
    public Response handleLoginRequest(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Response handleGetSquadsRequest(GetSquadsListRequest getSquadsListRequest) {
        Player player = dataBase.findPlayer(getSquadsListRequest.getMacAddress());
        Squad squad = player.getSquad();
        if (squad != null) return new GetSquadsListResponse("You are already in a squad!");
        return new GetSquadsListResponse(dataBase.getAllSquads());
    }

    @Override
    public Response handleCreateSquadRequest(CreateSquadRequest createSquadRequest) {
        String macAddress = createSquadRequest.getMACAddress();
        String message = dataBase.createSquad(macAddress);
        Player player = dataBase.findPlayer(macAddress);
        Squad squad = player.getSquad();
        return new CreateSquadResponse(message, player, squad);
    }

    @Override
    public Response handleIdentificationRequest(IdentificationRequest identificationRequest) {
        // most sensitive part of my program ...

        String macAddress = identificationRequest.getMACAddress();
        String username = identificationRequest.getUsername();
        dataBase.identificate(macAddress);
        if (username != null) dataBase.setUsername(macAddress, username);
        Player player = dataBase.findPlayer(macAddress);
        player.setLastOnlineTime(System.currentTimeMillis());
        Squad squad = player.getSquad();
        if (player.hasMessage()){
            player.setHasMessage(false);
            return new MessageResponse(player.getMessage());
        }
        if (player.hasJoinDemandMessage()) {
            player.setHasJoinDemandMessage(false);
            String macAdd = player.getJoinDemandMacAddress();
            String uName = player.getJoinDemandUsername();
            return new JoinDemandResponse(macAdd, uName);
        }



        if (player.hasNotification()){
            NotificationType type = player.getNotification().getNotificationType();
            System.out.println("notification type is indeed monomachia ...");
            return dataBase.sendNotificationToReceiver(type, player);




//            if (player.getNotification().getNotificationType() == NotificationType.MONOMACHIA) {
//                String macAddress1 = player.getNotification().getMacAddress();
//                String username1 = player.getNotification().getUsername();
//                player.setHasNotification(false); // should be after the two previous lines!
//                System.out.println("Sending message to Requester ...");
//                return new TransferReqToClientResponse(macAddress1, username1);
//            } else if (player.getNotification().getNotificationType() == NotificationType.SIMPLE_MESSAGE) {
//                String message1 = player.getNotification().getMessage();
//                player.setHasNotification(false);
//                System.out.println("Setting simple message ...");
//                return new MessageResponse(message1);
//            }
        }

        Squad opponent = dataBase.findOpponent(squad);

        return new IdentificationResponse(player, squad, opponent);
    }

    @Override
    public Response handleDonateRequest(DonateRequest donateRequest) {
        Player player = dataBase.findPlayer(donateRequest.getMacAddress());
        int amount = donateRequest.getAmount();
        String message = dataBase.donateToSquad(player, amount);
        return new DonateResponse(message);
    }

    @Override
    public Response handlePurchaseSkillRequest(PurchaseSkillRequest purchaseSkillRequest) {
        Player player = dataBase.findPlayer(purchaseSkillRequest.getMacAddress());
        Skill skill = purchaseSkillRequest.getSkill();
        String message = dataBase.purchaseSkill(player, skill);
        return new MessageResponse(message);
    }

    @Override
    public Response handleLeaveSquadReq(LeaveSquadReq leaveSquadReq) {
        Player player = dataBase.findPlayer(leaveSquadReq.getMacAddress());
        Squad squad = player.getSquad();

        if (squad.isInBattle()){
            Squad opponent = dataBase.findOpponent(squad);
            opponent.setInBattle(false);
            dataBase.getSquadPairs().remove(new Pair<>(squad, opponent));
        }

        String message = dataBase.sendOutFromSquad(player);
        return new LeaveSquadResponse(message);
    }

    @Override
    public Response handleJoinSquadReq(JoinSquadReq joinSquadReq) {
        Player player = dataBase.findPlayer(joinSquadReq.getMyMacAddress());
        Player leader = dataBase.findPlayer(joinSquadReq.getOwnerMacAddress());

        leader.setHasJoinDemandMessage(true);
        leader.setJoinDemandUsername(player.getUsername());
        leader.setJoinDemandMacAddress(player.getMacAddress());

        return new MessageResponse("Your join request has been sent successfully");
    }

    @Override
    public Response handleJoinDemandStatusReq(JoinDemandStatusReq joinDemandStatusReq) {
        // this message will be sent to the owner of the squad!
        Player leader = dataBase.findPlayer(joinDemandStatusReq.getOwnerMacAddress());
        Player demander = dataBase.findPlayer(joinDemandStatusReq.getDemanderMacAddress());
        Squad squad = leader.getSquad();
        boolean accepted = joinDemandStatusReq.isAccepted();
        String message = dataBase.joinPlayerToSquad(demander, squad, accepted);
        return new MessageResponse(message);
    }

    @Override
    public Response handleKickPlayerReq(KickPlayerReq kickPlayerReq) {
        // this message will be sent to the owner of the squad!
        String myMacAddress = kickPlayerReq.getMyMacAddress();
        Player me = dataBase.findPlayer(myMacAddress);
        Squad squad = me.getSquad();
        if (squad == null) return new MessageResponse("You are not in any squad!");
        String leaderMacAddress = squad.getOwner().getMacAddress();
        if (!leaderMacAddress.equals(myMacAddress)) return new MessageResponse("You are not the leader of squad!");
        String macAddressToBeKicked = kickPlayerReq.getPlayerMacAddress();
        Player playerToBeKicked = dataBase.findPlayer(macAddressToBeKicked);
        if (macAddressToBeKicked.equals(myMacAddress)) return new MessageResponse("You cannot kick out yourself!");
        String message = dataBase.kickPlayer(playerToBeKicked);
        return new MessageResponse(message);
    }

    @Override
    public Response handleAskForSthReq(AskForSthRequest askForSthRequest) {
        String requesterMacAddress = askForSthRequest.getRequesterMacAddress();
        Player requester = dataBase.findPlayer(requesterMacAddress);

        String receiverMacAddress = askForSthRequest.getReceiverMacAddress();
        Player receiver = dataBase.findPlayer(receiverMacAddress);

        NotificationType type  = askForSthRequest.getNotificationType();

        String message = dataBase.sth(requester, receiver, type);
        return new MessageResponse(message);
    }

    @Override
    public Response handleReportAskedPleaReq(ReportAskedPleaRequest reportAskedPleaRequest) {
        String requesterMacAddress = reportAskedPleaRequest.getRequesterMacAddress();
        String acceptorMacAddress = reportAskedPleaRequest.getAcceptorMacAddress();

        Player requester = dataBase.findPlayer(requesterMacAddress);
        Player receiver = dataBase.findPlayer(acceptorMacAddress);

        NotificationType type = reportAskedPleaRequest.getNotificationType();
        boolean accepted = reportAskedPleaRequest.isAccepted();

        String message = dataBase.getRequestStatus(type, requester, receiver, accepted);
        return new MessageResponse(message);

//        if (!accepted) {
//            Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE, "Your Monomachia challenge request was not accepted");
//            requester.setNotification(notification);
//            System.out.println("Monomachia challenge request was not accepted");
//            return new MessageResponse("You did not accept the monomachia battle challenge");
//        } else {
//            Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE, "Your Monomachia challenge request was accepted. It will start in 15 seconds.");
//            requester.setNotification(notification);
//            System.out.println("Monomachia challenge request was accepted");
//            return new MessageResponse("Monomachia challenge will start in 15 seconds!");
//        }
    }

}

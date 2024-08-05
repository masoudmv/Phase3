package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.controller.Game;
import game.controller.Utils;
import game.model.charactersModel.BulletModel;
import game.model.charactersModel.EpsilonModel;
import game.model.entities.Ability;
import game.model.entities.Profile;
import game.model.movement.Direction;
import server.socket.SocketResponseSender;
import shared.Model.*;
import shared.request.*;
import shared.request.game.*;
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
import shared.response.game.NullResponse;
import shared.response.game.PauseResponse;
import shared.response.game.StateResponse;

import javax.swing.*;
import java.awt.geom.Point2D;


import static game.controller.UserInterfaceController.fireAbility;
import static game.controller.Utils.addVectors;
import static game.controller.Utils.multiplyVector;
import static shared.constants.Constants.BULLET_VELOCITY;

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
        System.out.println("Message: " + message);
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

    @Override
    public Response handleMoveReq(MoveRequest moveRequest) {
        synchronized (dataBase){
            Game game  = dataBase.findGame("1");
            if (game.isPaused()) return new NullResponse();


            double deltaX = 0;
            double deltaY = 0;

            switch (moveRequest.getInput()){
                case move_up -> deltaY -= 0.7;
                case move_right -> deltaX += 0.7;
                case move_down -> deltaY += 0.7;
                case move_left -> deltaX -= 0.7;
            }



            String macAddress = "1";



            for (EpsilonModel epsilon : game.epsilons){

                System.out.println("EPSILON: " + epsilon.getMacAddress());
                if (epsilon.getMacAddress().equals("1")){

                    Point2D.Double vector = new Point2D.Double(deltaX, deltaY);
                    Point2D point = multiplyVector(epsilon.getDirection().getNormalizedDirectionVector(),
                            epsilon.getDirection().getMagnitude());
                    Direction direction = new Direction(addVectors(point, vector));
                    direction.adjustEpsilonDirectionMagnitude();


                    epsilon.setDirection(direction);
                    System.out.println("found epsilon");
                }
            }



            return new NullResponse();
        }
    }

    @Override
    public Response handleStateRequest(StateRequest stateRequest) {

        synchronized (dataBase) {
            StateResponse stateResponse = new StateResponse();


//            String macAddress = stateRequest.getMacAddress();
//            Player player = dataBase.findPlayer(macAddress);
            String gameID = "1";



            GameData gameData = dataBase.findGameData(gameID);


            stateResponse.setCreatedEntities(gameData.getCreatedEntities());
            stateResponse.setCreatedPanels(gameData.getCreatedPanels());
            stateResponse.setEliminatedEntities(gameData.getEliminatedEntities());
            stateResponse.setUpdatedPanels(gameData.getUpdatedPanels());
            stateResponse.setUpdatedModels(gameData.getUpdatedModels());



            return stateResponse;
        }
    }

    @Override
    public Response handleClickedRequest(ClickedRequest clickedRequest) {
        synchronized (dataBase){

            String gameID = "1";
            Game game  = dataBase.findGame(gameID);
            if (game.isPaused()) return new NullResponse();

            // todo find real gameID

            String macAddress = "1";


            for (EpsilonModel epsilon : game.epsilons){
                if (epsilon.getMacAddress().equals(macAddress)){


                    Point2D direction = calculateDirection(clickedRequest, epsilon);

                    double now = dataBase.findGame(gameID).ELAPSED_TIME;
                    double empowerInitTime = dataBase.findGame(gameID).getProfile().empowerInitiationTime;

                    Ability ability = dataBase.findGame(gameID).getProfile().activatedAbilities.get(macAddress);
                    if (ability == Ability.SLAUGHTER){
                        new BulletModel(epsilon.getAnchor(), new Direction(direction), gameID, macAddress, 50);
                        dataBase.findGame(gameID).getProfile().activatedAbilities.put(macAddress, null);
                    } else new BulletModel(epsilon.getAnchor(), new Direction(direction), gameID, macAddress);

                    boolean a = Ability.EMPOWER == dataBase.findGame(gameID).getProfile().activatedAbilities.get(macAddress);
//
                    if (now - empowerInitTime < 10 && a){
                        double angle = 10;
                        Point2D right = Utils.rotateVector(direction, Math.toRadians(angle));
                        new BulletModel(epsilon.getAnchor(), new Direction(right), gameID, macAddress);
                        Point2D left = Utils.rotateVector(direction, -Math.toRadians(angle));
                        new BulletModel(epsilon.getAnchor(), new Direction(left), gameID, macAddress);
                    }
                }

            }

            return new NullResponse();
        }
    }

    @Override
    public Response handlePauseRequest(PauseRequest pauseRequest) {
//        String macAddress = pauseRequest.getMacAddress();
//        Player player = dataBase.findPlayer(macAddress);
//        int inMenu = player.getInMenuTime();


        // todo use real gameID
        Game game = dataBase.findGame("1");

        if (!game.isPaused()){
            game.setPaused(true);
        } else {
            long pausedTime = game.getPausedTime();
            long now = System.currentTimeMillis();
            long pauseDuration = pausedTime - now;
            // todo add duration to player

            game.setPaused(false);
        }

        return new PauseResponse(true);
    }

    @Override
    public Response handleBuyAbilityRequest(BuyAbilityRequest buyAbilityRequest) {

//        if (ability == Ability.SLAUGHTER){
//            double now = Game.ELAPSED_TIME;
//            double initiation = Profile.getCurrent().slaughterInitiationTime;
//            if (now - initiation < 120) {
//                JOptionPane.showMessageDialog(frame, "You should wait at least 120 seconds after your previous slaughter!");
//                return;
//            }
//        }
//        int currentXP = Game.inGameXP;
//        if (currentXP >= ability.getCost()) {
//            Game.inGameXP -= ability.getCost();
//            Ability.activeAbility = ability;
//            if (ability == Ability.SLAUGHTER) Profile.getCurrent().slaughterInitiationTime = Game.ELAPSED_TIME;
//            JOptionPane.showMessageDialog(frame, ability.getName() + " was successfully activated");
//        } else {
//            JOptionPane.showMessageDialog(frame, "You don't have enough XP!", "Error", JOptionPane.ERROR_MESSAGE);
//        }


        return null;
    }

    @Override
    public Response handleActivateAbilityRequest(ActivateAbilityRequest activateAbilityRequest) {
        System.out.println("ability Req");

        String gameID = "1";
        String macAddress = "1";

        fireAbility(gameID, macAddress);

        return new NullResponse();
    }



    private static Point2D calculateDirection(ClickedRequest clickedRequest, EpsilonModel epsilon) {
        double startX = epsilon.getAnchor().getX();
        double startY = epsilon.getAnchor().getY();

        double mouseX = clickedRequest.getPosition().getX();
        double mouseY = clickedRequest.getPosition().getY();

        double deltaX = mouseX - startX;
        double deltaY = mouseY - startY;
        double pot = Math.hypot(deltaX, deltaY);

        double velX = deltaX * (BULLET_VELOCITY / pot);
        double velY = deltaY * (BULLET_VELOCITY / pot);
        Point2D direction = new Point2D.Double(velX, velY);
        return direction;
    }
}

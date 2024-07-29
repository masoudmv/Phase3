package server;

import server.socket.SocketResponseSender;
import shared.Model.Player;
import shared.Model.Skill;
import shared.Model.Squad;
import shared.request.*;
import shared.response.*;

import java.sql.Struct;

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

        return new IdentificationResponse(player, squad);
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
        String message = dataBase.joinPlayerToSquad(demander, squad);
        return new MessageResponse(message);
    }

    @Override
    public Response handleKickPlayerReq(KickPlayerReq kickPlayerReq) {
        // this message will be sent to the owner of the squad!
        String myMacAddress = kickPlayerReq.getMyMacAddress();
        Player me = dataBase.findPlayer(myMacAddress);
        Squad squad = me.getSquad();
        String leaderMacAddress = squad.getOwner().getMacAddress();
        if (!leaderMacAddress.equals(myMacAddress)) return new MessageResponse("You are not the leader of squad!");


        Player player = dataBase.findPlayer(kickPlayerReq.getPlayerMacAddress());



        return null;
    }
}

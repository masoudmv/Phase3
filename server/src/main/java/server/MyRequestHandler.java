package server;

import server.socket.SocketResponseSender;
import shared.Model.Squad;
import shared.request.*;
import shared.response.*;

import java.util.List;

public class MyRequestHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;

    public MyRequestHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
        this.dataBase = dataBase;
        this.socketResponseSender = socketResponseSender;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Response response = socketResponseSender.getRequest().run(this);
                socketResponseSender.sendResponse(response);
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
//        dataBase.getUsers().add(new Player(loginRequest.getUsername(),loginRequest.getPassword()));
        return null;
    }

    @Override
    public Response handleGetSquadsRequest(GetSquadsListRequest getSquadsListRequest) {
        System.out.println("in server ...");
        List<Squad> out = dataBase.getAllSquads();
        return new GetSquadsListResponse(out);
    }

    @Override
    public Response handleCreateSquadRequest(CreateSquadRequest createSquadRequest) {
        // TODO ...

        System.out.println("in server ...");
        return new CreateSquadResponse();
    }

    @Override
    public Response handleIdentificationRequest(IdentificationRequest identificationRequest) {
        String macAddress = identificationRequest.getMACAddress();
        String username = identificationRequest.getUsername();
        if (username != null){
            dataBase.setUsername(macAddress, username);
            System.out.println("username is set to " + username);
        }
        username = dataBase.identificate(macAddress);
        return new IdentificationResponse(username);
    }
}

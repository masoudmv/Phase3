package server;

import server.socket.SocketResponseSender;
import shared.Model.Squad;
import shared.request.GetSquadsListRequest;
import shared.request.HiRequest;
import shared.request.LoginRequest;
import shared.request.RequestHandler;
import shared.response.GetSquadsListResponse;
import shared.response.HiResponse;
import shared.response.Response;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;

    public ClientHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
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
        List<Squad> out = dataBase.getSquads();
        return new GetSquadsListResponse(out);
    }
}

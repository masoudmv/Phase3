package client.network;

import client.network.containers.SquadListPanel;
import client.network.containers.MainFrame;
import client.network.game.view.ClientDataBase;
import client.network.game.view.FinalPanelView;
import client.network.game.view.charactersView.*;
import shared.Model.*;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;
import shared.response.*;
import shared.response.game.NullResponse;
import shared.response.game.StateResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import static client.network.game.controller.UserInterfaceController.*;
import static client.network.game.view.FinalPanelView.finalPanelViews;
import static client.network.game.view.charactersView.GeoShapeView.geoShapeViews;

public class ClientsideResHandler implements ResponseHandler {

    public ClientsideResHandler() {
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }

    @Override
    public void handleGetSquadsListResponse(GetSquadsListResponse getSquadsListResponse) {
        String message = getSquadsListResponse.getMessage();
        if (message != null) {
            MainFrame frame = MainFrame.getINSTANCE();
            JOptionPane.showMessageDialog(frame, message);
            return;
        }
        List<Squad> squads = getSquadsListResponse.getList();
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));
//        squads.add(new Squad(new Player("massooood")));

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getINSTANCE();
            SquadListPanel squadListPanel = new SquadListPanel(squads, frame);
            frame.switchToPanel(squadListPanel);
        });
    }

    @Override
    public void handleCreateSquadResponse(CreateSquadResponse createSquadResponse) {

        String message = createSquadResponse.getMessage();
        MainFrame frame = MainFrame.getINSTANCE();
        JOptionPane.showMessageDialog(frame, message);


        Player player = createSquadResponse.getPlayer();
        Status.getINSTANCE().getPlayer().setSquad(createSquadResponse.getSquad());
    }

    @Override
    public void handleIdentificationResponse(IdentificationResponse identificationResponse) {
        System.out.println("handling identification response");
        Player player = identificationResponse.getPlayer();
        if (player != null) {
            // todo eww ...
            Status.getINSTANCE().setPlayer(player);
            Status.getINSTANCE().getPlayer().setSquad(identificationResponse.getSquad());
            Status.getINSTANCE().setOpponent(identificationResponse.getOpponent());
//            Status.getINSTANCE().getPlayer().getSquad().setOpponent(identificationResponse.getOpponent());
        }
    }


    // todo the following three responses can be handled with a single response class ...
    @Override
    public void handleDonateResponse(DonateResponse donateResponse) {
        String message = donateResponse.getMessage();
        MainFrame frame = MainFrame.getINSTANCE();
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void handlePurchaseSkillResponse(MessageResponse messageResponse) {
        String message = messageResponse.getMessage();
        MainFrame frame = MainFrame.getINSTANCE();
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void handleLeaveSquadResponse(LeaveSquadResponse leaveSquadResponse) {
        String message = leaveSquadResponse.getMessage();
        MainFrame frame = MainFrame.getINSTANCE();
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void handleJoinDemandResponse(JoinDemandResponse joinDemandResponse) {
        String demanderMacAddress = joinDemandResponse.getMacAddress();
        String username = joinDemandResponse.getUsername();
        MainFrame frame = MainFrame.getINSTANCE();

        int response = JOptionPane.showConfirmDialog(
                frame,
                "User " + username + " wants to join your squad. Do you agree?",
                "Join Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            // Handle accept action
            RequestFactory.createJoinDemandStatusReq(demanderMacAddress, true);

//            System.out.println(username + " has been accepted to join the squad.");
            // Add your code here to handle the acceptance
        } else if (response == JOptionPane.NO_OPTION) {
            // Handle decline action
            RequestFactory.createJoinDemandStatusReq(demanderMacAddress, false);
//            System.out.println(username + " has been declined to join the squad.");
            // Add your code here to handle the declination
        }
    }

    @Override
    public void handleTransferReqToClientResponse(TransferReqToClientResponse monomachiaInvitationResponse) {
        NotificationType type = monomachiaInvitationResponse.getNotificationType();

        switch (type) {
            case MONOMACHIA -> {
                System.out.println("handle monomachia request");
                String requesterMacAddress = monomachiaInvitationResponse.getMacAddress();

                String username = monomachiaInvitationResponse.getUsername();
                MainFrame frame = MainFrame.getINSTANCE();

                int response = JOptionPane.showOptionDialog(
                        frame,
                        username + " challenges you to a monomachia battle. Do you accept?",
                        "Monomachia Invitation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Accept", "Decline"}, // Custom button text
                        "Accept" // Default button
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Handle accept action
                    System.out.println("handle monomachia request");
                    // Add your logic here to handle the acceptance
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, true);
                    System.out.println("handle monomachia request");

                } else if (response == JOptionPane.NO_OPTION) {
                    // Handle decline action
                    // Add your logic here to handle the declination
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, false);

                }
            }
            case COLOSSEUM -> {
                String requesterMacAddress = monomachiaInvitationResponse.getMacAddress();

                String username = monomachiaInvitationResponse.getUsername();
                MainFrame frame = MainFrame.getINSTANCE();

                int response = JOptionPane.showOptionDialog(
                        frame,
                        username + " challenges you to a Colosseum battle. Do you accept?",
                        "Colosseum Invitation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Accept", "Decline"}, // Custom button text
                        "Accept" // Default button
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Handle accept action
                    System.out.println("handle monomachia request");
                    // Add your logic here to handle the acceptance
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, true);
                    System.out.println("handle monomachia request");

                } else if (response == JOptionPane.NO_OPTION) {
                    // Handle decline action
                    // Add your logic here to handle the declination
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, false);

                }
            }
            case SUMMON -> {
                String requesterMacAddress = monomachiaInvitationResponse.getMacAddress();

                String username = monomachiaInvitationResponse.getUsername();
                MainFrame frame = MainFrame.getINSTANCE();

                int response = JOptionPane.showOptionDialog(
                        frame,
                        username + " Summons you to a Monomachia battle. Do you accept?",
                        "Colosseum Invitation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Accept", "Decline"}, // Custom button text
                        "Accept" // Default button
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Handle accept action
                    System.out.println("handle monomachia request");
                    // Add your logic here to handle the acceptance
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, true);
                    System.out.println("handle monomachia request");

                } else if (response == JOptionPane.NO_OPTION) {
                    // Handle decline action
                    // Add your logic here to handle the declination
                    RequestFactory.createReportAskedPleaRequest(type, requesterMacAddress, false);

                }
            }

            case JOIN -> System.out.println();
            case SIMPLE_MESSAGE -> {
                String message = monomachiaInvitationResponse.getMessage();
                MainFrame frame = MainFrame.getINSTANCE();
                JOptionPane.showMessageDialog(frame, message);
            }
        }

    }

    @Override
    public void handleNullResponse(NullResponse nullResponse) {

    }

    @Override
    public void handleStateResponse(StateResponse stateResponse) {

        Map<String, EntityType> createdEntities = stateResponse.getCreatedEntities();
        List<String> eliminates = stateResponse.getEliminatedEntities();


        for (Map.Entry<String, EntityType> createdEntity : createdEntities.entrySet()) {
            String id = createdEntity.getKey();
            EntityType type = createdEntity.getValue();
            if (!ClientDataBase.models.containsKey(id)) {
                SwingUtilities.invokeLater(() ->  {
                    switch (type) {
                        case bullet -> new BulletView(id);
                        case epsilon -> new EpsilonView(id);
                        case collectible -> new CollectibleView(id);
                        case trigorath, squarantine, simplePolygon -> {
                            new TrigorathView(id);
                        }
                        case babyEpsilon -> createBabyEpsilonView(id);
                        case orb -> createOrbView(id);
                        case laser -> createLaserView(id);
                    }
                }) ;
            }
        }



        Map<String, DummyModel> models = stateResponse.getUpdatedModels();
        ClientDataBase.models.putAll(models);

        Map<String, DummyPanel> createdPanels = stateResponse.getCreatedPanels();
        for (Map.Entry<String, DummyPanel> createdPanel : createdPanels.entrySet()) {
            String id = createdPanel.getKey();
            if (!ClientDataBase.panels.containsKey(id)) {

                DummyPanel panel = createdPanel.getValue();
                Point2D location = panel.getLocation();
                Dimension dimension = panel.getDimension();

                // Ensure Swing components are manipulated on the EDT
                 SwingUtilities.invokeLater(() -> new FinalPanelView(id, location, dimension));
            }
        }



        Map<String, DummyPanel> panels = stateResponse.getUpdatedPanels();
        ClientDataBase.panels.putAll(panels);


        for (DummyModel model : ClientDataBase.models.values()) {
            if (eliminates.contains(model.getId())) {
                ClientDataBase.models.values().remove(model.getId());
            }
        }

        for (GeoShapeView view : geoShapeViews){
            if (eliminates.contains(view.getId())) {
                view.eliminate();
            }
        }

        for (FinalPanelView panelView : finalPanelViews){
            if (eliminates.contains(panelView.getId())) {
                panelView.eliminate();
            }
        }

        updateGeoShapeViewProperties();


//        double b = System.currentTimeMillis();
//        System.out.println("models size  " + ClientDataBase.models.size());
//        System.out.println("panels size  " + ClientDataBase.panels.size());
//        System.out.println("views  " + geoShapeViews.size());
//        System.out.println("panelVies  " + finalPanelViews.size());
//        System.out.println("eliminates  " + stateResponse.getEliminatedEntities().size());
//        System.out.println("created  " + stateResponse.getCreatedPanels().size());
//        System.out.println("updates models " + stateResponse.getUpdatedModels().size());
//        System.out.println("update panels  " + stateResponse.getUpdatedPanels().size());


//        long b = System.currentTimeMillis();
//
//        System.out.println("User Input Handling Time: " + (b - a) + " ms");
//        client.network.game.view.MainFrame.getINSTANCE().repaint();

//        SwingUtilities.invokeLater(client.network.game.view.MainFrame.getINSTANCE()::revalidate);
        SwingUtilities.invokeLater(client.network.game.view.MainFrame.getINSTANCE()::repaint);
    }



}


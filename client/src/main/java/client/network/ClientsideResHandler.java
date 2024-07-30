package client.network;

import client.network.containers.SquadListPanel;
import client.network.containers.MainFrame;
import shared.Model.NotificationType;
import shared.Model.Player;
import shared.Model.Squad;
import shared.response.*;

import javax.swing.*;
import java.util.List;

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


}


package client.network.toolBox;

import client.network.Status;
import client.network.socket.SocketRequestSender;
import shared.Model.Player;
import shared.Model.Squad;
import shared.request.CreateSquadRequest;
import shared.request.GetSquadsListRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SquadMenu extends JPanel {
    private JLabel usernameLabel;
    private JLabel usernameDisplayLabel;
    private JButton backButton;
    private JButton listSquadsButton;
    private JButton createSquadButton;
    private JPanel mySquadPanelContainer;
    private Timer updateTimer;
    private JButton leaveSquadButton;
    private JButton squadAbilitiesButton;

    public SquadMenu() {
        setLayout(new GridBagLayout());
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setVisible(true);

        backButton = new JButton("Back");
        usernameLabel = new JLabel("Username:");
        usernameDisplayLabel = new JLabel("Not set");
        listSquadsButton = new JButton("List Squads");
        createSquadButton = new JButton("Create Squad");
        leaveSquadButton = new JButton("Leave Squad");
        squadAbilitiesButton = new JButton("Squad Abilities");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(backButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(usernameLabel, gbc);

        gbc.gridx = 2;
        add(usernameDisplayLabel, gbc);

        gbc.gridy = 2;
        add(listSquadsButton, gbc);

        gbc.gridy = 3;
        add(createSquadButton, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mySquadPanelContainer = new JPanel(new BorderLayout());
        add(mySquadPanelContainer, gbc);

        gbc.gridy = 5;
        add(squadAbilitiesButton, gbc);
        squadAbilitiesButton.setVisible(false);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(leaveSquadButton, gbc);
        leaveSquadButton.setVisible(false);

        backButton.addActionListener(new BackAction());
        listSquadsButton.addActionListener(new ListSquadsAction());
        createSquadButton.addActionListener(new CreateSquadAction());
        leaveSquadButton.addActionListener(new LeaveSquadAction());
        squadAbilitiesButton.addActionListener(new SquadAbilitiesAction());

        centerPanel();
        updateUsernameDisplay();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        updateTimer = new Timer(1000, e -> updateSquadStatus());
        updateTimer.start();
    }

    private void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }

    private class BackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stopUpdateTimer();
            MainFrame frame = MainFrame.getINSTANCE();
            frame.switchToPanel(Menu.getINSTANCE());
            frame.repaint();
        }
    }

    private class ListSquadsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Status status = Status.getINSTANCE();
                SocketRequestSender socket = status.getSocket();
                socket.sendRequest(new GetSquadsListRequest()).run(status.getResponseHandler());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class CreateSquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String macAddress = Status.getINSTANCE().getPlayer().getMacAddress();
            SocketRequestSender socket = Status.getINSTANCE().getSocket();
            try {
                socket.sendRequest(new CreateSquadRequest(macAddress)).run(Status.getINSTANCE().getResponseHandler());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class LeaveSquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = Status.getINSTANCE().getPlayer();
            if (player != null && player.getSquad() != null) {
                player.setSquad(null); // Remove squad reference
                JOptionPane.showMessageDialog(SquadMenu.this, "You have left the squad.");
                updateMySquadPanel();
            }
        }
    }

    private class SquadAbilitiesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame frame = MainFrame.getINSTANCE();
            frame.switchToPanel(new SquadAbilitiesPanel());
            frame.repaint();
        }
    }

    public void updateSquadStatus() {
        Player player = Status.getINSTANCE().getPlayer();
        if (player != null && player.getSquad() != null) {
            leaveSquadButton.setVisible(true);
            squadAbilitiesButton.setVisible(true);
        } else {
            leaveSquadButton.setVisible(false);
            squadAbilitiesButton.setVisible(false);
        }
        updateMySquadPanel();
    }

    private void updateMySquadPanel() {
        Player player = Status.getINSTANCE().getPlayer();
        mySquadPanelContainer.removeAll();

        if (player != null && player.getSquad() != null) {
            JPanel squadPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            Squad squad = player.getSquad();
            for (Player member : squad.getMembers()) {
                JLabel memberLabel = new JLabel(member.getUsername() + " - XP: " + member.getXP() + " - Status: " + member.getStatus());
                gbc.gridx = 0;
                gbc.gridy++;
                squadPanel.add(memberLabel, gbc);

                JButton kickButton = new JButton("Kick");
                gbc.gridx = 1;
                squadPanel.add(kickButton, gbc);

                kickButton.addActionListener(new KickMemberAction(member));
            }

            mySquadPanelContainer.add(new JScrollPane(squadPanel), BorderLayout.CENTER);
        } else {
            JLabel noSquadLabel = new JLabel("You are not in any squad currently");
            noSquadLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mySquadPanelContainer.add(noSquadLabel, BorderLayout.CENTER);
        }

        mySquadPanelContainer.revalidate();
        mySquadPanelContainer.repaint();
    }

    private class KickMemberAction implements ActionListener {
        private Player member;

        public KickMemberAction(Player member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = Status.getINSTANCE().getPlayer();
            if (player != null && player.getSquad() != null) {
                player.getSquad().getMembers().remove(member);
                JOptionPane.showMessageDialog(SquadMenu.this, "Member " + member.getUsername() + " has been kicked.");
                updateMySquadPanel();
            }
        }
    }

    private void updateUsernameDisplay() {
        Player player = Status.getINSTANCE().getPlayer();
        if (player != null && player.getUsername() != null) {
            usernameDisplayLabel.setText(player.getUsername());
        } else {
            usernameDisplayLabel.setText("Not set");
        }
    }

    private void centerPanel() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getPreferredSize().width) / 2;
        int y = (screenSize.height - getPreferredSize().height) / 2;
        setBounds(x, y, getPreferredSize().width, getPreferredSize().height);
    }
}

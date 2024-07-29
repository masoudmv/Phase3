package client.network.containers;

import client.network.RequestFactory;
import client.network.Status;
import shared.Model.Player;
import shared.Model.Squad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton opponentButton; // Declare the new button

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
        opponentButton = new JButton("Opponent"); // Initialize the new button

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

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(leaveSquadButton, gbc);

        gbc.gridy = 7;
        add(opponentButton, gbc); // Add the new button to the layout

        leaveSquadButton.setVisible(false);
        squadAbilitiesButton.setVisible(false);

        backButton.addActionListener(new BackAction());
        listSquadsButton.addActionListener(new ListSquadsAction());
        createSquadButton.addActionListener(new CreateSquadAction());
        leaveSquadButton.addActionListener(new LeaveSquadAction());
        squadAbilitiesButton.addActionListener(new SquadAbilitiesAction());
        opponentButton.addActionListener(new OpponentAction()); // Set up the action listener

        centerPanel();
        updateUsernameDisplay();
        updateSquadStatus();
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
            frame.switchToPanel(MainMenu.getINSTANCE());
            frame.repaint();
        }
    }

    private class ListSquadsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RequestFactory.createGetSquadListReq();
        }
    }

    private class CreateSquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RequestFactory.createCreateSquadReq();
        }
    }

    private class LeaveSquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RequestFactory.createLeaveSquadReq();
        }
    }

    private class SquadAbilitiesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame frame = MainFrame.getINSTANCE();
            frame.switchToPanel(new SquadAbilitiesMenu());
            frame.repaint();
        }
    }

    private class OpponentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Squad opponent = Status.getINSTANCE().getOpponent();
            MainFrame frame = MainFrame.getINSTANCE();
            Squad squad = Status.getINSTANCE().getPlayer().getSquad();
            if (squad == null){
                JOptionPane.showMessageDialog(frame, "You are not in any squad!");
                return;
            }

            if (opponent == null) {
                JOptionPane.showMessageDialog(frame, "Your squad is not in a battle yet!");
                return;
            }
            frame.switchToPanel(new OpponentPanel(opponent));
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
            RequestFactory.createKickPlayerReq(member.getMacAddress());
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

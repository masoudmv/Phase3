package client.network.containers;

import client.network.RequestFactory;
import shared.Model.Player;
import shared.Model.Squad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpponentPanel extends JPanel {
    private Timer updateTimer;
    private Squad opponentSquad;
    private JPanel contentPanel;
    private GridBagConstraints gbc;

    public OpponentPanel(Squad opponentSquad) {
        this.opponentSquad = opponentSquad;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 600));

        // Create and add the back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopUpdateTimer();
                MainFrame frame = MainFrame.getINSTANCE();
                frame.switchToPanel(new SquadMenu());
                frame.repaint();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);

        updateOpponentPanel();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        updateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOpponentPanel();
            }
        });
        updateTimer.start();
    }

    private void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }

    private void updateOpponentPanel() {
        contentPanel.removeAll();
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Opponent Squad");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(titleLabel, gbc);

        gbc.gridy++;

        if (opponentSquad != null && opponentSquad.getMembers() != null) {
            for (Player member : opponentSquad.getMembers()) {
                JLabel memberLabel = new JLabel(member.getUsername() + " - XP: " + member.getXP() + " - Status: " + member.getStatus());
                gbc.gridx = 0;
                contentPanel.add(memberLabel, gbc);

                JButton monomachiaButton = new JButton("Start Monomachia");
                monomachiaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Add your logic to handle the "Start Monomachia" action here
                        RequestFactory.createInitMonomachiaReq(member.getMacAddress());
                    }
                });

                gbc.gridx = 1;
                contentPanel.add(monomachiaButton, gbc);
                gbc.gridy++;
            }
        } else {
            JLabel noMembersLabel = new JLabel("No members in opponent squad.");
            contentPanel.add(noMembersLabel, gbc);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}

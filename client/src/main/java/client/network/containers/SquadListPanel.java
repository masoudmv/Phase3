package client.network.containers;

import client.network.RequestFactory;
import shared.model.Squad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SquadListPanel extends JPanel {
    public SquadListPanel(List<Squad> squads, MainFrame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 600));

        JPanel squadListPanel = new JPanel();
        squadListPanel.setLayout(new BoxLayout(squadListPanel, BoxLayout.Y_AXIS));

        for (Squad squad : squads) {
            JPanel squadPanel = new JPanel();
            squadPanel.setLayout(new BoxLayout(squadPanel, BoxLayout.Y_AXIS));
            squadPanel.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
            squadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the panel horizontally

            JLabel squadNameLabel = new JLabel("Squad Owner: " + squad.getOwner().getUsername());
            JLabel membersCountLabel = new JLabel("Number of Members: " + squad.getMembers().size());

            JButton joinButton = new JButton("Join Squad");
            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add your join squad logic here
                    RequestFactory.createJoinSquadReq(squad.getOwner().getMacAddress());
                }
            });

            squadPanel.add(squadNameLabel);
            squadPanel.add(membersCountLabel);
            squadPanel.add(joinButton);

            squadListPanel.add(squadPanel);
            squadListPanel.add(Box.createVerticalStrut(10));  // Add space between squads
        }

        JScrollPane scrollPane = new JScrollPane(squadListPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager.showSquadMenu();
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}

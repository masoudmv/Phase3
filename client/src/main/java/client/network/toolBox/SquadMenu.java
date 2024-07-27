package client.network.toolBox;

import client.network.Status;
import shared.Model.Player;
import shared.Model.Squad;
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
//    private JButton setUsernameButton;
    private JButton listSquadsButton;
    private JButton createSquadButton;
    private JButton mySquadButton;
    private JLabel squadStatusLabel;

    public SquadMenu() {
        setLayout(new GridBagLayout());
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setVisible(true);

        backButton = new JButton("Back");
        usernameLabel = new JLabel("Username:");
        usernameDisplayLabel = new JLabel("Not set"); // Display the username
//        setUsernameButton = new JButton("Set Username");
        listSquadsButton = new JButton("List Squads");
        createSquadButton = new JButton("Create Squad");
        mySquadButton = new JButton("My Squad");
        squadStatusLabel = new JLabel("Squad Status: Not in a squad");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

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

//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.gridwidth = 2;
//        gbc.anchor = GridBagConstraints.CENTER;
//        add(setUsernameButton, gbc);

        gbc.gridy = 2;
        add(listSquadsButton, gbc);

        gbc.gridy = 3;
        add(createSquadButton, gbc);

        gbc.gridy = 4;
        add(mySquadButton, gbc);

        gbc.gridy = 5;
        add(squadStatusLabel, gbc);

        backButton.addActionListener(new BackAction());
//        setUsernameButton.addActionListener(new SetUsernameAction());
        listSquadsButton.addActionListener(new ListSquadsAction());
        createSquadButton.addActionListener(new CreateSquadAction());
        mySquadButton.addActionListener(new MySquadAction());

        // Center the panel on the screen
        centerPanel();
        updateUsernameDisplay();
    }

    private class BackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Code to handle back button action
            // For example, you can switch to the previous panel or close this panel
            MainFrame frame = MainFrame.getINSTANCE();

//            SwingUtilities.invokeLater(() -> {
//            });

//            frame.remove(SquadMenu.this);

//            Menu.getINSTANCE();

            MainFrame.getINSTANCE().switchToPanel(Menu.getINSTANCE());
            frame.repaint();
        }
    }


    private class ListSquadsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                Status.getINSTANCE().getSocket().sendRequest(new GetSquadsListRequest()).run(Status.getINSTANCE().getResponseHandler()); // ewwwwwwww
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }
    }

    private class CreateSquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = Status.getINSTANCE().getPlayer();
            if (player != null) {
                Squad squad = new Squad(player);
                player.setSquad(squad);
                squadStatusLabel.setText("Squad Status: In a squad");
                JOptionPane.showMessageDialog(SquadMenu.this, "Squad created.");
            } else {
                JOptionPane.showMessageDialog(SquadMenu.this, "Please set a username first.");
            }
        }
    }

    private class MySquadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = Status.getINSTANCE().getPlayer();
            if (player != null && player.getSquad() != null) {
                JOptionPane.showMessageDialog(SquadMenu.this, "You are in a squad.");
            } else {
                JOptionPane.showMessageDialog(SquadMenu.this, "You are not in a squad.");
            }
        }
    }

    public void updateSquadStatus() {
        Player player = Status.getINSTANCE().getPlayer();
        if (player != null && player.getSquad() != null) {
            squadStatusLabel.setText("Squad Status: In a squad");
        } else {
            squadStatusLabel.setText("Squad Status: Not in a squad");
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

    public static String showInputDialog(Component parentComponent, String message) {
        JTextField textField = new JTextField(20);
        final JComponent[] inputs = new JComponent[] {
                new JLabel(message),
                textField
        };
        int result = JOptionPane.showOptionDialog(
                parentComponent,
                inputs,
                "Input",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"OK"},
                "OK");

        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }
}

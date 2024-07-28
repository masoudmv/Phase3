package client.network.toolBox;

import client.network.Status;
import client.network.socket.SocketRequestSender;
import shared.request.DonateRequest;
import shared.Model.Player;
import shared.Model.Squad;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SquadAbilitiesPanel extends JPanel {
    private JButton backButton;
    private JButton donateButton;
    private JButton ability1Button;
    private JButton ability2Button;
    private JButton ability3Button;
    private JLabel ability1Status;
    private JLabel ability2Status;
    private JLabel ability3Status;
    private JLabel squadVaultLabel;
    private Thread updateThread;
    private volatile boolean running;

    public SquadAbilitiesPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(600, 600));
        setSize(new Dimension(600, 600));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        backButton = new JButton("Back");
        add(backButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        squadVaultLabel = new JLabel("Squad Vault: " + getCurrentSquadVaultXP());
        add(squadVaultLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        donateButton = new JButton("Donate to Squad Vault");
        add(donateButton, gbc);

        ability1Button = new JButton("Call of Palioxis");
        ability2Button = new JButton("Call of Adonis");
        ability3Button = new JButton("Call of Gefjon");

        ability1Status = new JLabel("Not Purchased");
        ability2Status = new JLabel("Not Purchased");
        ability3Status = new JLabel("Not Purchased");

        gbc.gridy++;
        add(ability1Button, gbc);
        gbc.gridx++;
        add(ability1Status, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(ability2Button, gbc);
        gbc.gridx++;
        add(ability2Status, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(ability3Button, gbc);
        gbc.gridx++;
        add(ability3Status, gbc);

        backButton.addActionListener(e -> {
            running = false; // Stop the update thread
//            MainFrame frame = MainFrame.getINSTANCE();

            PanelManager.showSquadMenu();

//            frame.repaint();
        });

        donateButton.addActionListener(e -> openDonationPanel());

        setVisible(true);

        startUpdateThread();
    }

    private int getCurrentSquadVaultXP() {
        Player player = Status.getINSTANCE().getPlayer();
        Squad squad = player.getSquad();
        if (squad != null) {
            return squad.getVault();
        }
        return 0;
    }

    private void updateSquadVaultLabel() {
        squadVaultLabel.setText("Squad Vault: " + getCurrentSquadVaultXP());
    }

    private void openDonationPanel() {
        JDialog donationDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Donate to Squad Vault", Dialog.ModalityType.APPLICATION_MODAL);
        donationDialog.setLayout(new FlowLayout());
        donationDialog.setSize(300, 150);
        donationDialog.setResizable(false);
        donationDialog.setLocationRelativeTo(this);

        JTextField donationAmount = new JTextField(10);
        donationAmount.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField tf = (JTextField) input;
                try {
                    Integer.parseInt(tf.getText());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        donationDialog.add(new JLabel("Enter Donation Amount:"));
        donationDialog.add(donationAmount);
        donationDialog.add(okButton);
        donationDialog.add(cancelButton);

        okButton.addActionListener(e -> {
            if (donationAmount.getInputVerifier().verify(donationAmount)) {
                try {
                    int amount = Integer.parseInt(donationAmount.getText());
                    SocketRequestSender socket = Status.getINSTANCE().getSocket();
                    socket.sendRequest(new DonateRequest(Status.getINSTANCE().getPlayer().getMacAddress(), amount)).run(Status.getINSTANCE().getResponseHandler());
                    donationDialog.dispose();
                    updateSquadVaultLabel(); // Update the squad vault label after donation
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(donationDialog, "Failed to process donation due to network error.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(donationDialog, "Please enter a valid integer amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> donationDialog.dispose());

        donationDialog.setVisible(true);
    }

    private void startUpdateThread() {
        running = true;
        updateThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                    SwingUtilities.invokeLater(this::updateSquadVaultLabel); // Update label on the Event Dispatch Thread
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }
}

package client.network.containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EndGamePanel extends JPanel {
    private JLabel endGameLabel;
    private JButton returnButton;
    private JList<String> historyList;
    private DefaultListModel<String> historyListModel;
    private JScrollPane scrollPane;

    private int buttonHeight = 30;
    private int buttonWidth = 120;

    public EndGamePanel(String result, List<String> gameHistories) {
        Dimension dimension = new Dimension(600, 600);
        setPreferredSize(dimension);
        setVisible(true);

        int buttonX = dimension.width / 2 - buttonWidth / 2;
        int labelX = dimension.width / 2 - 150;

        endGameLabel = new JLabel(result, SwingConstants.CENTER);
        endGameLabel.setBounds(labelX, 50, 300, buttonHeight);
        endGameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        endGameLabel.setForeground(result.equals("Victory") ? Color.GREEN : Color.RED);

        returnButton = new JButton("Return to Main Menu");
        returnButton.setBounds(buttonX, 500, buttonWidth, buttonHeight);
        returnButton.addActionListener(new ReturnButtonAction());

        historyListModel = new DefaultListModel<>();
        for (String history : gameHistories) {
            historyListModel.addElement(history);
        }

        historyList = new JList<>(historyListModel);
        scrollPane = new JScrollPane(historyList);
        scrollPane.setBounds(labelX, 100, 300, 350);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(endGameLabel);
        add(returnButton);
        add(scrollPane);
        setLayout(null);
    }

    private class ReturnButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PanelManager.displayMainMenu();
        }
    }
}


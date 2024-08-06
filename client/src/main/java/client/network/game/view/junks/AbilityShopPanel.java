package client.network.game.view.junks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static client.network.RequestFactory.createBuyAbilityRequest;
import static client.network.RequestFactory.createPauseRequest;

public class AbilityShopPanel extends JPanel {
    private final JFrame frame;

    public AbilityShopPanel(JFrame frame) {
        this.frame = frame;

        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setOpaque(true); // Ensure the panel is opaque
        setPreferredSize(new Dimension(400, 300)); // Set a preferred size for the panel

        // Add key binding for escape key
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        actionMap.put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPauseRequest();
                frame.dispose();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the buttons
        gbc.anchor = GridBagConstraints.NORTH; // Align components to the top
        gbc.gridx = 0; // All buttons will be in the first column
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons stretch horizontally

        int buttonY = 0; // Start placing buttons from the first row
        for (Ability ability : Ability.values()) {
            JButton abilityButton = new JButton(ability.getName() + " - Cost: " + ability.getCost());
            abilityButton.addActionListener(e -> performAbilityAction(ability));
            gbc.gridy = buttonY++; // Increment row index for each button
            add(abilityButton, gbc);
        }
    }

    private void performAbilityAction(Ability ability) {
        createBuyAbilityRequest(ability.getName());
    }

    private void purchaseAbility(Ability ability) {
        // Implement ability purchase logic here
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure proper painting
    }
}

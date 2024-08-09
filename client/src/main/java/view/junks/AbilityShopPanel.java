package view.junks;

import controller.Game;
import controller.UserInputHandler;
import model.entities.Ability;
import model.entities.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AbilityShopPanel extends JPanel {
    private final JFrame frame;

    public AbilityShopPanel(JFrame frame) {
        this.frame = frame;

        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setOpaque(true); // Ensure the panel is opaque

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

        // Add key binding for escape key
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        actionMap.put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.getINSTANCE().getGameLoop().resumeGame();
                frame.dispose();
                UserInputHandler.abilityShopPanel = null;
                UserInputHandler.abilityShopFrame = null;
            }
        });
    }

    private void performAbilityAction(Ability ability) {
        if (Ability.activeAbility != null) {
            JOptionPane.showMessageDialog(frame, "You already have an active ability!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        purchaseAbility(ability);
    }

    private void purchaseAbility(Ability ability) {
        if (ability == Ability.SLAUGHTER){
            double now = Game.elapsedTime;
            double initiation = Profile.getCurrent().slaughterInitiationTime;
            if (now - initiation < 120) {
                JOptionPane.showMessageDialog(frame, "You should wait at least 120 seconds after your previous slaughter!");
                return;
            }
        }
        int currentXP = Profile.getCurrent().inGameXP;
        if (currentXP >= ability.getCost()) {
            Profile.getCurrent().inGameXP -= ability.getCost();
            Ability.activeAbility = ability;
            if (ability == Ability.SLAUGHTER) Profile.getCurrent().slaughterInitiationTime = Game.elapsedTime;
            JOptionPane.showMessageDialog(frame, ability.getName() + " was successfully activated");
        } else {
            JOptionPane.showMessageDialog(frame, "You don't have enough XP!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

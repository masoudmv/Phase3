package view;

import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.gray;

public class KeyBindingMenu extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private static KeyBindingMenu INSTANCE;
    private JLabel backLabel = new JLabel("Back", SwingConstants.CENTER);
    private Point backButton = new Point(22, 22);

    private Map<String, JLabel> actionLabels = new HashMap<>();
    private Map<String, Integer> keyBindings = new HashMap<>();

    private Map<String, JLabel> keyLabels = new HashMap<>();

    private int labelOffset = 50;
    private int currentYPosition = 100;

    private int width = 120;
    private int height = 35;

    private JLabel warning = new JLabel();

    public KeyBindingMenu() {
        INSTANCE = this;
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);

        // Adding labels and sliders
        add(backLabel);
        backLabel.setBounds(backButton.x + 20, backButton.y, 75, height);
        backLabel.setBackground(gray);
        backLabel.setOpaque(true);
        backLabel.setLayout(new GridBagLayout());

        backLabel.addMouseListener(this);



        warning = new JLabel();
        warning.setBounds(backButton.x + 20, 500, 500, height);
        warning.setBackground(Color.GRAY);
        warning.setOpaque(true);
        add(warning);


        // Adding action labels
        addActionLabel("Move Left", KeyEvent.VK_A);
        addActionLabel("Move Right", KeyEvent.VK_D);
        addActionLabel("Move Down", KeyEvent.VK_S);
        addActionLabel("Move Up", KeyEvent.VK_W);
        addActionLabel("Open Shop", KeyEvent.VK_SPACE);
        addActionLabel("Activate Skill Tree Ability", KeyEvent.VK_E);
        addActionLabel("Activate Shop Ability", KeyEvent.VK_R);



        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

    }

    private void addActionLabel(String action, int defaultKey) {
        JLabel label = new JLabel(action, SwingConstants.CENTER);
        label.setBounds(backButton.x + 20, currentYPosition, 150, height);
        label.setBackground(Color.GRAY);
        label.setOpaque(true);
        actionLabels.put(action, label);
        add(label);

        // Adding a default key binding
        keyBindings.put(action, defaultKey);

        // Creating and adding the key label
        JLabel keyLabel = new JLabel(KeyEvent.getKeyText(defaultKey), SwingConstants.CENTER);
        keyLabel.setBounds(backButton.x + 300, currentYPosition, 150, height);
        keyLabel.setBackground(gray);
        keyLabel.setOpaque(true);
        keyLabels.put(action, keyLabel);
        add(keyLabel);

        // Adding mouse listener to key label
        keyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                keyLabel.setText("?");
                keyBindings.replace(action, 191);

                // Remove previous temporary key listener if exists
                removeKeyListener(KeyBindingMenu.this);

                // Add temporary key listener to capture next key press
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();

                        if (keyCode == 27){
                            System.out.println("canceled");
                        }


                        else if (keyBindings.values().contains(keyCode)){
                            warning.setText("Your Input Key Is Already In Use!");
                            warning.setHorizontalAlignment(SwingConstants.CENTER);
                        }
                        else {
                            // Update key binding and key label text
                            keyBindings.replace(action, keyCode);
                            keyLabel.setText(KeyEvent.getKeyText(keyCode));
                            warning.setText("Key Binding Was Successfully Updated!");
                            warning.setHorizontalAlignment(SwingConstants.CENTER);

                            // Remove temporary key listener
                            removeKeyListener(this);
                        }

                    }
                });

                // Request focus to receive key events
                requestFocus();
            }
        });



        currentYPosition += labelOffset;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Main.background, 0, 0, null);


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == backLabel) {
            removeKeyBindingMenu();
            new SettingsMenu();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Map.Entry<String, JLabel> entry : actionLabels.entrySet()) {
            if (entry.getValue().isFocusOwner()) {
                keyBindings.put(entry.getKey(), e.getKeyCode());
                entry.getValue().setText(KeyEvent.getKeyText(e.getKeyCode()));

                // Update key label
                JLabel keyLabel = keyLabels.get(entry.getKey());
                keyLabel.setText(KeyEvent.getKeyText(e.getKeyCode()));

                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    private void removeKeyBindingMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        frame.remove(this);
        frame.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    public Map<String, Integer> getKeyBindings() {
        return keyBindings;
    }
    public void addKeyBindingsMenuToFrame(){
        MainFrame frame = MainFrame.getINSTANCE();
        frame.add(this);
        frame.repaint();
    }

    public static KeyBindingMenu getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new KeyBindingMenu();
        return INSTANCE;
    }
}

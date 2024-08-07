package client.network.containers;

import java.util.List;

public class PanelManager {

    public static void showSquadMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        SquadMenu squadMenu = new SquadMenu();
        frame.switchToPanel(squadMenu);
        squadMenu.updateSquadStatus(); // needed really???
    }

    public static void displayMainMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        MainMenu mainMenu = MainMenu.getINSTANCE();
        mainMenu.updateStatusLabel();
        frame.switchToPanel(mainMenu);
    }

    public static void showEndGamePanel(String result, List<String> gameHistories) {
        MainFrame frame = MainFrame.getINSTANCE();
        EndGamePanel endGamePanel = new EndGamePanel(result, gameHistories);
        frame.switchToPanel(endGamePanel);
    }
}

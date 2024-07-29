package client.network.containers;

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
}

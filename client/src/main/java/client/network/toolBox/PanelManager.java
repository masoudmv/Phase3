package client.network.toolBox;

public class PanelManager {


    public static void showSquadMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        SquadMenu squadMenu = new SquadMenu();
        frame.switchToPanel(squadMenu);
        squadMenu.updateSquadStatus();
    }

    public static void displayMainMenu() {
        MainFrame frame = MainFrame.getINSTANCE();
        Menu menu = Menu.getINSTANCE();
        menu.updateStatusLabel();
        frame.switchToPanel(menu);
    }
}

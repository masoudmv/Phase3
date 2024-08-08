package shared;

import shared.hibernate.HibernateUtil;
import shared.hibernate.PlayerDAO;
import shared.hibernate.SquadDAO;
import shared.model.Player;
import shared.model.Squad;
import shared.model.Status;

public class Main {
    public static void main(String[] args) {
        PlayerDAO playerDAO = new PlayerDAO();
        SquadDAO squadDAO = new SquadDAO();

        // Clear existing data (for testing purposes, comment this out if not needed)
        playerDAO.deleteAllPlayers();

        // Create and save players with unique mac addresses
        Player owner = new Player();
        owner.setMacAddress("00:1A:2B:3C:4D:5E:1");  // Unique mac address
        owner.setUsername("player1");
        owner.setXP(1000);
        owner.setStatus(Status.online);
        playerDAO.savePlayer(owner);

        // Create a squad with the first player as the owner
        Squad squad = new Squad(owner);
//        squad.setName("MainSquad");
        squadDAO.saveSquad(squad);

        // Add the remaining players to the squad
        for (int i = 2; i <= 5; i++) {
            Player player = new Player();
            player.setMacAddress("00:1A:2B:3C:4D:5E:" + i);  // Ensure unique mac address
            player.setUsername("player" + i);
            player.setXP(1000 * i);
            player.setStatus(Status.online);
            player.setSquad(squad);
            playerDAO.savePlayer(player);
            squad.addMember(player);
        }

        // Update the squad with all members
        squadDAO.updateSquad(squad);

        // Retrieve and print the squad details
        System.out.println("\nSquad Details:");
        Squad mainSquad = squadDAO.getSquadById(owner.getSquad().getId());
        if (mainSquad != null) {
            System.out.println("Squad ID: " + mainSquad.getId() + ", Owner: " + mainSquad.getOwner().getUsername());
            System.out.println("Members:");
            for (Player member : mainSquad.getMembers()) {
                System.out.println("Player ID: " + member.getId() + ", Username: " + member.getUsername());
            }
        }

        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }
}

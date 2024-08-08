package server.cli;

import picocli.CommandLine;
import server.database.DataBase;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "servercli",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "CLI for server operations",
        subcommands = {
                ServerCli.BattleCommand.class,
                ServerCli.GameConstantsCommand.class
        })
public class ServerCli implements Callable<Integer> {

    @CommandLine.Option(names = {"-i", "--initiateSquadBattle"}, description = "Initiates a squad battle")
    private boolean initiateSquadBattle;

    @CommandLine.Option(names = {"-t", "--terminateSquadBattle"}, description = "Terminates a squad battle")
    private boolean terminateSquadBattle;

    private final DataBase dataBase;

    public ServerCli(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Integer call() throws Exception {
        if (initiateSquadBattle) {
            initiateSquadBattle();
        }
        if (terminateSquadBattle) {
            terminateSquadBattle();
        }
        return 0;
    }

    private void initiateSquadBattle() {
        // Add logic to initiate a squad battle
        synchronized (dataBase){
            dataBase.initiateSquadBattle();
        }
        System.out.println("Initiating squad battle...");
    }

    private void terminateSquadBattle() {
        // Add logic to terminate a squad battle
        System.out.println("Terminating squad battle...");
    }

    @CommandLine.Command(
            name = "battle",
            description = "Commands related to battles",
            subcommands = {
                    InitiateSquadBattleCommand.class,
                    TerminateSquadBattleCommand.class
            })
    static class BattleCommand implements Runnable {
        @Override
        public void run() {
            CommandLine.usage(this, System.out);
        }
    }

    @CommandLine.Command(name = "initiate", description = "Initiates a squad battle")
    static class InitiateSquadBattleCommand implements Runnable {
        @Override
        public void run() {
            System.out.println("Initiating squad battle...");
        }
    }

    @CommandLine.Command(name = "terminate", description = "Terminates a squad battle")
    static class TerminateSquadBattleCommand implements Runnable {
        @Override
        public void run() {
            System.out.println("Terminating squad battle...");
        }
    }

    @CommandLine.Command(
            name = "game-constants",
            description = "Commands to change game constants",
            subcommands = {
                    IncreasePaceCommand.class,
                    DecreasePaceCommand.class,
                    ChangeEpsilonAccelerationCommand.class,
                    ChangeEpsilonMaxSpeedCommand.class,
                    ChangeEnemiesMaxSpeedCommand.class,
                    ChangeEnemiesAccelerationCommand.class
            })
    static class GameConstantsCommand implements Runnable {
        @Override
        public void run() {
            CommandLine.usage(this, System.out);
        }
    }

    @CommandLine.Command(name = "increase-pace", description = "Increase the pace of the game")
    static class IncreasePaceCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to increase pace
            System.out.println("Increasing pace...");
        }
    }

    @CommandLine.Command(name = "decrease-pace", description = "Decrease the pace of the game")
    static class DecreasePaceCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to decrease pace
            System.out.println("Decreasing pace...");
        }
    }

    @CommandLine.Command(name = "change-epsilon-acceleration", description = "Change the acceleration of Epsilon")
    static class ChangeEpsilonAccelerationCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to change Epsilon's acceleration
            System.out.println("Changing Epsilon's acceleration...");
        }
    }

    @CommandLine.Command(name = "change-epsilon-max-speed", description = "Change the max speed of Epsilon")
    static class ChangeEpsilonMaxSpeedCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to change Epsilon's max speed
            System.out.println("Changing Epsilon's max speed...");
        }
    }

    @CommandLine.Command(name = "change-enemies-max-speed", description = "Change the max speed of enemies")
    static class ChangeEnemiesMaxSpeedCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to change enemies' max speed
            System.out.println("Changing enemies' max speed...");
        }
    }

    @CommandLine.Command(name = "change-enemies-acceleration", description = "Change the acceleration of enemies")
    static class ChangeEnemiesAccelerationCommand implements Runnable {
        @Override
        public void run() {
            // Add logic to change enemies' acceleration
            System.out.println("Changing enemies' acceleration...");
        }
    }
}

package game.controller;

import shared.constants.Constants;
import game.model.charactersModel.EpsilonModel;


public class Game {
    private static Game INSTANCE;
    public static int inGameXP = 1000;
    public static double ELAPSED_TIME = 0;
    public static int wave;
    private boolean isPaused = false;
    private EpsilonModel epsilon;


    private static GameLoop gameLoop;




    public Game (){
        INSTANCE = this;
        ELAPSED_TIME =0;
        inGameXP=0;
        wave=1;
        Constants.RADIUS = 15;


        new GameLoop();


    }

    public static Game getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Game();
        return INSTANCE;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    public void openShop(){}
    public void closeShop(){}
    public int getInGameXp() {
        return inGameXP;
    }

    public void sumInGameXpWith(int xp){
        this.inGameXP += xp;
    }


    public static void nullifyGameInstance() {
        INSTANCE = null;
//        MainFrame.getINSTANCE().removeKeyListener(gameLoop);
// todo: now the gameLoop doesnt contain KeyLister
        gameLoop =null;
    }
}

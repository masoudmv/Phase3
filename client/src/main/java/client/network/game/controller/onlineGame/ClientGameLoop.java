package client.network.game.controller.onlineGame;

import client.network.game.controller.UserInputHandler;
import client.network.game.view.MainFrame;
import java.util.concurrent.atomic.AtomicBoolean;

import static client.network.RequestFactory.createStateRequest;


public class ClientGameLoop implements Runnable{

    public static ClientGameLoop INSTANCE;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private long lastUpdateTime;
    private long lastTickTime = System.currentTimeMillis();
    private int frameCount = 0;
    public static boolean movementInProgress = false;


    public ClientGameLoop() {
        movementInProgress = false;
        MainFrame frame = MainFrame.getINSTANCE();
        INSTANCE = this;
        this.start();
    }



    public void start() {
        if (running.get()) return;
        running.set(true);
        new Thread(this).start();
    }

    public void stop() {
        if (!running.get()) return;
        running.set(false);
        exit.set(true);
    }





    public void updateView() {

        long currentTickTime = System.currentTimeMillis();
        long interval = currentTickTime - lastTickTime;

        lastTickTime = currentTickTime;

        UserInputHandler.updateMovement();


        // Increment frame count every time updateView is called
        frameCount++;
        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Check if one second has passed
        if (currentTime - lastUpdateTime >= 1000) {
            // Reset frame counter and last update time for the next second
            frameCount = 0;
            lastUpdateTime = currentTime;
        }

        createStateRequest();
    }



    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeF = (double) 1000000000 / 70;
        double deltaF = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (!exit.get()) {
            long currentTime = System.nanoTime();
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaF >= 1) {
                updateView();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println(String.format("FPS: %s", frames));
                frames = 0;
                timer += 1000;
            }
        }
    }


    public static ClientGameLoop getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new ClientGameLoop();
        return INSTANCE;
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isOn() {
        return !exit.get();
    }


}











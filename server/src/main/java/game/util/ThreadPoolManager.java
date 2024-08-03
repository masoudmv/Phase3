package game.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private ScheduledExecutorService scheduler;

    // Singleton pattern to ensure a single instance of ThreadPoolManager
    private static ThreadPoolManager instance;

    private ThreadPoolManager() {
        // Initialize the scheduler with a fixed number of threads
        scheduler = Executors.newScheduledThreadPool(10); // Adjust the number of threads as needed
    }

    public static synchronized ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }

    public void scheduleTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public void submitTask(Runnable task) {
        scheduler.submit(task);
    }

    public void shutdown() {
        try {
            scheduler.shutdown();
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}

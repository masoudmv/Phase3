package game.model.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class WaveManager implements Runnable {
    private static final int INITIAL_DELAY = 15000; // 12 seconds
    private static final int MIN_DELAY = 10000; // minimum delay of 1 second
    private static final int ENEMIES_TO_ELIMINATE_PER_WAVE = 2; // Example threshold
    private List<Class<? extends Enemy>> enemyClasses;
    private AtomicInteger enemiesEliminated; // Use AtomicInteger for thread safety
    private AtomicInteger enemiesGenerated; // Track generated enemies per wave
    private String gameID;
    private int numberOfWaves;
    private int waveIndex;


    private final AtomicBoolean running = new AtomicBoolean(true); // To control the running state
    private final Object pauseLock = new Object(); // Lock object for pausing

    public WaveManager(String gameID, int numberOfWaves) {
        this.enemyClasses = new ArrayList<>();
        this.enemiesEliminated = new AtomicInteger(0);
        this.enemiesGenerated = new AtomicInteger(0);
        this.gameID = gameID;
        this.numberOfWaves = numberOfWaves;
        discoverEnemies();
    }

    private void discoverEnemies() {
        Reflections reflections = new Reflections("game.model.charactersModel");
        Set<Class<? extends Enemy>> classes = reflections.getSubTypesOf(Enemy.class);
        this.enemyClasses.addAll(classes.stream()
                .filter(cls -> !Modifier.isAbstract(cls.getModifiers()))
                .collect(Collectors.toList()));
    }

    @Override
    public void run() {
        try {
            generateWaves(gameID, numberOfWaves);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void generateWaves(String gameID, int numberOfWaves) throws InterruptedException {
        for (int wave = 1; wave <= numberOfWaves; wave++) {
            synchronized (pauseLock) {
                while (!running.get()) {
                    pauseLock.wait(); // Wait until notified
                }
            }
            waveIndex = wave;
            System.out.println("Starting wave " + wave);
            generateWave(gameID, wave);
            waitForWaveCompletion();
            System.out.println("Wave " + wave + " completed.");
            waitForAllEnemiesToBeEliminated();
            System.out.println("All enemies for wave " + wave + " eliminated.");
        }

        waveIndex ++;
    }

    public void pause() {
        running.set(false);
    }

    public void resume() {
        synchronized (pauseLock) {
            running.set(true);
            pauseLock.notifyAll();
        }
    }

    private void generateWave(String gameID, int waveNumber) throws InterruptedException {
        int delay = INITIAL_DELAY;
        List<Class<? extends Enemy>> generatedEnemies = new ArrayList<>();
        enemiesGenerated.set(0);

        while (enemiesEliminated.get() < ENEMIES_TO_ELIMINATE_PER_WAVE) {
            synchronized (pauseLock) {
                while (!running.get()) {

                    pauseLock.wait(); // Wait until notified to resume
                }
            }

            boolean enemyGenerated = false;

            for (Class<? extends Enemy> enemyClass : enemyClasses) {
                try {
                    // Create an instance of the enemy class to check its properties
                    Enemy enemy = createEnemyInstance(enemyClass);

                    if (enemy != null && enemy.getMinSpawnWave() <= waveNumber) {
                        if (!isAlreadyGenerated(generatedEnemies, enemyClass, enemy)) {
                            enemy.create(gameID);
                            enemiesGenerated.incrementAndGet();
                            enemyGenerated = true;
                            if (enemy.isUniquePerWave() || enemy.isUniquePerGame()) {
                                generatedEnemies.add(enemyClass);
                            }
                            System.out.println("Generated " + enemyClass.getSimpleName() + " for wave " + waveNumber);

                            Thread.sleep(delay); // Simulating delay between enemy generations
                            delay = Math.max(MIN_DELAY, delay - 100); // Decrease delay dynamically
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error creating instance of " + enemyClass.getSimpleName() + ": " + e.getMessage());
                }
            }

            if (!enemyGenerated) {
                break;
            }
        }

        System.out.println("Total enemies generated in wave " + waveNumber + ": " + enemiesGenerated.get());
    }

    private Enemy createEnemyInstance(Class<? extends Enemy> enemyClass) {
        try {
            Constructor<? extends Enemy> constructor = enemyClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isAlreadyGenerated(List<Class<? extends Enemy>> generatedEnemies, Class<? extends Enemy> enemyClass, Enemy enemy) {
        if (enemy.isUniquePerGame() && generatedEnemies.stream().anyMatch(e -> e.equals(enemyClass))) {
            return true;
        }

        if (enemy.isUniquePerWave() && generatedEnemies.stream().anyMatch(e -> e.equals(enemyClass))) {
            return true;
        }

        return false;
    }

    private void waitForWaveCompletion() throws InterruptedException {
        while (enemiesEliminated.get() < ENEMIES_TO_ELIMINATE_PER_WAVE) {
            System.out.println("Waiting for wave completion: " + enemiesEliminated.get() + "/" + ENEMIES_TO_ELIMINATE_PER_WAVE);
            Thread.sleep(1000); // Check every second
        }
    }

    private void waitForAllEnemiesToBeEliminated() throws InterruptedException {
        while (enemiesEliminated.get() < enemiesGenerated.get()) {
            System.out.println("Waiting for all enemies to be eliminated: " + enemiesEliminated.get() + "/" + enemiesGenerated.get());
            Thread.sleep(1000); // Check every second
        }
        enemiesEliminated.set(0); // Reset for the next wave
    }

    // This method should be called by the game logic when an enemy is eliminated
    public void onEnemyEliminated() {
        enemiesEliminated.incrementAndGet();
        System.out.println("Enemy eliminated. Total eliminated: " + enemiesEliminated.get());
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public int getNumberOfWaves() {
        return numberOfWaves;
    }
}

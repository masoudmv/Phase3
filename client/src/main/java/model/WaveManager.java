package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import model.interfaces.Enemy;
import org.reflections.Reflections;

public class WaveManager implements Runnable {
    private static final int INITIAL_DELAY = 13000; // 5 seconds
    private static final int MIN_DELAY = 10000; // minimum delay of 1 second
    private static final int ENEMIES_TO_ELIMINATE_PER_WAVE = 2; // Example threshold
    private List<Class<? extends Enemy>> enemyClasses;
    private AtomicInteger enemiesEliminated; // Use AtomicInteger for thread safety
    private AtomicInteger enemiesGenerated; // Track generated enemies per wave

    public WaveManager() {
        this.enemyClasses = new ArrayList<>();
        this.enemiesEliminated = new AtomicInteger(0);
        this.enemiesGenerated = new AtomicInteger(0);
        discoverEnemies();
    }

    private void discoverEnemies() {
        Reflections reflections = new Reflections("model.charactersModel");
        Set<Class<? extends Enemy>> classes = reflections.getSubTypesOf(Enemy.class);
        this.enemyClasses.addAll(classes.stream()
                .filter(cls -> !Modifier.isAbstract(cls.getModifiers()))
                .collect(Collectors.toList()));
    }

    @Override
    public void run() {
        generateWaves();
    }

    private void generateWaves() {
        for (int wave = 1; wave <= 6; wave++) {
            System.out.println("Starting wave " + wave);
            generateWave(wave);
            waitForWaveCompletion();
            System.out.println("Wave " + wave + " completed.");
            waitForAllEnemiesToBeEliminated();
            System.out.println("All enemies for wave " + wave + " eliminated.");
        }
    }

    private void generateWave(int waveNumber) {
        int delay = INITIAL_DELAY;
        List<Class<? extends Enemy>> generatedEnemies = new ArrayList<>();
        enemiesGenerated.set(0);

        while (enemiesEliminated.get() < ENEMIES_TO_ELIMINATE_PER_WAVE) {
            boolean enemyGenerated = false;

            for (Class<? extends Enemy> enemyClass : enemyClasses) {
                try {
                    // Create an instance of the enemy class to check its properties
                    Enemy enemy = createEnemyInstance(enemyClass);

                    if (enemy != null && enemy.getMinSpawnWave() <= waveNumber) {
                        System.out.println("wave;  "+ waveNumber);
                        if (!isAlreadyGenerated(generatedEnemies, enemyClass, enemy)) {
                            enemy.create();
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

    private void waitForWaveCompletion() {
        while (enemiesEliminated.get() < ENEMIES_TO_ELIMINATE_PER_WAVE) {
            try {
                System.out.println("Waiting for wave completion: " + enemiesEliminated.get() + "/" + ENEMIES_TO_ELIMINATE_PER_WAVE);
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForAllEnemiesToBeEliminated() {
        while (enemiesEliminated.get() < enemiesGenerated.get()) {
            try {
                System.out.println("Waiting for all enemies to be eliminated: " + enemiesEliminated.get() + "/" + enemiesGenerated.get());
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        enemiesEliminated.set(0); // Reset for the next wave
    }

    // This method should be called by the game logic when an enemy is eliminated
    public void onEnemyEliminated() {
        enemiesEliminated.incrementAndGet();
        System.out.println("Enemy eliminated. Total eliminated: " + enemiesEliminated.get());
    }

    public void incrementGeneratedEnemies(){
        enemiesGenerated.incrementAndGet();
        enemiesGenerated.incrementAndGet();
        enemiesGenerated.incrementAndGet();
        enemiesGenerated.incrementAndGet();
    }
}

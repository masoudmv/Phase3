package model.interfaces;

public interface Enemy {

    void create();
    int getMinSpawnWave();
    default boolean isUniquePerWave(){ return false; };
    default boolean isUniquePerGame(){ return false; }
}
package game.model.reflection;

public interface Enemy {

    void create(String gameID);
    int getMinSpawnWave();
    default boolean isUniquePerWave(){ return false; };
    default boolean isUniquePerGame(){ return false; }
}

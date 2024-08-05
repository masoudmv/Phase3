package game.model.entities;

import game.controller.Game;
import game.model.charactersModel.GeoShapeModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static shared.constants.Constants.AOE_COOLDOWN;

public abstract class Entity {
    public int health;
    public int fullHealth;
    public boolean vulnerable = true;
    public ConcurrentHashMap<AttackTypes, Integer> damageSize = new ConcurrentHashMap<>();
//    public static CopyOnWriteArrayList<GeoShapeModel> entities = new CopyOnWriteArrayList<>();
    private double lastAoeTime = 0;

    public void damage(Entity entity, AttackTypes attackType) {
        // todo
        // implement melampus ...
//        double now = Game.ELAPSED_TIME;
//        if (now - lastAoeTime >= AOE_COOLDOWN) {
            if (entity.vulnerable) {
                entity.health -= damageSize.get(attackType);
                if (entity.health <= 0) {
                    entity.eliminate();
                }
            }
//            lastAoeTime = now;
//        }
    }

    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

    public abstract void eliminate();

}

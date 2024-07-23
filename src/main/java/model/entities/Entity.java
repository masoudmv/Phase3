package model.entities;

import controller.Game;
import model.charactersModel.GeoShapeModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.Constants.AOE_COOLDOWN;

public abstract class Entity {
    protected int health = 100;
    public int fullHealth;
    public boolean vulnerable = true;
    public ConcurrentHashMap<AttackTypes, Integer> damageSize = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<GeoShapeModel> entities = new CopyOnWriteArrayList<>();
    private double lastAoeTime = 0;

    public void damage(Entity entity, AttackTypes attackType) {
        double now = Game.ELAPSED_TIME;
        if (now - lastAoeTime >= AOE_COOLDOWN) {
            if (entity.vulnerable) {
//                entity.health -= damageSize.get(attackType);
                if (entity.health <= 0) {
                    entity.eliminate();
                    // if (entity instanceof CollectibleModel) playXPSoundEffect();
                    // else playDownSoundEffect();
                }   // else playHitSoundEffect();
            }
            lastAoeTime = now;
        }
    }

    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

    public abstract void eliminate();

}

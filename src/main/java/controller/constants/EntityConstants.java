package controller.constants;

import model.Profile;

public enum EntityConstants {
    EPSILON_HEALTH, SHOTS_PER_SECOND, SKILL_COOLDOWN_IN_SECONDS, COLLECTIBLE_LIFE_TIME, EPSILON_RAPID_SHOOTING_DELAY, EPSILON_SHOOTING_RAPIDITY,
    TRIGORATH_HEALTH, TRIGORATH_MELEE_DAMAGE, SQUARANTINE_HEALTH, SQUARANTINE_MELEE_DAMAGE, BULLET_HEALTH, COLLECTIBLE_HEALTH;
    public static final double ARCHMIRE_SPEED = 1;
    public static final double OMENOCT_NORMAL_SPEED = 1;
    public static final double OMENOCT_PANEL_SPEED = 2;
    public static final double NECROPICK_MIN_RADIUS = 100;
    public static final double NECROPICK_MAX_RADIUS = 200;

    public static final int HOVER_DURATION = 4; // 4 seconds in milliseconds
    public static final int NON_HOVER_DURATION = 4; // 8 seconds in milliseconds
    public static final int OMENOCT_SHOT_DELAY = 1; // 8 seconds in milliseconds
    public static final int ORB_PANEL_CREATION_DELAY = 1; // 8 seconds in milliseconds




    public int getValue() {
        return switch (this) {
            case EPSILON_SHOOTING_RAPIDITY -> Profile.getCurrent().EPSILON_SHOOTING_RAPIDITY;
            case EPSILON_HEALTH -> 100;
            case SHOTS_PER_SECOND -> 2;
            case SKILL_COOLDOWN_IN_SECONDS -> 300;
            case EPSILON_RAPID_SHOOTING_DELAY -> 50;
            case TRIGORATH_HEALTH -> 15;
            case TRIGORATH_MELEE_DAMAGE -> 10;
            case SQUARANTINE_HEALTH -> 10;
            case SQUARANTINE_MELEE_DAMAGE -> 6;
            case BULLET_HEALTH, COLLECTIBLE_HEALTH -> 0;
            case COLLECTIBLE_LIFE_TIME -> 8;
        };
    }




}

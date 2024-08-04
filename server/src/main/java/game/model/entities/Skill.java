package game.model.entities;

import game.controller.Game;
import game.controller.UserInterfaceController;
import shared.constants.EntityConstants;
import shared.constants.SkillConstants;
import game.model.charactersModel.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Skill {
    ARES, ASTRAPE, CERBERUS,
    ACESO, MELAMPUS, CHIRON, ATHENA,
    PROTEUS, EMPUSA, DOLUS;

    public static Skill activeSkill = null;
    public boolean acquired = true;
    public double lastSkillTime = -Double.MAX_VALUE;

    public static void initializeSkills() {
        activeSkill = ASTRAPE;
        CopyOnWriteArrayList<Skill> acquiredSkillSave = new CopyOnWriteArrayList<>();
        for (String skillName : Profile.getCurrent().acquiredSkillsNames) acquiredSkillSave.add(findSkill(skillName));
        for (Skill skill : acquiredSkillSave) skill.acquired = true;
    }

    public String getName() {
        return "WRIT OF " + name();
    }

    public int getCost() {
        return switch (this) {
            case ARES, MELAMPUS, EMPUSA -> 750;
            case ATHENA -> 1200;
            case ASTRAPE, PROTEUS -> 1000;
            case CERBERUS, DOLUS -> 1500;
            case ACESO -> 500;
            case CHIRON -> 900;
        };
    }

    public SkillType getType() {
        return switch (this) {
            case ARES, ASTRAPE, CERBERUS -> SkillType.ATTACK;
            case ACESO, MELAMPUS, CHIRON, ATHENA -> SkillType.GUARD;
            case PROTEUS, EMPUSA, DOLUS -> SkillType.POLYMORPHIA;
        };
    }

    public ActionListener getAction() {
        return switch (this) {
            case ARES -> e -> {

                Profile.getCurrent().EPSILON_MELEE_DAMAGE += (int) SkillConstants.WRIT_OF_ARES_BUFF_AMOUNT.getValue();
                Profile.getCurrent().EPSILON_RANGED_DAMAGE += (int) SkillConstants.WRIT_OF_ARES_BUFF_AMOUNT.getValue();
            };
            case ASTRAPE -> e -> EpsilonModel.getINSTANCE().damageSize.put(AttackTypes.ASTRAPE, 2);
            case CERBERUS -> e -> EpsilonModel.getINSTANCE().cerebrus();
            case ACESO -> e -> {
                Timer healthTimer = new Timer((int) SkillConstants.WRIT_OF_ACESO_HEALING_FREQUENCY.getValue(), null);
                healthTimer.addActionListener(e1 -> {
                    if (UserInterfaceController.isGameRunning()) EpsilonModel.getINSTANCE().addHealth((int) SkillConstants.WRIT_OF_ACESO_HEALING_AMOUNT.getValue());
                    if (!UserInterfaceController.isGameOn()) healthTimer.stop();
                });
                healthTimer.start();
            };
            case MELAMPUS -> e -> Profile.getCurrent().EPSILON_VULNERABILITY_PROBABILITY = 95;
            case CHIRON -> e -> Profile.getCurrent().EPSILON_HEALTH_REGAIN = 3;
            case ATHENA -> e -> Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT *= 0.80;
            case PROTEUS -> e -> EpsilonModel.getINSTANCE().addVertex();
            case EMPUSA -> e -> System.out.println();
            case DOLUS -> null;
        };
    }

    public void fire() {
        double now = Game.ELAPSED_TIME;
        if (now - lastSkillTime >= EntityConstants.SKILL_COOLDOWN_IN_SECONDS.getValue()) {
            ActionListener action = getAction();
            if (action != null) {
                action.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            } else {
                System.out.println("No action defined for this skill");
            }
            lastSkillTime = now;
        } else {
            System.out.println("skill is on cooldown, cannot fire");
        }
    }

    public enum SkillType {
        ATTACK, GUARD, POLYMORPHIA
    }

    // Add a method to find a skill by its name
    public static Skill findSkill(String name) {
        for (Skill skill : values()) {
            if (skill.name().equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }
}

package model.entities;

import controller.Game;
import model.Profile;
//import model.characters.EpsilonModel;
import model.charactersModel.EpsilonModel;
import model.collision.Impactable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static controller.Game.*;
import static controller.UserInterfaceController.*;
import static controller.constants.SkillConstants.*;
import static controller.constants.EntityConstants.SKILL_COOLDOWN_IN_SECONDS;
import static model.collision.Impactable.impactables;

public enum Skill {
    ARES, ASTRAPE, CERBERUS,
    ACESO, MELAMPUS, CHIRON,
    PROTEUS, EMPUSA, DOLUS;

    public static Skill activeSkill = null;
    public boolean acquired = false;
    public double lastSkillTime = 0;

    public static void initializeSkills() {
        activeSkill = findSkill(Profile.getCurrent().activeSkillSaveName);
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
            case ASTRAPE, PROTEUS -> 1000;
            case CERBERUS, DOLUS -> 1500;
            case ACESO -> 500;
            case CHIRON -> 900;
        };
    }

    public SkillType getType() {
        return switch (this) {

            case ARES, ASTRAPE, CERBERUS -> SkillType.ATTACK;
            case ACESO, MELAMPUS, CHIRON -> SkillType.GUARD;
            case PROTEUS, EMPUSA, DOLUS -> SkillType.POLYMORPHIA;
        };
    }

    public ActionListener getAction() {
        return switch (this) {

            case ARES -> e -> {
                Profile.getCurrent().EPSILON_MELEE_DAMAGE += WRIT_OF_ARES_BUFF_AMOUNT.getValue();
                Profile.getCurrent().EPSILON_RANGED_DAMAGE += WRIT_OF_ARES_BUFF_AMOUNT.getValue();
            };
            case ASTRAPE -> null;
            case CERBERUS -> null;
            case ACESO -> e -> {
                Timer healthTimer = new Timer((int) WRIT_OF_ACESO_HEALING_FREQUENCY.getValue(), null);
                healthTimer.addActionListener(e1 -> {
                    if (isGameRunning()) EpsilonModel.getINSTANCE().addHealth((int) WRIT_OF_ACESO_HEALING_AMOUNT.getValue());
                    if (!isGameOn()) healthTimer.stop();
                });
                healthTimer.start();
            };
            case MELAMPUS -> null;
            case CHIRON -> null;
            case PROTEUS -> e -> EpsilonModel.getINSTANCE().addVertex();
            case EMPUSA -> null;
            case DOLUS -> null;
        };
    }

    public void fire() {
        double now = Game.ELAPSED_TIME;
        if (now - lastSkillTime >= SKILL_COOLDOWN_IN_SECONDS.getValue()) {
            getAction().actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            lastSkillTime = now;
        }
    }

    public enum SkillType {
        ATTACK, GUARD, POLYMORPHIA
    }


//    public void ares(){
//        if (inGameXP >= 100) {
//            if (skillAbilityActivateTime == -1) {
//                skillAbilityActivateTime = ELAPSED_TIME;
//                EPSILON_MELEE_DAMAGE += 2;
//                EPSILON_RANGED_DAMAGE += 2;
//                inGameXP -= 100;
//            } else if (ELAPSED_TIME - skillAbilityActivateTime > 300) {
//                skillAbilityActivateTime = ELAPSED_TIME;
//                EPSILON_MELEE_DAMAGE += 2;
//                EPSILON_RANGED_DAMAGE += 2;
//                inGameXP -= 100;
//            }
//        }
//
//    }
//
//    public void aceso(){
//        if (inGameXP >= 100){
//            if (skillAbilityActivateTime==-1) {
//                skillAbilityActivateTime = ELAPSED_TIME;
//                acesoInProgress = true;
//                if (hpRegainRate>1) hpRegainRate=1;
//                else hpRegainRate /= 2;
//                inGameXP -= 100;
//            }
//            else if (ELAPSED_TIME -skillAbilityActivateTime>300){
//                skillAbilityActivateTime = ELAPSED_TIME;
//                acesoInProgress = true;
//                if (hpRegainRate>1) hpRegainRate=1;
//                else hpRegainRate /= 2;
//                inGameXP -= 100;
//            }
//        }
//    }
//
//    public void proteus(){
//        if (inGameXP >= 100){
//            if (skillAbilityActivateTime == -1){
//                skillAbilityActivateTime = ELAPSED_TIME;
//                EpsilonModel.getINSTANCE().addVertex();
//                inGameXP -=100;
//            }
//            else if (ELAPSED_TIME - skillAbilityActivateTime > 300){
//                skillAbilityActivateTime = ELAPSED_TIME;
//                EpsilonModel.getINSTANCE().addVertex();
//                inGameXP -=100;
//            }
//        }
//    }
//
//    public void heal(){
//        EpsilonModel.getINSTANCE().sumHpWith(10);
//        shopAbility = null;
//    }
//
//    public void empower(){
//        empowerStartTime = ELAPSED_TIME;
//        empowerEndTime = ELAPSED_TIME + 10;
//        empowerIsOn = true;
////            TODO move null maker
//        shopAbility = null;
//    }
//    public void banish(){
//        for (Impactable impactable : impactables) {
//            impactable.banish();
//        }
//        shopAbility = null;
//
//    }
}

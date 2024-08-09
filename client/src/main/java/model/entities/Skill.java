package model.entities;

import controller.Game;
import model.charactersModel.EpsilonModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.SkillConstants.*;
import static controller.constants.EntityConstants.SKILL_COOLDOWN_IN_SECONDS;

public enum Skill {
    ARES, ASTRAPE, CERBERUS,
    ACESO, MELAMPUS, CHIRON, ATHENA,
    PROTEUS, EMPUSA, DOLUS;

    public static Skill activeSkill = EMPUSA;
    public boolean acquired = false;
    public double lastSkillTime = -Double.MAX_VALUE;
    private static List<Skill> activeSkills = new CopyOnWriteArrayList<>();

    private static Skill s1 = null;
    private static Skill s2 = null;

    // Define the prerequisites for each skill
    private static final Map<Skill, List<Skill>> prerequisites = new HashMap<>();

    static {
        prerequisites.put(ARES, Collections.emptyList());
        prerequisites.put(ASTRAPE, Collections.singletonList(ARES));
        prerequisites.put(CERBERUS, Arrays.asList(ARES, ASTRAPE));

        prerequisites.put(ACESO, Collections.emptyList());
        prerequisites.put(MELAMPUS, Collections.singletonList(ACESO));
        prerequisites.put(CHIRON, Arrays.asList(ACESO, MELAMPUS));
        prerequisites.put(ATHENA, Arrays.asList(ACESO, MELAMPUS, CHIRON));

        prerequisites.put(PROTEUS, Collections.emptyList());
        prerequisites.put(EMPUSA, Collections.singletonList(PROTEUS));
        prerequisites.put(DOLUS, Arrays.asList(PROTEUS, EMPUSA));
    }

    public static void initializeSkills() {
        activeSkill = ASTRAPE;
        CopyOnWriteArrayList<Skill> acquiredSkillSave = new CopyOnWriteArrayList<>();
        for (String skillName : Profile.getCurrent().acquiredSkillsNames) acquiredSkillSave.add(findSkill(skillName));
        for (Skill skill : acquiredSkillSave) skill.acquired = true;
    }

    public String getName() {
        return "WRIT OF " + name();
    }

    public boolean isAcquired() {
        return acquired;
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

    public static String buySkill(Skill skill, int xp) {
        if (skill.isAcquired()) {
            Skill.activeSkill = skill;
            return  skill.getName() + "is set as your active skill!";
        }
        List<Skill> pres = prerequisites.get(skill);
        for (Skill s : pres){
            if (!s.isAcquired()) return "You must activate the previous skills in this branch first!";
        }

        if (xp < skill.getCost()) return "You don't have enough XP!";

        Profile.getCurrent().totalXP -= skill.getCost();
        skill.acquired = true;
        activeSkills.add(skill);
        return "The skill was successfully purchased!";
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
                Profile.getCurrent().EPSILON_MELEE_DAMAGE += (int) WRIT_OF_ARES_BUFF_AMOUNT.getValue();
                Profile.getCurrent().EPSILON_RANGED_DAMAGE += (int) WRIT_OF_ARES_BUFF_AMOUNT.getValue();
            };
            case ASTRAPE -> e -> EpsilonModel.getINSTANCE().damageSize.put(AttackTypes.ASTRAPE, 2);
            case CERBERUS -> e -> EpsilonModel.getINSTANCE().cerebrus();
            case ACESO -> e -> {
//                Timer healthTimer = new Timer((int) WRIT_OF_ACESO_HEALING_FREQUENCY.getValue(), null);
//                healthTimer.addActionListener(e1 -> {
//                    if (isGameRunning()) EpsilonModel.getINSTANCE().addHealth((int) WRIT_OF_ACESO_HEALING_AMOUNT.getValue());
//                    if (!isGameOn()) healthTimer.stop();
//                });
//                healthTimer.start();
            };
            case MELAMPUS -> e -> Profile.getCurrent().EPSILON_VULNERABILITY_PROBABILITY = 95;
            case CHIRON -> e -> Profile.getCurrent().EPSILON_HEALTH_REGAIN = 3;
            case ATHENA -> e -> Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT *= 0.80;
            case PROTEUS -> e -> EpsilonModel.getINSTANCE().addVertex();
            case EMPUSA -> e -> EpsilonModel.getINSTANCE().empusa();
            case DOLUS -> e -> {
                List<Skill> actives = activeSkills;
                Random rand = new Random();
                int index1 = rand.nextInt(actives.size());
                int index2 = rand.nextInt(actives.size());
                do {
                    index2 = rand.nextInt(actives.size());
                } while (index2 == index1);

                s1 = actives.get(index1);
                s2 = actives.get(index2);
                s1.fire();
                s2.fire();
            };
        };
    }

    public void fire() {
        double now = Game.elapsedTime;
        if (now - lastSkillTime >= SKILL_COOLDOWN_IN_SECONDS.getValue()) {
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

    // Method to get prerequisites for a skill
    public static List<Skill> getPrerequisites(Skill skill) {
        return prerequisites.getOrDefault(skill, Collections.emptyList());
    }
}

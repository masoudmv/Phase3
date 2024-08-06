package client.network.containers;


public enum Skill {
    ARES, ASTRAPE, CERBERUS,
    ACESO, MELAMPUS, CHIRON, ATHENA,
    PROTEUS, EMPUSA, DOLUS;

    public static Skill activeSkill = ASTRAPE;
    public boolean acquired = true;
    public double lastSkillTime = -Double.MAX_VALUE;

    public static void initializeSkills() {
//        activeSkill = ASTRAPE;
//        CopyOnWriteArrayList<Skill> acquiredSkillSave = new CopyOnWriteArrayList<>();
//        for (String skillName : Profile.getCurrent().acquiredSkillsNames) acquiredSkillSave.add(findSkill(skillName));
//        for (Skill skill : acquiredSkillSave) skill.acquired = true;
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
}

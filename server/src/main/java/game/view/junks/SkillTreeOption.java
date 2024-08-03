package game.view.junks;

import game.model.entities.Skill;

import java.awt.*;

public class SkillTreeOption {
    private final Skill skill;
    private final Point position;
    private boolean isTouching;

    public SkillTreeOption(Skill skill, Point position) {
        this.skill = skill;
        this.position = position;
        this.isTouching = false;
    }



    public String getName() {
        return skill.getName();
    }

    public Point getPosition() {
        return position;
    }

    public boolean isTouching() {
        return isTouching;
    }

    public void setTouching(boolean touching) {
        isTouching = touching;
    }

    public boolean isSelected() {
        return skill.acquired;
    }

    public void setSelected(boolean selected) {
        // logic for xp check
        skill.acquired = selected;
    }
}

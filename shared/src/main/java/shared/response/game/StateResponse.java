package shared.response.game;

import shared.Model.EntityType;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;
import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


@JsonTypeName("StateResponse")
public class StateResponse implements Response {
    private Map<String, EntityType> createdEntities = new HashMap<>();
    private Map<String, DummyPanel> createdPanels = new HashMap<>();
    private Map<String, DummyModel> updatedModels = new HashMap<>();
    private Map<String, DummyPanel> updatedPanels = new HashMap<>();
    private List<String> eliminatedEntities = new CopyOnWriteArrayList<>();


    private int health = 0;
    private int gameXP = 0;
    private int elapsedTime = 0;
    private int wave  = 0;
    private String skill;
    private int otherHealth = 0;
    private boolean isPaused = false;



    public StateResponse() {
    }

    public Map<String, EntityType> getCreatedEntities() {
        return createdEntities;
    }

    public void setCreatedEntities(Map<String, EntityType> createdEntities) {
        this.createdEntities = createdEntities;
    }

    public Map<String, DummyPanel> getCreatedPanels() {
        return createdPanels;
    }

    public void setCreatedPanels(Map<String, DummyPanel> createdPanels) {
        this.createdPanels = createdPanels;
    }

    public List<String> getEliminatedEntities() {
        return eliminatedEntities;
    }

    public void setEliminatedEntities(List<String> eliminatedEntities) {
        this.eliminatedEntities = eliminatedEntities;
    }

    public Map<String, DummyModel> getUpdatedModels() {
        return updatedModels;
    }

    public void setUpdatedModels(Map<String, DummyModel> updatedModels) {
        this.updatedModels = updatedModels;
    }

    public Map<String, DummyPanel> getUpdatedPanels() {
        return updatedPanels;
    }

    public void setUpdatedPanels(Map<String, DummyPanel> updatedPanels) {
        this.updatedPanels = updatedPanels;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getGameXP() {
        return gameXP;
    }

    public void setGameXP(int gameXP) {
        this.gameXP = gameXP;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getOtherHealth() {
        return otherHealth;
    }

    public void setOtherHealth(int otherHealth) {
        this.otherHealth = otherHealth;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleStateResponse(this);
    }
}

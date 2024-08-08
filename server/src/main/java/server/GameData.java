package server;

import shared.model.EntityType;
import shared.model.dummies.DummyModel;
import shared.model.dummies.DummyPanel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {

    public String id;

    private Map<String, EntityType> createdEntities = new ConcurrentHashMap<>();
    private Map<String, DummyPanel> createdPanels = new ConcurrentHashMap<>();
    private Map<String, DummyModel> updatedModels = new ConcurrentHashMap<>();
    private Map<String, DummyPanel> updatedPanels = new ConcurrentHashMap<>();
    private List<String> eliminatedEntities = new CopyOnWriteArrayList<>();

    public GameData(String id) {
        this.id = id;
    }


    public GameData() {
    }

    public synchronized void createEntity(String id, EntityType entityType){
        createdEntities.put(id, entityType);
    }

    public synchronized void createPanel(String id, Point2D location, Dimension dimension){
        int x = (int) location.getX();
        int y = (int) location.getY();
        DummyPanel panel = new DummyPanel(id, new Point(x, y), dimension);
        createdPanels.put(id, panel);
    }

    public synchronized void eliminateEntity(String id){
        eliminatedEntities.add(id);
    }


    public synchronized void clearModels(){
        updatedModels.clear();
    }

    public synchronized void addUpdatedModels(DummyModel model){
        updatedModels.put(model.getId(), model);
    }

    public synchronized void clearPanels(){
        updatedPanels.clear();
    }

    public synchronized void addUpdatedPanels(DummyPanel panel){
        updatedPanels.put(panel.getId(), panel);
    }

    public synchronized Map<String, DummyPanel> getCreatedPanels() {
        return createdPanels;
    }

    public synchronized Map<String, EntityType> getCreatedEntities() {
        return createdEntities;
    }

    public synchronized Map<String, DummyPanel> getUpdatedPanels() {
        return updatedPanels;
    }

    public synchronized Map<String, DummyModel> getUpdatedModels() {
        return updatedModels;
    }

    public synchronized List<String> getEliminatedEntities() {
        return eliminatedEntities;
    }






}

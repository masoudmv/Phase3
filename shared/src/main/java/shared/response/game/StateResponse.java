package shared.response.game;

import shared.Model.EntityType;
import shared.Model.Pair;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;
import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@JsonTypeName("StateResponse")
public class StateResponse implements Response {
    private List<Pair<String, EntityType>> createdEntities = new CopyOnWriteArrayList<>();
    private List<Pair<String, DummyPanel>> createdPanels = new CopyOnWriteArrayList<>();
    private List<String> eliminatedEntities = new CopyOnWriteArrayList<>();
    private List<DummyModel> updatedModels = new CopyOnWriteArrayList<>();
    private List<DummyPanel> updatedPanels = new CopyOnWriteArrayList<>();

    public StateResponse(List<DummyPanel> updatedPanels, List<Pair<String, DummyPanel>> createdPanels, List<DummyModel> updatedModels, List<String> eliminatedEntities, List<Pair<String, EntityType>> createdEntities) {
        this.updatedPanels = updatedPanels;
        this.createdPanels = createdPanels;
        this.updatedModels = updatedModels;
        this.eliminatedEntities = eliminatedEntities;
        this.createdEntities = createdEntities;
    }

    public StateResponse() {
    }

    public void setCreatedEntities(List<Pair<String, EntityType>> createdEntities) {
        this.createdEntities = createdEntities;
    }

    public void setEliminatedEntities(List<String> eliminatedEntities) {
        this.eliminatedEntities = eliminatedEntities;
    }

    public void setUpdatedModels(List<DummyModel> updatedModels) {
        this.updatedModels = updatedModels;
    }

    public void setCreatedPanels(List<Pair<String, DummyPanel>> createdPanels) {
        this.createdPanels = createdPanels;
    }

    public void setUpdatedPanels(List<DummyPanel> updatedPanels) {
        this.updatedPanels = updatedPanels;
    }


    public List<Pair<String, EntityType>> getCreatedEntities() {
        return createdEntities;
    }

    public List<String> getEliminatedEntities() {
        return eliminatedEntities;
    }

    public List<DummyModel> getUpdatedModels() {
        return updatedModels;
    }

    public List<Pair<String, DummyPanel>> getCreatedPanels() {
        return createdPanels;
    }

    public List<DummyPanel> getUpdatedPanels() {
        return updatedPanels;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleStateResponse(this);
    }
}

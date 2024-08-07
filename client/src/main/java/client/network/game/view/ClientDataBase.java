package client.network.game.view;

import client.network.game.view.charactersView.GeoShapeView;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static client.network.game.view.FinalPanelView.finalPanelViews;
import static client.network.game.view.charactersView.GeoShapeView.geoShapeViews;

public class ClientDataBase {
    public static Map<String, DummyModel> models = new ConcurrentHashMap<>();
    public static Map<String, DummyPanel> panels = new ConcurrentHashMap<>();


    public static void removeAll(){
        for (FinalPanelView finalPanelView : finalPanelViews){
            finalPanelView.eliminate();
        }

        for (GeoShapeView  view : geoShapeViews){
            view.eliminate();
        }
    }
}

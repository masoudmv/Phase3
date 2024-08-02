package controller;

import model.FinalPanelModel;
import model.MyPolygon;
import model.TimedLocation;
import model.charactersModel.BulletModel;
import model.charactersModel.*;
import model.entities.Ability;
import model.entities.Skill;
import view.FinalPanelView;
import view.charactersView.BulletView;
import view.charactersView.CollectibleView;
import view.charactersView.GeoShapeView;
import view.charactersView.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import static controller.Utils.relativeLocation;
import static model.charactersModel.NecropickModel.necropickModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static view.FinalPanelView.finalPanelViews;
import static view.charactersView.BulletView.bulletViews;
import static view.charactersView.GeoShapeView.geoShapeViews;
import static view.charactersView.NecropickView.necropickViews;

public abstract class UserInterfaceController {


    public static void createEpsilonView(String id, Image image){
        new EpsilonView(id, image);
    }
    public static void createSquarantineView(String id){
        new SquarantineView(id);
    }
    public static void createTrigorathView(String id){ new TrigorathView(id); }
    public static void createOmenoctView(String id){ new SquarantineView(id); }

    public static void creatBulletView(String id){ new BulletView(id); }
    public static void createCollectibleView(String id){ new CollectibleView(id); }
    public static void createBabyEpsilonView(String id){ new BabyEpsilonView(id); }
    public static void createSmileyAOEView(String id){ new SmileyAOE(id); }








    public static void createFinalPanelView(String id, Point2D location, Dimension size){
        new FinalPanelView(id, location, size);
    } // todo edit parameters



    public static void createGeoShapeView(String id, Image image){
        new GeoShapeView(id, image);
    }

    public static void createGeoShapeView(String id, Image image, int zOrder){
        new GeoShapeView(id, image, zOrder);
    }




    public static void createNecropickView(String id, Image image){
        new NecropickView(id, image);
    }

    public static void createArchmireView(String id, Image image){
        new ArchmireView(id, image);
    }

    public static void createLaserView(String id){
        new LaserView(id);
    }

    public static void createGeoShapeView(String id){ new GeoShapeView(id); }



    public static Point2D calculateViewLocationPolygonalEnemy(FinalPanelView component, String id){
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        Point corner = new Point(component.getX(), component.getY());
        assert geoShapeModel != null;
        return relativeLocation(geoShapeModel.getAnchor(), corner);
    }




    public static MyPolygon calculateEntityView(FinalPanelView component, String id){
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert geoShapeModel != null;

        double[] xpoints = new double[geoShapeModel.myPolygon.npoints];
        double[] ypoints = new double[geoShapeModel.myPolygon.npoints];

        for (int i = 0; i < geoShapeModel.myPolygon.npoints; i++) {
            xpoints[i] = geoShapeModel.myPolygon.xpoints[i] - component.getX();
            ypoints[i] = geoShapeModel.myPolygon.ypoints[i] - component.getY();
        }

        return new MyPolygon(xpoints, ypoints, geoShapeModel.myPolygon.npoints);
    }


    public static double calculateGeoShapeViewAngle(String id){
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        assert geoShapeModel != null;
        return geoShapeModel.getAngle();
    }




    public static GeoShapeModel findGeoShapeModel(String id){
        for (GeoShapeModel geoShapeModel : GeoShapeModel.entities){
            if (geoShapeModel.getId().equals(id)) return geoShapeModel;
        }
        return null;
    }

    public static GeoShapeView findGeoShapeView(String id){
        for (GeoShapeView geoShapeView : GeoShapeView.geoShapeViews){
            if (geoShapeView.getId().equals(id)) return geoShapeView;
        }
        return null;
    }


    public static void updateGeoShapeViewProperties(){
        for (FinalPanelView finalPanelView : finalPanelViews){
            for (GeoShapeView geoShapeView : geoShapeViews){
                Point2D currentLocation = calculateViewLocationPolygonalEnemy(finalPanelView, geoShapeView.getId());
                String panelID = finalPanelView.getId();
                geoShapeView.setCurrentLocation(panelID, currentLocation);
                geoShapeView.setMyPolygon(panelID, calculateEntityView(finalPanelView, geoShapeView.getId()));
                geoShapeView.setHistory(panelID, calculateLocationHistory(finalPanelView, geoShapeView.getId()));
            }
        }

        for (GeoShapeView geoShapeView : geoShapeViews){
            geoShapeView.setAngle(calculateGeoShapeViewAngle(geoShapeView.getId()));
        }
    }


    public static void updateNecropick(String id){
        NecropickModel m = findNecropickModel(id);
        NecropickView v = findNecropickView(id);
        assert m != null;
        assert v != null;
        v.showNextLocation = m.isHovering();
    }

    public static LinkedList<TimedLocation> calculateLocationHistory(FinalPanelView component, String id){
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        if (!(geoShapeModel instanceof ArchmireModel)) return null;
        LinkedList<TimedLocation> timedLocations = ((ArchmireModel) geoShapeModel).getLocationHistory();
        LinkedList<TimedLocation> result = new LinkedList<>();
        for (TimedLocation timedLocation : timedLocations){
            double timeStamp = timedLocation.getTimestamp();
            MyPolygon pol = timedLocation.getMyPolygon();

            int[] xPoints = new int[pol.npoints];
            int[] yPoints = new int[pol.npoints];

            for (int i = 0; i < pol.npoints; i++) {
                xPoints[i] = (int) (pol.xpoints[i] - component.getX());
                yPoints[i] = (int) (pol.ypoints[i] - component.getY());
            }

            TimedLocation t = new TimedLocation(new Polygon(xPoints, yPoints, pol.npoints), timeStamp);
            result.add(t);
        }
        return result;
    }



    public static Point2D calculateLocationOfFinalPanelView(String id){
        FinalPanelModel f = findFinalPanelModel(id);
        return f.getLocation();
    }


    public static Dimension calculateDimensionOfFinalPanelView(String id){
        FinalPanelModel f = findFinalPanelModel(id);
        return new Dimension((int) f.getSize().getWidth(), (int) f.getSize().getHeight());

    }





    public static EpsilonModel findModel(String id){
        return EpsilonModel.getINSTANCE();
    }

    public static SquarantineModel findSquarantineModel(String id){
        for (SquarantineModel squarantineModel: squarantineModels){
            if (squarantineModel.getId().equals(id)) return squarantineModel;
        }
        return null;
    }
    public static TrigorathModel findTrigorathModel(String id){
        for (TrigorathModel trigorathModel: trigorathModels){
            if (trigorathModel.getId().equals(id)) return trigorathModel;
        }
        return null;
    }



    public static FinalPanelModel findFinalPanelModel(String id){
        for (FinalPanelModel f: FinalPanelModel.finalPanelModels){
            if (f.getId().equals(id)) return f;
        }
        return null;
    }




    public static void removeFinalPanelView(String id){
        FinalPanelView finalPanelView = findFinalPanelView(id);
        finalPanelView.eliminate();
    }

    public static BulletView findBulletView(String id){
        for (BulletView bulletView: bulletViews){
            if (bulletView.getId().equals(id)) return bulletView;
        }
        return null;
    }

    public static void eliminateBulletView(String id){
        findBulletView(id).eliminate();
    }





    public static NecropickView findNecropickView(String id){
        for (NecropickView n: necropickViews){
            if (n.getId().equals(id)) return n;
        }
        return null;
    }

    public static NecropickModel findNecropickModel(String id){
        for (NecropickModel m: necropickModels){
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    public static FinalPanelView findFinalPanelView(String id){
        for (FinalPanelView f: finalPanelViews){
            if (f.getId().equals(id)) return f;
        }
        return null;
    }


    public static Skill findSkill(String name) {
        for (Skill.SkillType type : Skill.SkillType.values()) {
            for (Skill skill : Skill.values()) {
                if (skill.getType().equals(type) && skill.getName().equals(name)) return skill;
            }
        }
        return null;
    }

    public static void fireSkill() {
        if (Skill.activeSkill != null) {
            Skill.activeSkill.fire();
        }
    }

    public static void fireAbility() {
        if (Ability.activeAbility != null) {
            Ability.activeAbility.fire();
            Ability.activeAbility = null;
        }
    }

    public static boolean isGameRunning() {
        return GameLoop.getINSTANCE().isRunning();
    }

    public static boolean isGameOn() {
        return GameLoop.getINSTANCE().isOn();
    }

    public static void eliminateGeoShapeView(String id){
        findGeoShapeView(id).eliminate();
    }


}
package client.network.game.controller;

import client.network.game.view.ClientDataBase;
import client.network.game.view.FinalPanelView;
import client.network.game.view.charactersView.*;

import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static client.network.game.view.FinalPanelView.finalPanelViews;
import static client.network.game.view.charactersView.BulletView.bulletViews;
import static client.network.game.view.charactersView.GeoShapeView.geoShapeViews;


public abstract class UserInterfaceController {
    public static void createPolygonView(String id){
        new PolygonView(id);
    }

    public static void createBabyEpsilonView(String id){
        new BabyEpsilonView(id);
    }




    public static void creatBulletView(String id){ new BulletView(id); }

    public static void createCollectibleView(String id){ new CollectibleView(id); }


    public static void createFinalPanelView(String id, Point2D location, Dimension size){ new FinalPanelView(id, location, size); } // todo edit parameters

    public static void createEpsilonView(String id){
        new EpsilonView(id);
    }

    public static void eliminateGeoShapeView(String id){ findGeoShapeView(id).eliminate(); }

    public static void eliminateFinalPanelView(String id){ findFinalPanelView(id).eliminate(); }

    public static void eliminateBulletView(String id){ findBulletView(id).eliminate(); }

    public static void createLaserView(String id){
        new LaserView(id);
    }

    public static void createBarricadosView(String id){
        new BarricadosView(id);
    }

//    public static void createGeoShapeView(String id, Image image){
//        new GeoShapeView(id, image);
//    }

    public static void createOrbView(String id){
        new OrbView(id);
    }

    public static void createArchmireView(String id){
        new ArchmireView(id);
    }

    public static void createBabyArchmireView(String id){
        new BabyArchmireView(id);
    }


    public static void createOmenoctView(String id){ new PolygonView(id); }
    public static void createSmileyAOEView(String id){ new SmileyAOE(id); }
    public static void createGeoShapeView(String id){ new GeoShapeView(id); }
    public static void createNecropickView(String id){
        new NecropickView(id);
    }
    public static void createGeoShapeView(String id, Image image, int zOrder){
        new GeoShapeView(id, image, zOrder);
    }



    public static Point2D calculateViewLocationPolygonalEnemy(FinalPanelView component, String id){
        DummyModel geoShapeModel = findGeoShapeModel(id);
        Point corner = new Point(component.getX(), component.getY());
        assert geoShapeModel != null;
        return Utils.relativeLocation(geoShapeModel.getAnchor(), corner);
    }




    public static MyPolygon calculateEntityView(FinalPanelView component, String id){
        DummyModel geoShapeModel = findGeoShapeModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert geoShapeModel != null;

        int[] xpoints = geoShapeModel.getxPoints();
        int[] ypoints = geoShapeModel.getyPoints();
        int numPoints = geoShapeModel.getnPoints();

        double[] xs = new double[numPoints];
        double[] ys = new double[numPoints];

        for (int i = 0; i < numPoints; i++) {
            xs[i] = xpoints[i] - component.getX();
            ys[i] = ypoints[i] - component.getY();
        }

        return new MyPolygon(xs, ys, numPoints);
    }


    public static double calculateGeoShapeViewAngle(String id){
        DummyModel geoShapeModel = findGeoShapeModel(id);
        assert geoShapeModel != null;
        return geoShapeModel.getAngle();
    }




    public static DummyModel findGeoShapeModel(String id){
//        for (DummyModel geoShapeModel : ClientDataBase.models){
//            if (geoShapeModel.getId().equals(id)) return geoShapeModel;
//        }

        return ClientDataBase.models.get(id);
//        return null;
    }

    public static GeoShapeView findGeoShapeView(String id){
        for (GeoShapeView geoShapeView : geoShapeViews){
            if (geoShapeView.getId().equals(id)) return geoShapeView;
        }
        return null;
    }


    public static void updateGeoShapeViewProperties(){
//        System.out.println(SwingUtilities.isEventDispatchThread());


        for (FinalPanelView f : finalPanelViews){
            f.setLocation(calculateLocationOfFinalPanelView(f.getId()));
            f.setSize(calculateDimensionOfFinalPanelView(f.getId()));
        }


        for (FinalPanelView finalPanelView : finalPanelViews){
            for (GeoShapeView geoShapeView : geoShapeViews){
                DummyModel m = findGeoShapeModel(geoShapeView.getId());
                // TODO remove the following if ...
                if (! (m== null)){
                    Point2D currentLocation = calculateViewLocationPolygonalEnemy(finalPanelView, geoShapeView.getId());
                    String panelID = finalPanelView.getId();
                    geoShapeView.setCurrentLocation(panelID, currentLocation);
                    geoShapeView.setMyPolygon(panelID, calculateEntityView(finalPanelView, geoShapeView.getId()));
                    geoShapeView.setHistory(panelID, calculateLocationHistory(finalPanelView, geoShapeView.getId()));

                } else System.out.println("null geoshapemodel!!!!");
            }
        }

//        for (GeoShapeView geoShapeView : geoShapeViews){
//            geoShapeView.setAngle(calculateGeoShapeViewAngle(geoShapeView.getId()));
//        }



    }


//    public static void updateNecropick(String id){
//        NecropickModel m = findNecropickModel(id);
//        NecropickView v = findNecropickView(id);
//        assert m != null;
//        assert v != null;
//        v.showNextLocation = m.isHovering();
//    }

    public static List<Polygon> calculateLocationHistory(FinalPanelView component, String id){
        DummyModel geoShapeModel = findGeoShapeModel(id);
        List<Polygon> polygons = geoShapeModel.getPolygons();
        List<Polygon> res = new ArrayList<>();

        for (Polygon polygon : polygons){
            if (polygon == null) break;
            int[] xPoints = new int[polygon.npoints];
            int[] yPoints = new int[polygon.npoints];

            for (int i = 0; i < polygon.npoints; i++) {
                xPoints[i] = polygon.xpoints[i] - component.getX();
                yPoints[i] = polygon.ypoints[i] - component.getY();
            }

            res.add(new Polygon(xPoints, yPoints, polygon.npoints));

        }
        return res;
    }



    public static Point2D calculateLocationOfFinalPanelView(String id){
        DummyPanel f = findFinalPanelModel(id);
        return f.getLocation();
    }


    public static Dimension calculateDimensionOfFinalPanelView(String id){
        DummyPanel f = findFinalPanelModel(id);
        return new Dimension((int) f.getDimension().getWidth(), (int) f.getDimension().getHeight());

    }






    public static DummyPanel findFinalPanelModel(String id){
        return ClientDataBase.panels.get(id);
    }






    public static BulletView findBulletView(String id){
        for (BulletView bulletView: bulletViews){
            if (bulletView.getId().equals(id)) return bulletView;
        }
        return null;
    }






    // todo wtf?
//    public static NecropickView findNecropickView(String id){
//        for (NecropickView n : necropickViews){
//            if (n.getId().equals(id)) return n;
//        }
//        return null;
//    }

//    public static NecropickModel findNecropickModel(String id){
//        for (NecropickModel m: NecropickModel.necropickModels){
//            if (m.getId().equals(id)) return m;
//        }
//        return null;
//    }

    public static FinalPanelView findFinalPanelView(String id){
        for (FinalPanelView f: finalPanelViews){
            if (f.getId().equals(id)) return f;
        }
        return null;
    }


//    public static Skill findSkill(String name) {
//        for (Skill.SkillType type : Skill.SkillType.values()) {
//            for (Skill skill : Skill.values()) {
//                if (skill.getType().equals(type) && skill.getName().equals(name)) return skill;
//            }
//        }
//        return null;
//    }

//    public static void fireSkill() {
//        if (Skill.activeSkill != null) {
//            Skill.activeSkill.fire();
//        }
//    }

//    public static void fireAbility() {
//        if (Ability.activeAbility != null) {
//            Ability.activeAbility.fire();
//            Ability.activeAbility = null;
//        }
//    }

//    public static boolean isGameRunning() {
//        return GameLoop.getINSTANCE().isRunning();
//    }
//
//    public static boolean isGameOn() {
//        return GameLoop.getINSTANCE().isOn();
//    }




}
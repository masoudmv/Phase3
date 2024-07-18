package controller;

import model.FinalPanelModel;
import model.MyPolygon;
import model.TimedLocation;
import model.charactersModel.BulletModel;
import model.charactersModel.CollectibleModel;
import model.charactersModel.*;
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
import static model.charactersModel.CollectibleModel.collectibleModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static view.FinalPanelView.finalPanelViews;
import static view.charactersView.BulletView.bulletViews;
import static view.charactersView.CollectibleView.collectibleViews;
import static view.charactersView.NecropickView.necropickViews;
import static view.charactersView.SquarantineView.squarantineViews;
import static view.charactersView.TrigorathView.trigorathViews;

public abstract class UserInterfaceController {




    // what a fucking mess!!!
    public static void createEpsilonView(String id){
        new EpsilonView(id);
    }
    public static void createSquarantineView(String id){
        new SquarantineView(id);
    }
    public static void createTrigorathView(String id){ new TrigorathView(id); }

    public static void creatBulletView(String id){ new BulletView(id); }
    public static void createCollectibleView(String id){ new CollectibleView(id); }
    public static void createBabyEpsilon(String id){ new BabyEpsilonView(id); }








    public static void createFinalPanelView(String id, Point2D location, Dimension size){
        new FinalPanelView(id, location, size);
    } // todo edit parameters



    public static void createGeoShapeView(String id, Image image){ new GeoShapeView(id, image); }
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

    public static Point2D calculateViewLocationPolygonalEnemy(Component component, String id){
        GeoShapeModel geoShapeModel = findGeoShapeModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert geoShapeModel != null;

//        System.out.println("Corner:   " + corner.getX() + "    " +  corner.getY());
//        System.out.println("object:   " + polygonalEnemyModel.getAnchor().getX() + "    " +  polygonalEnemyModel.getAnchor().getY());

        return relativeLocation(geoShapeModel.getAnchor(),corner);
    }



    public static MyPolygon calculateEntityView(Component component, String id){
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



    public static Polygon calculateEntityView(Component component, MyPolygon polygon){
        MyPolygon myPolygon = polygon;
        assert myPolygon != null;

        int[] xPoints = new int[myPolygon.npoints];
        int[] yPoints = new int[myPolygon.npoints];

        for (int i = 0; i < myPolygon.npoints; i++) {
            xPoints[i] = (int) myPolygon.xpoints[i] - component.getX();
            yPoints[i] = (int) myPolygon.ypoints[i] - component.getY();
        }

        return new Polygon(xPoints, yPoints, myPolygon.npoints);
    }

    public static Point calculateEntityView(Component component, Point2D anchor){
//        Point2D anc = circle;
        assert anchor != null;
        double x = anchor.getX() - component.getX();
        double y = anchor.getY() - component.getY();
        return new Point((int) x, (int) y);
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


    public static void updateGeoShapeViewsLocations(Component component){
        for (GeoShapeView geoShapeView : GeoShapeView.geoShapeViews){
            geoShapeView.setCurrentLocation(
                    calculateViewLocationPolygonalEnemy(component, geoShapeView.getId())
            );

            geoShapeView.setMyPolygon(calculateEntityView(component, geoShapeView.getId()));
            geoShapeView.setAngle(calculateGeoShapeViewAngle(geoShapeView.getId()));
            geoShapeView.setLocationHistory(calculateLocationHistory(component, geoShapeView.getId())); //archmire
        }
    }

    private static LinkedList<TimedLocation> calculateLocationHistory(Component component, String id){
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

















    public static Point2D calculateViewLocationEpsilon(Component component, String id){
        EpsilonModel epsilonModel = findModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert epsilonModel != null;
        return relativeLocation(epsilonModel.getAnchor(),corner);
    }





    public static ArrayList<Point2D> calculateViewLocationOfEpsilonVertices(Component component, String id){
        EpsilonModel epsilonModel = findModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert epsilonModel != null;
        ArrayList<Point2D> viewLocationOfVertices = new ArrayList<>();
        for (int i = 0; i < epsilonModel.numberOfVertices; i++) {
            viewLocationOfVertices.add(relativeLocation(epsilonModel.vertices.get(i), corner));
        }
        return viewLocationOfVertices;
    }



//    public static Point2D calculateViewLocationEntity(Component component, String id){
//        EpsilonModel epsilonModel = findModel(id);
//        Point corner = new Point(component.getX(),component.getY());
//        assert epsilonModel != null;
//        return relativeLocation(epsilonModel.getAnchor(),corner);
//    }




    public static Point2D calculateViewLocationCollectible(Component component, String id){
        CollectibleModel collectibleModel = findCollectibleModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert collectibleModel != null;
        return relativeLocation(collectibleModel.getAnchor(),corner);
    }

    public static Point2D calculateLocationOfFinalPanelView(String id){
        FinalPanelModel f = findFinalPanelModel(id);
        return f.getLocation();
    }


    public static Dimension calculateDimensionOfFinalPanelView(String id){
        FinalPanelModel f = findFinalPanelModel(id);
        return new Dimension((int) f.getSize().getWidth(), (int) f.getSize().getHeight());

    }


    public static Point2D[] calculateViewLocationSquarantine(Component component, String id){
        SquarantineModel squarantineModel = findSquarantineModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert squarantineModel != null;
        Point2D point1 = relativeLocation(squarantineModel.getVertices()[0], corner);
        Point2D point2 = relativeLocation(squarantineModel.getVertices()[1], corner);
        Point2D point3 = relativeLocation(squarantineModel.getVertices()[2], corner);
        Point2D point4 = relativeLocation(squarantineModel.getVertices()[3], corner);
        return new Point2D[]{point1, point2, point3, point4};
    }
    public static Point2D[] calculateViewLocationTrigorath(Component component, String id){
        TrigorathModel trigorathModel = findTrigorathModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert trigorathModel != null;
        Point2D point1 = relativeLocation(trigorathModel.getVertices()[0], corner);
        Point2D point2 = relativeLocation(trigorathModel.getVertices()[1], corner);
        Point2D point3 = relativeLocation(trigorathModel.getVertices()[2], corner);
        return new Point2D[]{point1, point2, point3};
    }




//    public static void updateFinalPanelView(String id){
//        FinalPanelView f = findFinalPanelView(id);
//
//    }





//    public static Point2D[] calculateViewLocationOmenoct(Component component, String id){
//        OmenoctModel omenoctModel = findOmenoctModel(id);
//        Point corner = new Point(component.getX(),component.getY());
//        assert omenoctModel != null;
//        Point2D[] out = new Point2D[omenoctModel.npoints];
//        for (int i = 0; i < omenoctModel.npoints; i++) {
//            out[i] = relativeLocation(omenoctModel.getVertices()[i], corner);
//        } return out;
//    }





    public static Point2D calculateViewLocationBullet(Component component, String id){
        BulletModel bulletModel = findBulletModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert bulletModel != null;
        return relativeLocation(bulletModel.getAnchor(),corner);
    }

    // todo use oop paradigm!

//    public static Enemy findEnemyModel(){
//        for (Enemy enemy: enemy ){
//            if (epsilonModel.getId().equals(id)) return epsilonModel;
//        }
//        return null;
//    }

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
    public static BulletModel findBulletModel(String id){
        for (BulletModel bulletModel: BulletModel.bulletModels){
            if (bulletModel.getId().equals(id)) return bulletModel;
        }
        return null;
    }


//    public static OmenoctModel findOmenoctModel(String id){
//        for (OmenoctModel omenoctModel:OmenoctModel.omenoctModels){
//            if (omenoctModel.getId().equals(id)) return omenoctModel;
//        }
//        return null;
//    }


    public static CollectibleModel findCollectibleModel(String id){
        for (CollectibleModel collectibleModel: collectibleModels){
            if (collectibleModel.getId().equals(id)) return collectibleModel;
        }
        return null;
    }


    public static FinalPanelModel findFinalPanelModel(String id){
        for (FinalPanelModel f: FinalPanelModel.finalPanelModels){
            if (f.getId().equals(id)) return f;
        }
        return null;
    }










    public static CollectibleView findCollectibleView(String id){
        for (CollectibleView collectibleView: collectibleViews){
            if (collectibleView.getId().equals(id)) return collectibleView;
        }
        return null;
    }

    public static BulletView findBulletView(String id){
        for (BulletView bulletView: bulletViews){
            if (bulletView.getId().equals(id)) return bulletView;
        }
        return null;
    }

    public static TrigorathView findTrigorathView(String id){
        for (TrigorathView trigorathView: trigorathViews){
            if (trigorathView.getId().equals(id)) return trigorathView;
        }
        return null;
    }
    public static SquarantineView findSquarantineView(String id){
        for (SquarantineView squarantineView: squarantineViews){
            if (squarantineView.getId().equals(id)) return squarantineView;
        }
        return null;
    }



    public static NecropickView findNecropickView(String id){
        for (NecropickView n: necropickViews){
            if (n.getId().equals(id)) return n;
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
        if (Skill.activeSkill != null) Skill.activeSkill.fire();
    }

    public static boolean isGameRunning() {
        return GameLoop.getINSTANCE().isRunning();
    }

    public static boolean isGameOn() {
        return GameLoop.getINSTANCE().isOn();
    }

}
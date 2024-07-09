package controller;

import model.BulletModel;
import model.CollectibleModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquarantineModel;
import model.charactersModel.TrigorathModel;
import view.BulletView;
import view.CollectibleView;
import view.charactersView.EpsilonView;
import view.charactersView.SquarantineView;
import view.charactersView.TrigorathView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.Utils.relativeLocation;
import static model.CollectibleModel.collectibleModels;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static view.BulletView.bulletViews;
import static view.CollectibleView.collectibleViews;
import static view.charactersView.SquarantineView.squarantineViews;
import static view.charactersView.TrigorathView.trigorathViews;

public abstract class Controller {
    public static void createEpsilonView(String id){
        new EpsilonView(id);
    }
    public static void createSquarantineView(String id){
        new SquarantineView(id);
    }
    public static void createTrigorathView(String id){ new TrigorathView(id); }
    public static void creatBulletView(String id){ new BulletView(id); }
    public static void createCollectibleView(String id){ new CollectibleView(id); }
    public static Point2D calculateViewLocationEpsilon(Component component, String id){
        EpsilonModel epsilonModel = findModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert epsilonModel != null;
        return relativeLocation(epsilonModel.getAnchor(),corner);
    }

    public static Point2D calculateViewLocationCollectible(Component component, String id){
        CollectibleModel collectibleModel = findCollectibleModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert collectibleModel != null;
        return relativeLocation(collectibleModel.getAnchor(),corner);
    }


    public static ArrayList<Point2D> calculateViewLocationOfEpsilonVertices(Component component, String id){
        EpsilonModel epsilonModel = findModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert epsilonModel != null;
        ArrayList<Point2D> viewLocationOfVertices = new ArrayList<>();

//        if (epsilonModel.numberOfVertices>1) {
            for (int i = 0; i < epsilonModel.numberOfVertices; i++) {
    //            Point vertex = new Point((int) epsilonModel.vertices.get(i).getX(), (int) epsilonModel.vertices.get(i).getY());
                viewLocationOfVertices.add(relativeLocation(epsilonModel.vertices.get(i), corner));
            }
//        }
        return viewLocationOfVertices;
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
//        return relativeLocation(trigorathModel.getAnchor(),corner);
        Point2D point1 = relativeLocation(trigorathModel.getVertices()[0], corner);
        Point2D point2 = relativeLocation(trigorathModel.getVertices()[1], corner);
        Point2D point3 = relativeLocation(trigorathModel.getVertices()[2], corner);
        return new Point2D[]{point1, point2, point3};
    }

    public static Point2D calculateViewLocationBullet(Component component, String id){
        BulletModel bulletModel = findBulletModel(id);
        Point corner = new Point(component.getX(),component.getY());
        assert bulletModel != null;
        return relativeLocation(bulletModel.getAnchor(),corner);
    }

    public static EpsilonModel findModel(String id){
        for (EpsilonModel epsilonModel: EpsilonModel.epsilonModels){
            if (epsilonModel.getId().equals(id)) return epsilonModel;
        }
        return null;
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

    public static CollectibleModel findCollectibleModel(String id){
        for (CollectibleModel collectibleModel: collectibleModels){
            if (collectibleModel.getId().equals(id)) return collectibleModel;
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

}
package game.model.charactersModel.smiley;

import game.controller.Game;
import game.controller.UserInterfaceController;
import game.controller.Utils;
import shared.constants.EntityConstants;
import shared.Model.MyPolygon;
import game.model.charactersModel.EpsilonModel;
import game.model.charactersModel.GeoShapeModel;

import java.awt.geom.Point2D;
import java.util.Random;

public class SmileyAOE extends GeoShapeModel {

    private static double birthTime = Double.MAX_VALUE;
    private static double lastDamageTime = 0;

    private static SmileyAOE a1;
    private static SmileyAOE a2;
    private static SmileyAOE a3;
    private static boolean isActivated;


    public SmileyAOE(Point2D anchor, String gameID) {
        super(gameID);
        this.anchor = anchor;
        this.radius = EntityConstants.SMILEY_AOE_RADIUS.getValue();

        setDummyPolygon();
//        UserInterfaceController.createSmileyAOEView(id);
    }

    public static void updateAll(){
        if (birthTime == Double.MAX_VALUE) return;
        double now = Game.ELAPSED_TIME;
        if (now - birthTime > EntityConstants.SMILEY_AOE_ACTIVATION_TIME.getValue()) isActivated = true;
        if (now - birthTime > EntityConstants.SMILEY_AOE_ACTIVATED_LIFETIME.getValue()) {
            eliminateAll();
            birthTime = Double.MAX_VALUE;
        }

        if (isActivated && isEpsilonInside()) {
            // TODO damage epsilon
            System.out.println("DDDDDDDDDDDDD");
        }
    }



    private static void eliminateAll(){
        a1.eliminate();
        a2.eliminate();
        a3.eliminate();
    }


    @Override
    public void eliminate(){
        super.eliminate();
    }




    public static void createVomitAOEs(String gameID){
        if (birthTime != Double.MAX_VALUE) return;

        birthTime = Game.ELAPSED_TIME;
        isActivated = false;

        Point2D epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
        double xMin = epsilonAnchor.getX() - EntityConstants.SMILEY_AOE_RECTANGLE_LENGTH.getValue();
        double xMax = epsilonAnchor.getX() + EntityConstants.SMILEY_AOE_RECTANGLE_LENGTH.getValue();

        double yMin = epsilonAnchor.getY() - EntityConstants.SMILEY_AOE_RECTANGLE_LENGTH.getValue();
        double yMax = epsilonAnchor.getY() + EntityConstants.SMILEY_AOE_RECTANGLE_LENGTH.getValue();

        Random random = new Random();

        double x1 = random.nextDouble(xMin, xMax);
        double x2 = random.nextDouble(xMin, xMax);
        double x3 = random.nextDouble(xMin, xMax);

        double y1 = random.nextDouble(yMin, yMax);
        double y2 = random.nextDouble(yMin, yMax);
        double y3 = random.nextDouble(yMin, yMax);


        a1 = new SmileyAOE(new Point2D.Double(x1, y1), gameID);
        a2 = new SmileyAOE(new Point2D.Double(x2, y2), gameID);
        a3 = new SmileyAOE(new Point2D.Double(x3, y3), gameID);
    }




    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public static boolean isEpsilonInside(){
        double now = Game.ELAPSED_TIME;
        if (now - lastDamageTime < EntityConstants.SMILEY_AOE_COOLDOWN.getValue()) return false;

        Point2D epsilon = EpsilonModel.getINSTANCE().getAnchor();
        Point2D first = a1.getAnchor();
        Point2D second = a2.getAnchor();
        Point2D third = a3.getAnchor();

        double d1 = Utils.findDistance(epsilon, first);
        double d2 = Utils.findDistance(epsilon, second);
        double d3 = Utils.findDistance(epsilon, third);

        double distance = EntityConstants.SMILEY_AOE_RADIUS.getValue() - EpsilonModel.getINSTANCE().getRadius();

        boolean inside1 = d1 < distance;
        boolean inside2 = d2 < distance;
        boolean inside3 = d3 < distance;

        if (inside1 || inside2 || inside3) {
            lastDamageTime = now;
            return true;
        }

        return false;

    }
}

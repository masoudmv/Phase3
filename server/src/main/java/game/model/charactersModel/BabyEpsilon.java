package game.model.charactersModel;

import game.controller.Game;
import game.controller.Utils;
import shared.constants.EntityConstants;
import shared.model.MyPolygon;
import game.model.entities.AttackTypes;
import game.model.movement.Direction;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static game.controller.UserInterfaceController.createBabyEpsilonView;

public class BabyEpsilon extends GeoShapeModel {

    BabyEpsilon(Point2D anchor, String gameID) {
        super(gameID);
        this.anchor = anchor;
        this.radius = EntityConstants.BABY_EPSILON_RADIUS.getValue();
        damageSize.put(AttackTypes.MELEE, 10);
        createBabyEpsilonView(id, gameID);
    }

    public void move(Direction direction) {
        Point2D movement = Utils.multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        this.anchor = Utils.addVectors(anchor, movement);
    }



    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public static void createBabies(String gameID, String macAddress) {
        Game game = findGame(gameID);
        EpsilonModel epsilon = null;
        for (EpsilonModel epsilonModel : game.epsilons){
            if (epsilonModel.getMacAddress().equals(macAddress)) {
                epsilon = epsilonModel;
                break;
            }
        }


        Point2D epsilonAnchor = epsilon.getAnchor();
        double sideLength = 70; // replace with the actual side length if known
        BabyEpsilon[] babies = new BabyEpsilon[3];
        Point2D[] vertices = calculateEquilateralTriangleVertices(epsilonAnchor, sideLength);

        for (int i = 0; i < 3; i++) {
            babies[i] = new BabyEpsilon(vertices[i], gameID);
        }

        epsilon.setBabies(babies);
    }

    @Override
    public ArrayList<Point2D> getBoundingPoints(){
        ArrayList<Point2D> bound = new ArrayList<>();
        bound.add(new Point2D.Double(getAnchor().getX() - getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX() + getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() + getRadius()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() - getRadius()));
        return bound;
    }

    @Override
    public void update() {
        super.update();

    }

    private static Point2D[] calculateEquilateralTriangleVertices(Point2D center, double sideLength) {
        Point2D[] vertices = new Point2D[3];
        double height = (Math.sqrt(3) / 3) * sideLength;

        // Top vertex
        vertices[0] = new Point2D.Double(center.getX(), center.getY() - height);

        // Bottom-left vertex
        vertices[1] = new Point2D.Double(center.getX() - sideLength / 2, center.getY() + height / 2);

        // Bottom-right vertex
        vertices[2] = new Point2D.Double(center.getX() + sideLength / 2, center.getY() + height / 2);

        return vertices;
    }
}

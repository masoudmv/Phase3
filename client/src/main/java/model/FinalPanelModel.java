package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.Utils;
import model.charactersModel.BulletModel;
import model.collision.Collidable;
import model.entities.Profile;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import static controller.UserInterfaceController.*;
import static controller.Utils.*;
import static controller.constants.Constants.FRAME_DIMENSION;
import static model.PanelManager.handlePanelPanelCollision;

public class FinalPanelModel implements Collidable, Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("vertices")
    @Expose
    private ArrayList<Point2D> vertices;

    private transient ArrayList<Line2D> edges = new ArrayList<>();

    @SerializedName("size")
    @Expose
    protected Dimension2D size;

    @SerializedName("location")
    @Expose
    protected Point2D location;

    public static transient ArrayList<FinalPanelModel> finalPanelModels = new ArrayList<>();

    @SerializedName("isIsometric")
    @Expose
    private boolean isIsometric;

    @SerializedName("isRigid")
    @Expose
    private boolean isRigid;

    @SerializedName("isStatic")
    @Expose
    private boolean isStatic;

    private transient boolean moveUp = false;
    private transient boolean moveRight = false;
    private transient boolean moveDown = false;
    private transient boolean moveLeft = false;

    @SerializedName("acceleration")
    @Expose
    private double acceleration;

    @SerializedName("velocity")
    @Expose
    private double velocity;

    public FinalPanelModel(Point2D location, Dimension2D size) {
        this.id = UUID.randomUUID().toString();
        this.location = location;
        this.size = size;
        this.vertices = new ArrayList<>();

        updateVertices();
        finalPanelModels.add(this);

        createFinalPanelView(id, location, new Dimension((int) size.getWidth(), (int) size.getHeight()));
        collidables.add(this);
    }

    public void setRigid(boolean rigid) {
        isRigid = rigid;
    }

    public boolean isRigid() {
        return isRigid;
    }

    public void setIsometric(boolean isometric) {
        isIsometric = isometric;
    }

    public boolean isIsometric() {
        return isIsometric;
    }

    public void moveLocation(Point2D movement){
        this.location = addVectors(location, movement);
        updateVertices();
    }

    private void moveTopEdge(double shrinkCoefficient){
        Point2D movement = new Point2D.Double(0, shrinkCoefficient);
        this.location = addVectors(location, movement);
        this.size.setSize(size.getWidth(), size.getHeight() - shrinkCoefficient);
        updateVertices();
    }

    private void moveBottomEdge(double shrinkCoefficient){
        this.size.setSize(size.getWidth(), size.getHeight() - shrinkCoefficient);
        updateVertices();
    }

    private void moveLeftEdge(double shrinkCoefficient){
        Point2D movement = new Point2D.Double(shrinkCoefficient, 0);
        this.location = addVectors(location, movement);
        this.size.setSize(size.getWidth() - shrinkCoefficient, size.getHeight());
        updateVertices();
    }

    private void moveRightEdge(double shrinkCoefficient){
        this.size.setSize(size.getWidth() - shrinkCoefficient, size.getHeight());
        updateVertices();
    }

    public Dimension2D getSize() {
        return size;
    }

    public Point2D getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    private void initializeEdges() {
        edges.clear();
        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            edges.add(new Line2D.Double(vertices.get(i), vertices.get(nextIndex)));
        }
    }

    private void updateEdges(){
        edges.clear();
        ArrayList<Line2D> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Point2D start = vertices.get(i);
            Point2D end = vertices.get((i+1) % 4);
            res.add(new Line2D.Double(start, end));
        }
        edges = res;
    }

    public void updateVertices() {
        vertices.clear();
        double x = location.getX();
        double y = location.getY();
        double width = size.getWidth();
        double height = size.getHeight();

        // Top-left corner (location)
        vertices.add(new Point2D.Double(x, y));
        // Top-right corner
        vertices.add(new Point2D.Double(x + width, y));
        // Bottom-right corner
        vertices.add(new Point2D.Double(x + width, y + height));
        // Bottom-left corner
        vertices.add(new Point2D.Double(x, y + height));
        updateEdges();
    }

    public void eliminate(){

    }

    public ArrayList<Point2D> getArrayListVertices() {
        return vertices;
    }

    public Point2D[] getVertices(){
        Point2D[] array = new Point2D[vertices.size()];
        array = vertices.toArray(array);
        return array;
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return edges;
    }

    private void handleCollisionWithBullet(Point2D intersection){
        int index = Utils.findPanelEdgeIndex(getArrayListVertices(), intersection);
        switch (index){
            case 0 -> moveUp = true;
            case 1 -> moveRight = true;
            case 2 -> moveDown = true;
            case 3 -> moveLeft = true;
        }
        acceleration = 0.45; velocity = 0.1;
    }

    private void handleCollisionWithRigidPanel(){

    }

    private void handleCollisionWithNonRigidPanel(){

    }

    public void moveRight(){
        if (vertices.get(1).getX() + 1 < FRAME_DIMENSION.getWidth()) {
            size.setSize(size.getWidth() + 3 * velocity / 4, size.getHeight());
            Point2D movement = new Point2D.Double(velocity/4, 0);
            updateVertices();
            moveLocation(movement);
        } if (velocity < 0) moveRight = false;
    }

    public void moveDown(){
        if (vertices.get(2).getY() < FRAME_DIMENSION.getHeight()) {
            size.setSize(size.getWidth(), size.getHeight() + 3 * velocity / 4);
            Point2D movement = new Point2D.Double(0, velocity/4);
            moveLocation(movement);
            updateVertices();
        } if (velocity < 0) moveDown = false;

    }

    public void moveLeft(){
        if (vertices.get(0).getX() > 0) {
            size.setSize(size.getWidth() + 3 * velocity / 4, size.getHeight());
            Point2D movement = new Point2D.Double(-velocity, 0);
            moveLocation(movement);
            updateVertices();
        } if (velocity<0) moveLeft = false;
    }

    public void moveUp(){
        if (vertices.get(0).getY() > 0) {
            size.setSize(size.getWidth(), size.getHeight() + 3 * velocity / 4);
            Point2D movement = new Point2D.Double(0, -velocity);
            moveLocation(movement);
            updateVertices();
        }  if (velocity<0) moveUp = false;
    }

    public void topShrink(double contraction){
        if (size.getHeight() > 400){
            size.setSize(size.getWidth(), size.getHeight() - contraction);
            Point2D movement = new Point2D.Double(0, contraction);
            moveLocation(movement);
            updateVertices();
        }
    }

    public void bottomShrink(double contraction){
        if (size.getHeight() > 400){
            size.setSize(size.getWidth(), size.getHeight() - contraction);
            updateVertices();
        }
    }

    public void rightShrink(double contraction){
        if (size.getWidth() > 400){
            size.setSize(size.getWidth() - contraction, size.getHeight());
            updateVertices();
        }
    }

    public void leftShrink(double contraction){
        if (size.getWidth() > 400){
            size.setSize(size.getWidth() - contraction, size.getHeight());
            Point2D movement = new Point2D.Double(contraction, 0);
            moveLocation(movement);
            updateVertices();
        }
    }

    public void verticalShrink(double contraction){
        if (size.getHeight() > 400){
            size.setSize(size.getWidth(), size.getHeight() - contraction * 2);
            Point2D movement = new Point2D.Double(0, contraction);
            moveLocation(movement);
            updateVertices();
        }
    }

    public void horizontalShrink(double contraction){
        if (size.getWidth() > 400){
            size.setSize(size.getWidth() - contraction * 2, size.getHeight());
            Point2D movement = new Point2D.Double(contraction, 0);
            moveLocation(movement);
            updateVertices();
        }
    }

    public void panelMotion(){
        updateVertices();
        if (isIsometric) return;
        velocity = acceleration + velocity;

        if (velocity < 4) {
            if (moveRight) moveRight();
            if (moveDown) moveDown();
            if (moveLeft) moveLeft();
            if (moveUp) moveUp();
        }
        if (velocity > 4){
            acceleration = -0.45;
        }

        if (!moveRight&&!moveLeft){
            leftShrink(Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT); // TODO sth is wrong!
            rightShrink(Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT);
        }

        if (!moveDown && !moveUp) {
            topShrink(Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT);
            bottomShrink(Profile.getCurrent().PANEL_SHRINKAGE_COEFFICIENT);
        }
    }

    @Override
    public Point2D getAnchor() {
        return null;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    public void trimRight(double shrinkage){
        size.setSize(size.getWidth() - shrinkage, size.getHeight());
        updateVertices();
    }

    public void trimLeft(double shrinkage){
        size.setSize(size.getWidth() - shrinkage, size.getHeight());
        Point2D movement = new Point2D.Double(shrinkage, 0);
        moveLocation(movement);
        updateVertices();
    }

    public void trimTop(double shrinkage){
        size.setSize(size.getWidth(), size.getHeight() - shrinkage);
        Point2D movement = new Point2D.Double(0, shrinkage);
        moveLocation(movement);
        updateVertices();
    }

    public void trimBottom(double shrinkage){
        size.setSize(size.getWidth(), size.getHeight() - shrinkage);
        updateVertices();
    }

    public void onCollision(Collidable other, Point2D intersection){
        if (other instanceof BulletModel) handleCollisionWithBullet(intersection);
        if (other instanceof FinalPanelModel) handleCollisionWithBullet(intersection);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof FinalPanelModel) handlePanelPanelCollision(this, (FinalPanelModel) other);
    }

    public void setVertices(ArrayList<Point2D> vertices) {
        this.vertices = vertices;
        initializeEdges();
    }

    public ArrayList<Line2D> getUnTrimmedEdges() {
        ArrayList<Line2D> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            res.add(new Line2D.Double(vertices.get(i), vertices.get(nextIndex)));
        }
        return res;
    }

    public void adjustEdgesOnOverlap(FinalPanelModel other) {
        ArrayList<Point2D> thisVertices = this.getArrayListVertices();
        ArrayList<Point2D> otherVertices = other.getArrayListVertices();
        ArrayList<Point2D> verticesInsideOtherPanel = new ArrayList<>();
        ArrayList<Point2D> verticesInsideThisPanel = new ArrayList<>();

        for (Point2D vertex : thisVertices) {
            if (other.isVertexInside(vertex)) {
                verticesInsideOtherPanel.add(vertex);
            }
        }

        for (Point2D vertex : otherVertices) {
            if (this.isVertexInside(vertex)) {
                verticesInsideThisPanel.add(vertex);
            }
        }

        if (verticesInsideOtherPanel.isEmpty() && verticesInsideThisPanel.isEmpty()) {
            return;
        }

        if (verticesInsideOtherPanel.size() == 4) {
            edges.clear();
            return;
        }

        if (verticesInsideThisPanel.size() == 4) {
            other.edges.clear();
            return;
        }

        if (verticesInsideOtherPanel.size() == 2) {
            handleTwoVerticesInside(other, this, verticesInsideOtherPanel);
        }

        if (verticesInsideThisPanel.size() == 2) {
            handleTwoVerticesInside(other, this, verticesInsideThisPanel);
        }

        if (verticesInsideOtherPanel.size() == 1) {
            handleOneVertexInside(this, other, verticesInsideOtherPanel.get(0), verticesInsideThisPanel.isEmpty() ? null : verticesInsideThisPanel.get(0));
        }
        if (verticesInsideThisPanel.size() == 1) {
            handleOneVertexInside(other, this, verticesInsideThisPanel.get(0), verticesInsideOtherPanel.isEmpty() ? null : verticesInsideOtherPanel.get(0));
        }
    }

    private void handleTwoVerticesInside(FinalPanelModel thisPanel, FinalPanelModel otherPanel, ArrayList<Point2D> verticesInside) {
        ArrayList<Point2D> intersections = findIntersections(thisPanel, otherPanel);
        if (intersections == null) return;


        if (intersections.size() != 2) {
            return;
        }

        Point2D i1 = intersections.get(0);
        Point2D i2 = intersections.get(1);
        Point2D v1 = verticesInside.get(0);
        Point2D v2 = verticesInside.get(1);

        ArrayList<Line2D> segments = new ArrayList<>();
        segments.add(new Line2D.Double(i1, i2));

        if (i1.distance(v1) < i1.distance(v2)) {
            segments.add(new Line2D.Double(i1, v1));
            segments.add(new Line2D.Double(i2, v2));
        } else {
            segments.add(new Line2D.Double(i1, v2));
            segments.add(new Line2D.Double(i2, v1));
        }

        segments.add(new Line2D.Double(v1, v2));

        thisPanel.trimPanel(segments);
        otherPanel.trimPanel(segments);

        ArrayList<Line2D> newEdges = new ArrayList<>();
        for (Line2D edge : thisPanel.edges) {
            if (!otherPanel.isEdgeInside(edge)) {
                newEdges.add(edge);
            }
        }
        thisPanel.edges = newEdges;

        ArrayList<Line2D> newEs = new ArrayList<>();
        for (Line2D edge : otherPanel.edges) {
            if (!thisPanel.isEdgeInside(edge)) {
                newEs.add(edge);
            }
        }
        otherPanel.edges = newEs;
    }

    private void handleOneVertexInside(FinalPanelModel thisPanel, FinalPanelModel otherPanel, Point2D thisVertex, Point2D otherVertex) {
        ArrayList<Point2D> intersections = findIntersections(thisPanel, otherPanel);
        if (intersections == null) return;

        if (intersections.size() != 2) {
            return;
        }

        ArrayList<Line2D> segments = new ArrayList<>();
        for (Point2D vertex : intersections) {
            segments.add(new Line2D.Double(vertex, thisVertex));
            if (otherVertex != null) {
                segments.add(new Line2D.Double(vertex, otherVertex));
            }
        }

        thisPanel.trimPanel(segments);
        otherPanel.trimPanel(segments);
    }

    public boolean isEdgeInside(Line2D edge) {
        return isVertexInside(edge.getP1()) && isVertexInside(edge.getP2());
    }

    public static ArrayList<Line2D> trimEdge(Line2D edge, Line2D segment) {
        boolean isEdgeVertical = edge.getX1() == edge.getX2();
        boolean isSegmentVertical = segment.getX1() == segment.getX2();

        if (isEdgeVertical && isSegmentVertical) {
            return trimVerticalSegments(edge, segment);
        } else if (!isEdgeVertical && !isSegmentVertical) {
            return trimHorizontalEdge(edge, segment);
        }

        return null;
    }

    private static ArrayList<Line2D> trimHorizontalEdge(Line2D edge, Line2D segment) {
        ArrayList<Line2D> result = new ArrayList<>();

        if (edge.getY1() != segment.getY1() ) return null;

        double xMinEdge = Math.min(edge.getX1(), edge.getX2());
        double xMaxEdge = Math.max(edge.getX1(), edge.getX2());
        double xMinSegment = Math.min(segment.getX1(), segment.getX2());
        double xMaxSegment = Math.max(segment.getX1(), segment.getX2());

        if (xMinSegment <= xMinEdge && xMaxEdge <= xMaxSegment) {
            result.add(new Line2D.Double(0, 0, 0, 0));
        }
        else if (xMaxSegment < xMinEdge || xMaxEdge < xMinSegment) {
            result.add(edge);
        }
        else if (xMinEdge < xMinSegment && xMaxSegment < xMaxEdge) {
            result.add(new Line2D.Double(xMinEdge, edge.getY1(), xMinSegment, edge.getY1()));
            result.add(new Line2D.Double(xMaxSegment, edge.getY1(), xMaxEdge, edge.getY1()));
        }
        else if (xMaxSegment < xMaxEdge && xMinEdge < xMaxSegment) {
            result.add(new Line2D.Double(xMaxSegment, edge.getY1(), xMaxEdge, edge.getY1()));
        }
        else if (xMaxEdge <= xMaxSegment && xMinSegment < xMaxEdge) {
            result.add(new Line2D.Double(xMinEdge, edge.getY1(), xMinSegment, edge.getY1()));
        }

        return result.isEmpty() ? null : result;
    }

    private static ArrayList<Line2D> trimVerticalSegments(Line2D edge, Line2D segment) {
        ArrayList<Line2D> result = new ArrayList<>();

        if (edge.getX1() != segment.getX1()) return null;

        double yMinEdge = Math.min(edge.getY1(), edge.getY2());
        double yMaxEdge = Math.max(edge.getY1(), edge.getY2());
        double yMinSegment = Math.min(segment.getY1(), segment.getY2());
        double yMaxSegment = Math.max(segment.getY1(), segment.getY2());

        if (yMinSegment <= yMinEdge && yMaxEdge <= yMaxSegment) {
            result.add(new Line2D.Double(0, 0, 0, 0));
        }
        if (yMaxSegment < yMinEdge || yMaxEdge < yMinSegment) {
            result.add(edge);
        }
        if (yMinEdge < yMinSegment && yMaxSegment < yMaxEdge) {
            result.add(new Line2D.Double(edge.getX1(), yMinEdge, edge.getX1(), yMinSegment));
            result.add(new Line2D.Double(edge.getX1(), yMaxSegment, edge.getX1(), yMaxEdge));
        }
        if (yMaxSegment < yMaxEdge && yMinEdge < yMaxSegment) {
            result.add(new Line2D.Double(edge.getX1(), yMaxSegment, edge.getX1(), yMaxEdge));
        }
        if (yMaxEdge <= yMaxSegment && yMinSegment < yMaxEdge) {
            result.add(new Line2D.Double(edge.getX1(), yMinEdge, edge.getX1(), yMinSegment));
        }

        return result.isEmpty() ? null : result;
    }

    public boolean isVertexInside(Point2D vertex) {
        Point2D[] array = getArrayListVertices().toArray(new Point2D[0]);
        return isPointInPolygon(vertex, array);
    }

    void trimPanel(ArrayList<Line2D> segments) {
        ArrayList<Line2D> newList = new ArrayList<>();
        for (Line2D edge : edges) {
            boolean edgeAdded = false;
            for (Line2D segment : segments) {
                ArrayList<Line2D> trimmedEdges = trimEdge(edge, segment);
                if (trimmedEdges != null) {
                    newList.addAll(trimmedEdges);
                    edgeAdded = true;
                    break;
                }
            }
            if (!edgeAdded) {
                newList.add(edge);
            }
        }
        edges = newList;
    }

    public void setEdges(ArrayList<Line2D> edges) {
        this.edges = edges;
    }

    public void setSize(Dimension2D size) {
        this.size = size;
        updateVertices();
    }
}

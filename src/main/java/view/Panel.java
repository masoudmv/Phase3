//package view;
//
////import view.charactersView.EpsilonView;
//
//import model.charactersModel.BulletModel;
//import model.NonRigid;
////import model.charactersModel.Polygon;
//import model.collision.Collidable;
//import model.collision.CollisionState;
//import model.collision.Edge;
//import model.collision.Impactable;
//import model.movement.Direction;
//import model.movement.Movable;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;
//import java.util.ArrayList;
//
//import static controller.constants.Constants.*;
////import static controller.Game.squarantine;
//import static controller.Utils.*;
//import static controller.Utils.relativeLocation;
//
////public final class myPanel extends JPanel  implements Collidable {
//public final class Panel extends JPanel implements Collidable, Impactable, Movable {
//    Point2D anchor;
//    Point2D[] vertices;
//    ArrayList<Edge> edges;
//
//    public NonRigid obj;
//
//    double height = 800;
//    double width = 400;
//    Dimension panelSize = new Dimension((int) width, (int) height);
//    Point2D vertex1;
//    Point2D vertex2;
//    Point2D vertex3;
//    Point2D vertex4 ;
////    Point2D[] vertices = new Point2D[4];
//
////    private static MainPanel INSTANCE;
//    public boolean moveRight = false;
//    public boolean moveDown = false;
//    public boolean moveLeft = false;
//    public boolean moveUp = false;
//    private double velocity = 0;
//    private double acceleration = 0;
////    private final MouseController mouseController;
//
//    public static ArrayList<Panel> panels = new ArrayList<>();
//
//    public Panel() {
//
//
////        INSTANCE = this;
////        mouseController = new MouseController();
//        MainFrame frame = MainFrame.getINSTANCE();
//        setSize(panelSize);
//        setLocationToCenter(frame);
//        vertex1 = new Point2D.Double((double) frame.getWidth() /3- (double) getWidth() /3 - 300, (double) frame.getHeight() / 3- (double) getHeight() /3 - 300);
//        vertex2 = addVectors(vertex1, new Point2D.Double(width, 0));
//        vertex3 = addVectors(vertex2, new Point2D.Double(0, height));
//        vertex4 = addVectors(vertex3, new Point2D.Double(-width, 0));
//        vertices = new Point2D[]{vertex1, vertex2, vertex3, vertex4};
//        setBackground(Color.black);
//        frame.add(this);
//
//
////        System.out.println("Panel:  " + vertex1);
//
////        panels.add(this);
//
//
//        ArrayList<Point2D> points= new ArrayList<>();
//        points.add(vertex1);
//        points.add(vertex2);
//        points.add(vertex3);
//        points.add(vertex4);
//        this.obj = new NonRigid(points);
//
////        collidables.add(this);
//        panels.add(this);
//
//
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D)g;
//        float thickness = 10.0f;
//        g2d.setStroke(new BasicStroke(thickness));
//        g2d.setColor(Color.red);
//        boundaryDrawer(obj.getEdges(), g2d);
//
//    }
//
//
//
//
//    public void setLocationToCenter(MainFrame glassFrame){
//        setLocation(glassFrame.getWidth()/3-getWidth()/3-300,glassFrame.getHeight()/3-getHeight()/3-300);
//    }
//
//    public void adjustLocation(){
//        setLocation((int) vertex1.getX(), (int) vertex1.getY());
//    }
//
//    @Override
//    public boolean isCircular() {
//        return false;
//    }
//
//    @Override
//    public void setDirection(Direction direction) {
//
//    }
//
//    @Override
//    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint) {
//        double minDistance = Double.MAX_VALUE;
//        Point2D closest = null;
//        int edgeNumber = 5;
//        for (int i=0; i<vertices.length; i++){
//
//            Point2D temp = getClosestPointOnSegment(vertices[i],vertices[(i+1)%vertices.length], collisionPoint);
//            double distance = temp.distance(collisionPoint);
//            if (distance < minDistance) {
//                minDistance = distance;
//                edgeNumber = i;
//            }
//        }
//
//        if (edgeNumber==0) moveUp=true;
//        if (edgeNumber==1) moveRight = true;
//        if (edgeNumber==2) moveDown=true;
//        if (edgeNumber==3) moveLeft=true;
//
//
//        acceleration = 0.45; velocity = 0.1;
//    }
//
//
//
//    public void verticalShrink(double contraction){
//        if (height > 300){
////            double contraction = 0.25;
//            this.height -=contraction*2;
//            vertex1 = addVectors(vertex1, new Point2D.Double(0, contraction));
//            vertex2 = addVectors(vertex2, new Point2D.Double(0, contraction));
//            vertex3 = addVectors(vertex3, new Point2D.Double(0, -contraction));
//            vertex4 = addVectors(vertex4, new Point2D.Double(0, -contraction));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//        }
//    }
//
//    public void horizontalShrink(double contraction){
//        if (width > 300){
////            double contraction = 0.25;
//            this.width -=contraction*2;
//            vertex1 = addVectors(vertex1, new Point2D.Double(contraction, 0));
//            vertex2 = addVectors(vertex2, new Point2D.Double(-contraction, 0));
//            vertex3 = addVectors(vertex3, new Point2D.Double(-contraction, 0));
//            vertex4 = addVectors(vertex4, new Point2D.Double(contraction, 0));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//        }
//    }
//
//
//
//    private void updateVertices(){
//        vertices[0] = vertex1; vertices[1] = vertex2; vertices[2] = vertex3; vertices[3] = vertex4;
//    }
//
//    public void moveRight(){
//        if (vertex3.getX()+1<FRAME_DIMENSION.getWidth()) {
//            this.width += 3*velocity/4;
//            vertex1 = addVectors(vertex1, new Point2D.Double(velocity/4, 0));
//            vertex2 = addVectors(vertex2, new Point2D.Double(velocity, 0));
//            vertex3 = addVectors(vertex3, new Point2D.Double(velocity, 0));
//            vertex4 = addVectors(vertex4, new Point2D.Double(velocity/4, 0));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//            MainFrame.getINSTANCE().repaint();
////            MainPanel.getINSTANCE().repaint();
//        } if (velocity<0)moveRight = false;
//    }
//
//    public void moveDown(){
//        if (vertex3.getY()<FRAME_DIMENSION.getHeight()) {
//            this.height += 3*velocity/4;
//            vertex1 = addVectors(vertex1, new Point2D.Double(0, velocity/4));
//            vertex2 = addVectors(vertex2, new Point2D.Double(0, velocity/4));
//            vertex3 = addVectors(vertex3, new Point2D.Double(0, velocity));
//            vertex4 = addVectors(vertex4, new Point2D.Double(0, velocity));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//            MainFrame.getINSTANCE().repaint();
////            MainPanel.getINSTANCE().repaint();
//
//
//        } if (velocity<0) moveDown = false;
//    }
//
//    public void moveLeft(){
//        if (vertex1.getX()>0) {
//            this.width += 3*velocity/4;
//            vertex1 = addVectors(vertex1, new Point2D.Double(-velocity, 0));
//            vertex2 = addVectors(vertex2, new Point2D.Double(-velocity/4, 0));
//            vertex3 = addVectors(vertex3, new Point2D.Double(-velocity/4, 0));
//            vertex4 = addVectors(vertex4, new Point2D.Double(-velocity, 0));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//            MainFrame.getINSTANCE().repaint();
////            MainPanel.getINSTANCE().repaint();
//
//        } if (velocity<0) moveLeft = false;
//    }
//
//    public void moveUp(){
//        if (vertex1.getY()>0) {
//            this.height += 3*velocity/4;
//            vertex1 = addVectors(vertex1, new Point2D.Double(0, -velocity));
//            vertex2 = addVectors(vertex2, new Point2D.Double(0, -velocity));
//            vertex3 = addVectors(vertex3, new Point2D.Double(0, -velocity/4));
//            vertex4 = addVectors(vertex4, new Point2D.Double(0, -velocity/4));
//            updateVertices();
//            adjustLocation();
//            panelSize = new Dimension((int) width, (int) height);
//            setSize(panelSize);
//            MainFrame.getINSTANCE().repaint();
////            MainPanel.getINSTANCE().repaint();
//
//        }  if (velocity<0) moveUp = false;
//    }
//
//
//    public void panelMotion(){
//        setVelocity(getAcceleration() + getVelocity());
//        if (getVelocity()<4) {
//            if (moveRight) moveRight();
//            if (moveDown) moveDown();
//            if (moveLeft) moveLeft();
//            if (moveUp) moveUp();
//        }
//        if (getVelocity() > 4){
//            setAcceleration(-0.45);
//        }
//
//        if (!moveRight&&!moveLeft){
//            // don't change contraction coefficient!
//            horizontalShrink(0.25);
//        }
//
//        if (!moveDown && !moveUp) {
//            // don't change contraction coefficient!
//            verticalShrink(0.25);
//        }
//    }
//
//    public void expansion(){
//        setVelocity(getAcceleration() + getVelocity());
//        if (getVelocity()<4) {
//            if (moveRight) moveRight();
//            if (moveDown) moveDown();
//            if (moveLeft) moveLeft();
//            if (moveUp) moveUp();
//        }
//        if (getVelocity() > 4){
//            setAcceleration(-0.45);
//        }
//    }
//
//
//    @Override
//    public Direction getDirection() {
//        return null;
//    }
//
//    @Override
//    public void move(Direction direction) {
//
//    }
//
//    @Override
//    public void move() {
//
//    }
//
//    @Override
//    public void friction() {
//
//    }
//
//    @Override
//    public double getRadius() {
//        return 0;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public Point2D getAnchor() {
//        return new Point2D.Double((double) MainFrame.getINSTANCE().getHeight() /2,  (double) MainFrame.getINSTANCE().getWidth() /2);
//    }
//
//
//    @Override
//    public Point2D[] getVertices() {
//        return vertices;
//    }
//
//    @Override
//    public void onCollision(Collidable other, Point2D intersection) {
//
//    }
//
//
//    @Override
//    public boolean isImpactInProgress() {
//        return false;
//    }
//
//    @Override
//    public void setImpactInProgress(boolean impactInProgress) {
//
//    }
//
//    @Override
//    public void impact(CollisionState collisionState) {
//
//    }
//
//
//
//    @Override
//    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
//
//    }
//
//    @Override
//    public double getImpactCoefficient(Point2D collisionRelativeVector) {
//        return 0;
//    }
//
//    @Override
//    public void banish() {
//
//    }
//
//
//    public double getVelocity() {
//        return this.velocity;
//    }
//
//    public double getAcceleration() {
//        return this.acceleration;
//    }
//
//
//    public void setVelocity(double velocity) {
//        this.velocity = velocity;
//    }
//
//    public void setAcceleration(double acceleration) {
//        this.acceleration = acceleration;
//    }
//
////    public MouseController getMouseController() {
////        return mouseController;
////    }
//    public static void nullifyMainPanel(){
//        INSTANCE = null;
//    }
//
//    private void boundaryDrawer(ArrayList<Line2D> lines, Graphics2D graphics2D){
//        for (Line2D line2D : lines){
//            Point corner = new Point(this.getX(), this.getY());
//            Point2D head1 = relativeLocation(line2D.getP1(), corner);
//            Point2D head2 = relativeLocation(line2D.getP2(), corner);
//
//            graphics2D.drawLine((int) head1.getX(), (int) head1.getY(), (int) head2.getX(), (int) head2.getY());
//        }
//    }
//}
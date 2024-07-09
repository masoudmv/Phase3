package model;

import view.MainPanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import static controller.Utils.*;
//import static model.Panel.panels;

public class NonRigid {
    private Rectangle2D rectangle;
    private ArrayList<Point2D> vertices;
    private ArrayList<Line2D> edges;
    public static ArrayList<NonRigid> nonRigids = new ArrayList<>();

    public NonRigid(ArrayList<Point2D> vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        initializeEdges();
        nonRigids.add(this);
        double width = vertices.get(1).getX() - vertices.get(0).getX();
        double height = vertices.get(3).getY() - vertices.get(0).getY();
        rectangle = new Rectangle2D.Double(vertices.get(0).getX(), vertices.get(0).getY(), width, height);
    }

    public void setVertices(ArrayList<Point2D> vertices) {
        this.vertices = vertices;
        initializeEdges();
    }

    private void initializeEdges() {
        edges.clear();
        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            edges.add(new Line2D.Double(vertices.get(i), vertices.get(nextIndex)));
        }
    }

    public ArrayList<Line2D> getUnTrimmedEdges() {
        ArrayList<Line2D> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            res.add(new Line2D.Double(vertices.get(i), vertices.get(nextIndex)));
        }
        return res;
    }

    public boolean overlapsWith(NonRigid panel) {
        return doAABBsIntersect(this.rectangle, panel.getRectangle());
    }

    public void adjustEdgesOnOverlap(NonRigid other) {
        ArrayList<Point2D> thisVertices = this.getVertices();
        ArrayList<Point2D> otherVertices = other.getVertices();
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
            initializeEdges();
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
            handleTwoVerticesInside(this, other, verticesInsideOtherPanel);
        }

        if (verticesInsideThisPanel.size() == 2) {
            handleTwoVerticesInside(this, other, verticesInsideThisPanel);
        }

        if (verticesInsideOtherPanel.size() == 1) {
            handleOneVertexInside(this, other, verticesInsideOtherPanel.get(0), verticesInsideThisPanel.isEmpty() ? null : verticesInsideThisPanel.get(0));
        }

        if (verticesInsideThisPanel.size() == 1) {
            handleOneVertexInside(other, this, verticesInsideThisPanel.get(0), verticesInsideOtherPanel.isEmpty() ? null : verticesInsideOtherPanel.get(0));
        }
    }

    private void handleTwoVerticesInside(NonRigid thisPanel, NonRigid otherPanel, ArrayList<Point2D> verticesInside) {
        ArrayList<Point2D> intersections = findIntersections(thisPanel, otherPanel);

        if (intersections.size() != 2) {
            return; // Error: we expect exactly two intersections.
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

        // Remove edges that are completely inside the other panel
        ArrayList<Line2D> newEdges = new ArrayList<>();
        for (Line2D edge : thisPanel.edges) {
            if (!otherPanel.isEdgeInside(edge)) {
                newEdges.add(edge);
            }
        }
        thisPanel.edges = newEdges;
    }

    private void handleOneVertexInside(NonRigid thisPanel, NonRigid otherPanel, Point2D thisVertex, Point2D otherVertex) {
        ArrayList<Point2D> intersections = findIntersections(thisPanel, otherPanel);

        if (intersections.size() != 2) {
            return; // Error: we expect exactly two intersections.
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
            return trimVerticalSegments(edge, segment); // Both lines are vertical
        } else if (!isEdgeVertical && !isSegmentVertical) {
            return trimHorizontalEdge(edge, segment); // Both lines are horizontal
        }

        return null; // Lines are not both vertical or both horizontal, return null
    }

    private static ArrayList<Line2D> trimHorizontalEdge(Line2D edge, Line2D segment) {
        ArrayList<Line2D> result = new ArrayList<>();

        if (edge.getY1() != segment.getY1()) return null; // Different y-coordinates, no overlap

        double xMinEdge = Math.min(edge.getX1(), edge.getX2());
        double xMaxEdge = Math.max(edge.getX1(), edge.getX2());
        double xMinSegment = Math.min(segment.getX1(), segment.getX2());
        double xMaxSegment = Math.max(segment.getX1(), segment.getX2());

        if (xMinSegment <= xMinEdge && xMaxEdge <= xMaxSegment) {
            return null; // Every part of the edge is removed
        } else if (xMaxSegment <= xMinEdge || xMaxEdge <= xMinSegment) {
            result.add(edge); // Segments do not overlap
        } else if (xMinEdge < xMinSegment && xMaxSegment < xMaxEdge) {
            // Trimmed edge has two parts: left part and right part
            result.add(new Line2D.Double(xMinEdge, edge.getY1(), xMinSegment, edge.getY1()));
            result.add(new Line2D.Double(xMaxSegment, edge.getY1(), xMaxEdge, edge.getY1()));
        } else if (xMaxSegment < xMaxEdge) {
            // Right side of edge remains
            result.add(new Line2D.Double(xMaxSegment, segment.getY1(), xMaxEdge, edge.getY1()));
        } else if (xMaxEdge < xMaxSegment) {
            // Left side of edge remains
            result.add(new Line2D.Double(xMinEdge, edge.getY1(), xMinSegment, segment.getY1()));
        }

        return result.isEmpty() ? null : result;
    }

    private static ArrayList<Line2D> trimVerticalSegments(Line2D edge, Line2D segment) {
        ArrayList<Line2D> result = new ArrayList<>();

        if (edge.getX1() != segment.getX1()) return null; // Different x-coordinates, no overlap

        double yMinEdge = Math.min(edge.getY1(), edge.getY2());
        double yMaxEdge = Math.max(edge.getY1(), edge.getY2());
        double yMinSegment = Math.min(segment.getY1(), segment.getY2());
        double yMaxSegment = Math.max(segment.getY1(), segment.getY2());

        if (yMinSegment <= yMinEdge && yMaxEdge <= yMaxSegment) {
            return null; // Every part of the edge is removed
        } else if (yMaxSegment <= yMinEdge || yMaxEdge <= yMinSegment) {
            result.add(edge); // Segments do not overlap
        } else if (yMinEdge < yMinSegment && yMaxSegment < yMaxEdge) {
            // Trimmed edge has two parts: upper part and lower part
            result.add(new Line2D.Double(edge.getX1(), yMinEdge, edge.getX1(), yMinSegment));
            result.add(new Line2D.Double(edge.getX1(), yMaxSegment, edge.getX1(), yMaxEdge));
        } else if (yMaxSegment < yMaxEdge) {
            // Upper side of edge remains
            result.add(new Line2D.Double(edge.getX1(), yMaxSegment, edge.getX1(), yMaxEdge));
        } else if (yMaxEdge < yMaxSegment) {
            // Lower side of edge remains
            result.add(new Line2D.Double(edge.getX1(), yMinEdge, edge.getX1(), yMinSegment));
        }

        return result.isEmpty() ? null : result;
    }

    public boolean isVertexInside(Point2D vertex) {
        Point2D[] array = getVertices().toArray(new Point2D[0]);
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

    public ArrayList<Line2D> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Line2D> edges) {
        this.edges = edges;
    }

    public ArrayList<Point2D> getVertices() {
        return vertices;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }
}

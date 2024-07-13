package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.imagetools.ToolBox.getBufferedImage;

public class BarricadosModel extends GeoShapeModel{
    static BufferedImage image;
    private final static Dimension panelSize = new Dimension(400, 400);

    public static ArrayList<BarricadosModel> barricados = new ArrayList<>();

    public BarricadosModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        Point2D location = new Point2D.Double(anchor.getX()-200, anchor.getY()-200);
        FinalPanelModel f = new FinalPanelModel(location, panelSize);
        f.setRigid(false);
        f.setIsometric(true);
        barricados.add(this);
    }


    public static BufferedImage loadImage(){
        Image img = new ImageIcon("./src/barricados.png").getImage();
        BarricadosModel.image = getBufferedImage(img);
        return BarricadosModel.image;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    @Override
    public void eliminate() {

    }
}

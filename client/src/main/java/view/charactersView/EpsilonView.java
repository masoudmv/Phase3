package view.charactersView;

import model.MyPolygon;
import model.entities.Profile;
import view.FinalPanelView;

import java.awt.*;

import static model.imagetools.ToolBox.rotateImage;

public class EpsilonView extends GeoShapeView{
    private double radius;


    public EpsilonView(String id, Image image) {
        super(id, image);
        this.radius = Profile.getCurrent().EPSILON_RADIUS;

    }

    //
    @Override
    public void draw(Graphics g, String panelID){
//        this.radius = Profile.getCurrent().EPSILON_RADIUS;


        // tofo epsilon polymorpph ability
        Graphics2D g2d = (Graphics2D) g;


        if (locations.get(panelID) == null) return;
        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();


        g2d.drawImage(image, (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);

//        g2d.setColor(Color.red);
//        g2d.drawOval( (x - 20), (y - 20), 40, 40);

    }


}
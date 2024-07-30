package view.charactersView;

import model.entities.Profile;
import java.awt.*;

import static model.imagetools.ToolBox.rotateImage;

public class EpsilonView extends GeoShapeView{
    private double radius;


    public EpsilonView(String id, Image image) {
        super(id, image);
        this.radius = Profile.getCurrent().EPSILON_RADIUS;
    }

//
//    @Override
//    public void draw(Graphics g, String panelID) {
//
//        int x = (int) locations.get(panelID).getX();
//        int y = (int) locations.get(panelID).getY();
//
//
//        g.drawImage(rotateImage(image, angle), x, y, null);
//
//
//        // TODO epsilon polymorph ability ...
//
//    }


}
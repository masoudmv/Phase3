package model.charactersModel;

import model.FinalPanelModel;
import org.example.Main;
import view.MainFrame;

import java.awt.geom.Point2D;
import java.util.Random;

public class EnemyFactory {

    private static final double offset = 100;
    private static final Random random = new Random();
    private final MainFrame frame = MainFrame.getINSTANCE();
    private final double width = frame.getWidth();
    private final double height = frame.getHeight();

    public void createNecropick(){
        new NecropickModel();
    }


    public Point2D findRandPosition(){
        int index = random.nextInt(4);
        double x = -offset;
        double y = -offset;
        switch (index){
            case 0:
                y = -offset;
                x = random.nextDouble(0, width);
            case 1:
                x = width + offset;
                y = random.nextDouble(0, height);

            case 2:
                y = height + offset;
                x = random.nextDouble(0, width);
            case 3:
                x = -offset;
                y = random.nextDouble(0, height);
        }

        return new Point2D.Double(x, y);

    }
}

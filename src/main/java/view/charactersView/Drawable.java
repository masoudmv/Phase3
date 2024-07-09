package view.charactersView;

import java.awt.*;
import java.util.ArrayList;

public interface Drawable {
    ArrayList<Drawable> drawables = new ArrayList<>();
    void draw(Graphics g);
}

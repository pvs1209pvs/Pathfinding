package sample;

import javafx.scene.paint.Color;

import java.awt.*;

public class Marker {

    private Point position;
    private boolean isSet;
    private final Color color;

    public Marker(Color colors) {
        this.position = new Point(-1, -1);
        this.isSet = false;
        this.color = colors;
    }

    void unSet(){
        this.position = new Point(-1, -1);
        this.isSet = false;
    }

    public void setPosition(Point position) {
        this.position = position;
        this.isSet = true;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isSet() {
        return isSet;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "position=" + position +
                ", isSet=" + isSet +
                ", color=" + color +
                '}';
    }
}

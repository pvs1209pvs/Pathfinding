package sample;

import javafx.scene.paint.Color;

import java.awt.*;

public class Marker {

    private Point position;
    private boolean isEnabled;
    private final Color color;

    Marker(Color colors) {
        this.position = new Point(-1, -1);
        this.isEnabled = false;
        this.color = colors;
    }

    void disable() {
        this.position = new Point(-1, -1);
        this.isEnabled = false;
    }

    void setPosition(Point position) {
        this.position = position;
        this.isEnabled = true;
    }

    void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    Point getPosition() {
        return position;
    }

    boolean getEnabled() {
        return isEnabled;
    }

    Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "position=" + position +
                ", isSet=" + isEnabled +
                ", color=" + color +
                '}';
    }
}

package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 * Represents a rotating rectangular hazard.
 *
 * Collision detection uses the actual rotated rectangle shape instead of a
 * square bounding box, so the player only dies when touching the visible hazard.
 */
public class SpinningRectangle implements IntersectionDetectable {
    private final double centerX;
    private final double centerY;
    private final int width;
    private final int height;
    private final double rotationSpeed;
    private double rotation;

    public SpinningRectangle(double centerX, double centerY, int width, int height, double rotationSpeed) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
        this.rotationSpeed = rotationSpeed;
    }

    public void move() {
        rotation = (rotation + rotationSpeed) % 360;
    }

    public void paint(Graphics brush) {
        Graphics2D g2 = (Graphics2D) brush;
        AffineTransform originalTransform = g2.getTransform();

        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(rotation));
        g2.setColor(new Color(236, 87, 186));
        g2.fillRect(-width / 2, -height / 2, width, height);
        g2.setColor(new Color(130, 20, 95));
        g2.drawRect(-width / 2, -height / 2, width, height);

        g2.setTransform(originalTransform);
    }

    @Override
    public boolean intersects(Player player) {
        Area hazardArea = new Area(getRotatedShape());
        hazardArea.intersect(new Area(player.getBounds()));
        return !hazardArea.isEmpty();
    }

    private Shape getRotatedShape() {
        Rectangle baseRectangle = new Rectangle(-width / 2, -height / 2, width, height);
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY);
        transform.rotate(Math.toRadians(rotation));
        return transform.createTransformedShape(baseRectangle);
    }
}

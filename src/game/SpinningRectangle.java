package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * Represents a rotating rectangular hazard.
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

    /**
     * Backward-compatible constructor for older project code.
     */
    public SpinningRectangle(Point initialPosition, double initialRotation) {
        this(initialPosition.getX(), initialPosition.getY(), 70, 18, 2.0);
        this.rotation = initialRotation;
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
        // Approximation: use a bounding rectangle around the rotating hazard.
        // This keeps gameplay responsive while avoiding heavy geometry code.
        int boundingSize = Math.max(width, height);
        Rectangle hazardBounds = new Rectangle(
                (int) centerX - boundingSize / 2,
                (int) centerY - boundingSize / 2,
                boundingSize,
                boundingSize
        );

        return hazardBounds.intersects(player.getBounds());
    }
}

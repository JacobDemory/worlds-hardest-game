package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents a circular enemy that patrols horizontally or vertically.
 */
public class Enemy implements IntersectionDetectable {
    public enum Axis {
        HORIZONTAL,
        VERTICAL
    }

    private double x;
    private double y;
    private final int radius;
    private final double speed;
    private final Axis axis;
    private final double min;
    private final double max;
    private int direction = 1;

    public Enemy(double x, double y, int radius, double speed, Axis axis, double min, double max) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.axis = axis;
        this.min = min;
        this.max = max;
    }

    /**
     * Backward-compatible constructor for older vertical enemy definitions.
     */
    public Enemy(double x, double y, int radius, double speed) {
        this(x, y, radius, speed, Axis.VERTICAL, WorldsHardestGame.BOARD_Y + 20,
                WorldsHardestGame.BOARD_Y + WorldsHardestGame.BOARD_HEIGHT - 20);
    }

    public void move() {
        if (axis == Axis.HORIZONTAL) {
            x += speed * direction;
            if (x <= min || x >= max) {
                x = Math.max(min, Math.min(max, x));
                direction *= -1;
            }
        } else {
            y += speed * direction;
            if (y <= min || y >= max) {
                y = Math.max(min, Math.min(max, y));
                direction *= -1;
            }
        }
    }

    public void paint(Graphics brush) {
        int diameter = radius * 2;
        brush.setColor(new Color(34, 85, 255));
        brush.fillOval((int) (x - radius), (int) (y - radius), diameter, diameter);
        brush.setColor(new Color(10, 30, 150));
        brush.drawOval((int) (x - radius), (int) (y - radius), diameter, diameter);
    }

    @Override
    public boolean intersects(Player player) {
        Rectangle rect = player.getBounds();

        double nearestX = Math.max(rect.x, Math.min(x, rect.x + rect.width));
        double nearestY = Math.max(rect.y, Math.min(y, rect.y + rect.height));

        double dx = x - nearestX;
        double dy = y - nearestY;

        return (dx * dx + dy * dy) <= radius * radius;
    }
}

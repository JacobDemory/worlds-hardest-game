package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents the player-controlled red square.
 */
public class Player {
    public static final int SIZE = 20;

    private double x;
    private double y;
    private double startX;
    private double startY;
    private final double speed = 4.5;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    public Player(double startX, double startY) {
        setStartPosition(startX, startY);
        reset();
    }

    public void setStartPosition(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void reset() {
        this.x = startX;
        this.y = startY;
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }

    public void move(Rectangle bounds) {
        double nextX = x;
        double nextY = y;

        if (movingUp) {
            nextY -= speed;
        }
        if (movingDown) {
            nextY += speed;
        }
        if (movingLeft) {
            nextX -= speed;
        }
        if (movingRight) {
            nextX += speed;
        }

        x = clamp(nextX, bounds.x, bounds.x + bounds.width - SIZE);
        y = clamp(nextY, bounds.y, bounds.y + bounds.height - SIZE);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void paint(Graphics brush) {
        brush.setColor(new Color(224, 32, 32));
        brush.fillRect((int) x, (int) y, SIZE, SIZE);
        brush.setColor(new Color(120, 0, 0));
        brush.drawRect((int) x, (int) y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, SIZE, SIZE);
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}

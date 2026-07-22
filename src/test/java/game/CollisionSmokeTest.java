package game;

import java.awt.Rectangle;

/**
 * Dependency-free smoke tests for collision and player-boundary behavior.
 */
public final class CollisionSmokeTest {
    private CollisionSmokeTest() {
    }

    public static void main(String[] args) {
        detectsCircleCollision();
        rejectsDistantCircle();
        detectsRotatedRectangleCollision();
        rejectsDistantRotatedRectangle();
        clampsPlayerToPlayArea();
        System.out.println("All collision smoke tests passed.");
    }

    private static void detectsCircleCollision() {
        Player player = new Player(100, 100);
        Enemy enemy = new Enemy(110, 110, 8, 0, Enemy.Axis.HORIZONTAL, 110, 110);
        assert enemy.intersects(player) : "Expected the enemy to intersect the player.";
    }

    private static void rejectsDistantCircle() {
        Player player = new Player(100, 100);
        Enemy enemy = new Enemy(200, 200, 8, 0, Enemy.Axis.HORIZONTAL, 200, 200);
        assert !enemy.intersects(player) : "A distant enemy should not intersect the player.";
    }

    private static void detectsRotatedRectangleCollision() {
        Player player = new Player(100, 100);
        SpinningRectangle hazard = new SpinningRectangle(110, 110, 70, 18, 0);
        assert hazard.intersects(player) : "Expected the rotating hazard to intersect the player.";
    }

    private static void rejectsDistantRotatedRectangle() {
        Player player = new Player(100, 100);
        SpinningRectangle hazard = new SpinningRectangle(250, 250, 70, 18, 0);
        assert !hazard.intersects(player) : "A distant rotating hazard should not intersect the player.";
    }

    private static void clampsPlayerToPlayArea() {
        Player player = new Player(100, 100);
        Rectangle bounds = new Rectangle(100, 100, 100, 100);
        player.setMovingLeft(true);
        player.setMovingUp(true);
        player.move(bounds);
        assert player.getBounds().x == 100 : "Player moved past the left boundary.";
        assert player.getBounds().y == 100 : "Player moved past the top boundary.";
    }
}

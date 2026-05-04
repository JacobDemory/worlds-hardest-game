package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game class for a Java desktop game inspired by The World's Hardest Game.
 */
public class WorldsHardestGame extends Game implements KeyListener {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int BOARD_X = 120;
    public static final int BOARD_Y = 130;
    public static final int BOARD_WIDTH = 560;
    public static final int BOARD_HEIGHT = 360;

    // Backward-compatible aliases used by older helper classes.
    public static final int checkeredX = BOARD_X;
    public static final int checkeredY = BOARD_Y;
    public static final int checkeredSize = BOARD_HEIGHT;

    private static final Rectangle PLAY_AREA = new Rectangle(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
    private static final Rectangle START_ZONE = new Rectangle(BOARD_X, BOARD_Y, 90, BOARD_HEIGHT);
    private static final Rectangle GOAL_ZONE = new Rectangle(BOARD_X + BOARD_WIDTH - 90, BOARD_Y, 90, BOARD_HEIGHT);

    private final Player player = new Player(BOARD_X + 35, BOARD_Y + BOARD_HEIGHT / 2.0 - Player.SIZE / 2.0);
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<SpinningRectangle> spinningHazards = new ArrayList<>();
    private final List<Rectangle> coins = new ArrayList<>();

    private boolean started = false;
    private boolean won = false;
    private int deaths = 0;
    private int coinsCollected = 0;
    private long startTime;
    private long finishTime;

    public WorldsHardestGame() {
        super("World's Hardest Game - Java", WINDOW_WIDTH, WINDOW_HEIGHT);
        addKeyListener(this);
        setupLevel();
    }

    private void setupLevel() {
        enemies.clear();
        spinningHazards.clear();
        coins.clear();

        // Vertical patrols through the main lane.
        enemies.add(new Enemy(250, BOARD_Y + 55, 12, 3.6, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        enemies.add(new Enemy(310, BOARD_Y + BOARD_HEIGHT - 55, 12, 4.0, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        enemies.add(new Enemy(370, BOARD_Y + 55, 12, 4.4, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        enemies.add(new Enemy(430, BOARD_Y + BOARD_HEIGHT - 55, 12, 4.0, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        enemies.add(new Enemy(490, BOARD_Y + 55, 12, 3.6, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));

        // Horizontal patrols for variety.
        enemies.add(new Enemy(BOARD_X + 230, BOARD_Y + 95, 10, 3.2, Enemy.Axis.HORIZONTAL, BOARD_X + 210, BOARD_X + BOARD_WIDTH - 210));
        enemies.add(new Enemy(BOARD_X + BOARD_WIDTH - 230, BOARD_Y + 265, 10, 3.2, Enemy.Axis.HORIZONTAL, BOARD_X + 210, BOARD_X + BOARD_WIDTH - 210));

        spinningHazards.add(new SpinningRectangle(BOARD_X + 210, BOARD_Y + 180, 80, 18, 2.5));
        spinningHazards.add(new SpinningRectangle(BOARD_X + 350, BOARD_Y + 180, 80, 18, -2.5));

        coins.add(new Rectangle(BOARD_X + 245, BOARD_Y + 55, 14, 14));
        coins.add(new Rectangle(BOARD_X + 335, BOARD_Y + 310, 14, 14));
        coins.add(new Rectangle(BOARD_X + 455, BOARD_Y + 55, 14, 14));
    }

    private void resetGame() {
        started = true;
        won = false;
        deaths = 0;
        coinsCollected = 0;
        startTime = System.currentTimeMillis();
        finishTime = 0;
        player.reset();
        setupLevel();
    }

    private void respawnPlayer() {
        deaths++;
        player.reset();
    }

    private void updateGame() {
        if (!started || won) {
            return;
        }

        player.move(PLAY_AREA);

        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.intersects(player)) {
                respawnPlayer();
                return;
            }
        }

        for (SpinningRectangle hazard : spinningHazards) {
            hazard.move();
            if (hazard.intersects(player)) {
                respawnPlayer();
                return;
            }
        }

        collectCoins();

        if (GOAL_ZONE.contains(player.getBounds()) && coinsCollected == 3) {
            won = true;
            finishTime = System.currentTimeMillis();
        }
    }

    private void collectCoins() {
        Rectangle playerBounds = player.getBounds();

        for (int i = coins.size() - 1; i >= 0; i--) {
            if (playerBounds.intersects(coins.get(i))) {
                coins.remove(i);
                coinsCollected++;
            }
        }
    }

    @Override
    public void paint(Graphics brush) {
        updateGame();

        Graphics2D g = (Graphics2D) brush;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g);
        drawHud(g);
        drawBoard(g);

        if (!started) {
            drawStartOverlay(g);
        } else if (won) {
            drawWinOverlay(g);
        }
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(new Color(22, 24, 50));
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(36, 41, 89));
        for (int y = 0; y < height; y += 40) {
            g.drawLine(0, y, width, y);
        }
        for (int x = 0; x < width; x += 40) {
            g.drawLine(x, 0, x, height);
        }
    }

    private void drawHud(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("World's Hardest Game", 32, 45);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Deaths: " + deaths, 32, 75);
        g.drawString("Time: " + formatElapsedTime(), 130, 75);
        g.drawString("Coins: " + coinsCollected + "/3", 250, 75);
        g.drawString("Move: Arrow Keys   Restart: R", 520, 75);
    }

    private void drawBoard(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(BOARD_X - 5, BOARD_Y - 5, BOARD_WIDTH + 10, BOARD_HEIGHT + 10);

        g.setColor(new Color(238, 238, 255));
        g.fillRect(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);

        drawGrid(g);
        drawZones(g);
        drawCoins(g);

        for (Enemy enemy : enemies) {
            enemy.paint(g);
        }

        for (SpinningRectangle hazard : spinningHazards) {
            hazard.paint(g);
        }

        player.paint(g);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawRect(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(new Color(205, 205, 235));
        for (int x = BOARD_X; x <= BOARD_X + BOARD_WIDTH; x += 40) {
            g.drawLine(x, BOARD_Y, x, BOARD_Y + BOARD_HEIGHT);
        }
        for (int y = BOARD_Y; y <= BOARD_Y + BOARD_HEIGHT; y += 40) {
            g.drawLine(BOARD_X, y, BOARD_X + BOARD_WIDTH, y);
        }
    }

    private void drawZones(Graphics2D g) {
        g.setColor(new Color(135, 232, 135));
        g.fill(START_ZONE);
        g.fill(GOAL_ZONE);

        g.setColor(new Color(25, 135, 55));
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("START", START_ZONE.x + 18, START_ZONE.y + 30);
        g.drawString("GOAL", GOAL_ZONE.x + 24, GOAL_ZONE.y + 30);
    }

    private void drawCoins(Graphics2D g) {
        for (Rectangle coin : coins) {
            g.setColor(new Color(255, 208, 45));
            g.fillOval(coin.x, coin.y, coin.width, coin.height);
            g.setColor(new Color(145, 95, 0));
            g.drawOval(coin.x, coin.y, coin.width, coin.height);
        }
    }

    private void drawStartOverlay(Graphics2D g) {
        drawOverlay(g, "Press ENTER to Start", "Collect all coins, avoid enemies, and reach the goal.");
    }

    private void drawWinOverlay(Graphics2D g) {
        drawOverlay(g, "You Win!", "Deaths: " + deaths + "   Time: " + formatElapsedTime() + "   Press R to restart.");
    }

    private void drawOverlay(Graphics2D g, String title, String subtitle) {
        g.setColor(new Color(0, 0, 0, 165));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - titleWidth) / 2, height / 2 - 25);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, (width - subtitleWidth) / 2, height / 2 + 15);
    }

    private String formatElapsedTime() {
        long end = won ? finishTime : System.currentTimeMillis();
        if (!started || startTime == 0) {
            return "0.0s";
        }
        double seconds = (end - startTime) / 1000.0;
        return String.format("%.1fs", seconds);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ENTER && !started) {
            resetGame();
            return;
        }

        if (key == KeyEvent.VK_R) {
            resetGame();
            return;
        }

        if (!started || won) {
            return;
        }

        switch (key) {
            case KeyEvent.VK_UP -> player.setMovingUp(true);
            case KeyEvent.VK_DOWN -> player.setMovingDown(true);
            case KeyEvent.VK_LEFT -> player.setMovingLeft(true);
            case KeyEvent.VK_RIGHT -> player.setMovingRight(true);
            default -> { }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> player.setMovingUp(false);
            case KeyEvent.VK_DOWN -> player.setMovingDown(false);
            case KeyEvent.VK_LEFT -> player.setMovingLeft(false);
            case KeyEvent.VK_RIGHT -> player.setMovingRight(false);
            default -> { }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used.
    }

    private void runGameLoop() {
        while (true) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void main(String[] args) {
        WorldsHardestGame game = new WorldsHardestGame();
        game.runGameLoop();
    }
}

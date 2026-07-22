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
    public static final int WINDOW_WIDTH = 860;
    public static final int WINDOW_HEIGHT = 640;
    public static final int BOARD_X = 110;
    public static final int BOARD_Y = 130;
    public static final int BOARD_WIDTH = 640;
    public static final int BOARD_HEIGHT = 390;

    // Backward-compatible aliases used by older helper classes.
    public static final int checkeredX = BOARD_X;
    public static final int checkeredY = BOARD_Y;
    public static final int checkeredSize = BOARD_HEIGHT;

    private static final Rectangle PLAY_AREA = new Rectangle(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
    private static final Rectangle START_ZONE = new Rectangle(BOARD_X, BOARD_Y, 90, BOARD_HEIGHT);
    private static final Rectangle GOAL_ZONE = new Rectangle(BOARD_X + BOARD_WIDTH - 90, BOARD_Y, 90, BOARD_HEIGHT);

    private final Player player = new Player(START_ZONE.x + 35, START_ZONE.y + BOARD_HEIGHT / 2.0 - Player.SIZE / 2.0);
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<SpinningRectangle> spinningHazards = new ArrayList<>();
    private final List<Rectangle> coins = new ArrayList<>();
    private final List<LevelConfig> levels = new ArrayList<>();

    private boolean started = false;
    private boolean won = false;
    private boolean campaignComplete = false;
    private int levelIndex = 0;
    private int deaths = 0;
    private int totalDeaths = 0;
    private int coinsCollected = 0;
    private long campaignStartTime;
    private long levelStartTime;
    private long levelFinishTime;
    private long campaignFinishTime;

    public WorldsHardestGame() {
        super("World's Hardest Game - Java", WINDOW_WIDTH, WINDOW_HEIGHT);
        addKeyListener(this);
        buildLevels();
        setupLevel();
    }

    private void buildLevels() {
        levels.clear();

        LevelConfig levelOne = new LevelConfig("Level 1: The Warmup", "Classic vertical patrols with a few coins.");
        levelOne.enemies.add(new Enemy(260, BOARD_Y + 55, 12, 3.6, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelOne.enemies.add(new Enemy(325, BOARD_Y + BOARD_HEIGHT - 55, 12, 4.0, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelOne.enemies.add(new Enemy(390, BOARD_Y + 55, 12, 4.4, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelOne.enemies.add(new Enemy(455, BOARD_Y + BOARD_HEIGHT - 55, 12, 4.0, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelOne.enemies.add(new Enemy(520, BOARD_Y + 55, 12, 3.6, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelOne.coins.add(new Rectangle(BOARD_X + 250, BOARD_Y + 55, 14, 14));
        levelOne.coins.add(new Rectangle(BOARD_X + 345, BOARD_Y + 320, 14, 14));
        levelOne.coins.add(new Rectangle(BOARD_X + 475, BOARD_Y + 55, 14, 14));
        levels.add(levelOne);

        LevelConfig levelTwo = new LevelConfig("Level 2: Cross Traffic", "Horizontal and vertical enemies overlap in the center.");
        levelTwo.enemies.add(new Enemy(255, BOARD_Y + 55, 12, 4.2, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelTwo.enemies.add(new Enemy(390, BOARD_Y + BOARD_HEIGHT - 55, 12, 4.5, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelTwo.enemies.add(new Enemy(525, BOARD_Y + 55, 12, 4.2, Enemy.Axis.VERTICAL, BOARD_Y + 30, BOARD_Y + BOARD_HEIGHT - 30));
        levelTwo.enemies.add(new Enemy(BOARD_X + 220, BOARD_Y + 120, 11, 4.1, Enemy.Axis.HORIZONTAL, BOARD_X + 200, BOARD_X + BOARD_WIDTH - 200));
        levelTwo.enemies.add(new Enemy(BOARD_X + BOARD_WIDTH - 220, BOARD_Y + 250, 11, 4.1, Enemy.Axis.HORIZONTAL, BOARD_X + 200, BOARD_X + BOARD_WIDTH - 200));
        levelTwo.spinningHazards.add(new SpinningRectangle(BOARD_X + 325, BOARD_Y + 195, 85, 18, 2.2));
        levelTwo.coins.add(new Rectangle(BOARD_X + 225, BOARD_Y + 325, 14, 14));
        levelTwo.coins.add(new Rectangle(BOARD_X + 385, BOARD_Y + 70, 14, 14));
        levelTwo.coins.add(new Rectangle(BOARD_X + 535, BOARD_Y + 325, 14, 14));
        levels.add(levelTwo);

        LevelConfig levelThree = new LevelConfig("Level 3: Spin Cycle", "Time each move carefully through the rotating hazards.");
        levelThree.enemies.add(new Enemy(255, BOARD_Y + 60, 11, 4.8, Enemy.Axis.VERTICAL, BOARD_Y + 25, BOARD_Y + BOARD_HEIGHT - 25));
        levelThree.enemies.add(new Enemy(360, BOARD_Y + BOARD_HEIGHT - 60, 11, 5.0, Enemy.Axis.VERTICAL, BOARD_Y + 25, BOARD_Y + BOARD_HEIGHT - 25));
        levelThree.enemies.add(new Enemy(465, BOARD_Y + 60, 11, 4.8, Enemy.Axis.VERTICAL, BOARD_Y + 25, BOARD_Y + BOARD_HEIGHT - 25));
        levelThree.enemies.add(new Enemy(BOARD_X + 265, BOARD_Y + 105, 10, 4.7, Enemy.Axis.HORIZONTAL, BOARD_X + 205, BOARD_X + BOARD_WIDTH - 205));
        levelThree.enemies.add(new Enemy(BOARD_X + BOARD_WIDTH - 265, BOARD_Y + 285, 10, 4.7, Enemy.Axis.HORIZONTAL, BOARD_X + 205, BOARD_X + BOARD_WIDTH - 205));
        levelThree.spinningHazards.add(new SpinningRectangle(BOARD_X + 270, BOARD_Y + 195, 90, 18, 3.0));
        levelThree.spinningHazards.add(new SpinningRectangle(BOARD_X + 430, BOARD_Y + 195, 90, 18, -3.0));
        levelThree.coins.add(new Rectangle(BOARD_X + 240, BOARD_Y + 70, 14, 14));
        levelThree.coins.add(new Rectangle(BOARD_X + 350, BOARD_Y + 190, 14, 14));
        levelThree.coins.add(new Rectangle(BOARD_X + 500, BOARD_Y + 310, 14, 14));
        levels.add(levelThree);
    }

    private void setupLevel() {
        enemies.clear();
        spinningHazards.clear();
        coins.clear();

        LevelConfig level = levels.get(levelIndex);
        enemies.addAll(level.enemies);
        spinningHazards.addAll(level.spinningHazards);
        coins.addAll(level.coins);

        coinsCollected = 0;
        player.setStartPosition(START_ZONE.x + 35, START_ZONE.y + BOARD_HEIGHT / 2.0 - Player.SIZE / 2.0);
        player.reset();
    }

    private void resetCampaign() {
        started = true;
        won = false;
        campaignComplete = false;
        levelIndex = 0;
        deaths = 0;
        totalDeaths = 0;
        long now = System.currentTimeMillis();
        campaignStartTime = now;
        levelStartTime = now;
        levelFinishTime = 0;
        campaignFinishTime = 0;
        buildLevels();
        setupLevel();
    }

    private void restartCurrentLevel() {
        started = true;
        won = false;
        campaignComplete = false;
        deaths = 0;
        levelStartTime = System.currentTimeMillis();
        levelFinishTime = 0;
        buildLevels();
        setupLevel();
    }

    private void nextLevel() {
        totalDeaths += deaths;
        levelIndex++;

        deaths = 0;
        won = false;
        levelFinishTime = 0;
        levelStartTime = System.currentTimeMillis();
        buildLevels();
        setupLevel();
    }

    private void respawnPlayer() {
        deaths++;
        player.reset();
    }

    private void updateGame() {
        if (!started || won || campaignComplete) {
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

        if (GOAL_ZONE.contains(player.getBounds()) && coins.isEmpty()) {
            completeLevel();
        }
    }


    private void completeLevel() {
        won = true;
        levelFinishTime = System.currentTimeMillis();

        if (levelIndex == levels.size() - 1) {
            totalDeaths += deaths;
            campaignComplete = true;
            campaignFinishTime = levelFinishTime;
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
        } else if (campaignComplete) {
            drawCampaignCompleteOverlay(g);
        } else if (won) {
            drawLevelCompleteOverlay(g);
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
        LevelConfig level = levels.get(levelIndex);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("World's Hardest Game", 32, 42);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(level.name, 32, 70);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString(level.description, 32, 94);
        g.drawString("Deaths: " + deaths, 32, 118);
        g.drawString("Total Deaths: " + (totalDeaths + deaths), 130, 118);
        g.drawString("Level Time: " + formatLevelTime(), 260, 118);
        g.drawString("Coins: " + coinsCollected + "/" + (coinsCollected + coins.size()), 410, 118);
        g.drawString("Move: Arrow Keys   Restart: R   New Game: N", 520, 118);
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
        drawOverlay(g, "Press ENTER to Start", "Clear 3 levels. Collect every coin before reaching the goal.",
                "Arrow Keys move • R restarts the level • N restarts the campaign");
    }

    private void drawLevelCompleteOverlay(Graphics2D g) {
        drawOverlay(g, "Level Complete!",
                "Deaths: " + deaths + "   Time: " + formatLevelTime(),
                "Press ENTER for next level • Press R to replay this level • Press N for new game");
    }

    private void drawCampaignCompleteOverlay(Graphics2D g) {
        drawOverlay(g, "Campaign Complete!",
                "Total Deaths: " + totalDeaths + "   Total Time: " + formatCampaignTime(),
                "Press ENTER or N to start a new campaign");
    }

    private void drawOverlay(Graphics2D g, String title, String subtitle, String controls) {
        g.setColor(new Color(0, 0, 0, 175));
        g.fillRect(0, 0, width, height);

        int panelWidth = 620;
        int panelHeight = 210;
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        g.setColor(new Color(245, 247, 255));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 28, 28);
        g.setColor(new Color(35, 40, 85));
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 28, 28);

        g.setColor(new Color(28, 32, 70));
        g.setFont(new Font("Arial", Font.BOLD, 42));
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - titleWidth) / 2, panelY + 70);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, (width - subtitleWidth) / 2, panelY + 115);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int controlsWidth = g.getFontMetrics().stringWidth(controls);
        g.drawString(controls, (width - controlsWidth) / 2, panelY + 155);
    }

    private String formatLevelTime() {
        long end = won && levelFinishTime > 0 ? levelFinishTime : System.currentTimeMillis();
        if (!started || levelStartTime == 0) {
            return "0.0s";
        }
        return formatSeconds(end - levelStartTime);
    }

    private String formatCampaignTime() {
        long end = campaignComplete && campaignFinishTime > 0 ? campaignFinishTime : System.currentTimeMillis();
        if (!started || campaignStartTime == 0) {
            return "0.0s";
        }
        return formatSeconds(end - campaignStartTime);
    }

    private String formatSeconds(long elapsedMillis) {
        double seconds = elapsedMillis / 1000.0;
        return String.format("%.1fs", seconds);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ENTER && (!started || campaignComplete)) {
            resetCampaign();
            return;
        }

        if (key == KeyEvent.VK_ENTER && won && !campaignComplete) {
            nextLevel();
            return;
        }

        if (key == KeyEvent.VK_R) {
            restartCurrentLevel();
            return;
        }

        if (key == KeyEvent.VK_N) {
            resetCampaign();
            return;
        }

        if (!started || won || campaignComplete) {
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

    private static class LevelConfig {
        private final String name;
        private final String description;
        private final List<Enemy> enemies = new ArrayList<>();
        private final List<SpinningRectangle> spinningHazards = new ArrayList<>();
        private final List<Rectangle> coins = new ArrayList<>();

        private LevelConfig(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}

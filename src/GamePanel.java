import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main game panel — handles the game loop, rendering, input, and collision.
 * Demonstrates OOP: Composition, Encapsulation, Polymorphism (draw/update via GameObject)
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    public  static final int WIDTH  = 600;
    public  static final int HEIGHT = 750;
    private static final int FPS    = 60;

    // Game state enum — demonstrates OOP: Encapsulation of state
    private enum State { MENU, PLAYING, GAME_OVER }

    private State state = State.MENU;

    private Thread      gameThread;
    private Player      player;
    private StarField   stars;
    private List<Enemy>     enemies;
    private List<Explosion> explosions;

    private int  score;
    private int  lives;
    private int  enemySpawnTimer;
    private int  enemySpawnInterval = 90; // frames
    private Random rng = new Random();

    // ── Constructor ─────────────────────────────────────────────────────────
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        stars = new StarField();
        initGame();
    }

    private void initGame() {
        player     = new Player(WIDTH / 2 - 20, HEIGHT - 100);
        enemies    = new ArrayList<>();
        explosions = new ArrayList<>();
        score      = 0;
        lives      = 3;
        enemySpawnTimer    = 0;
        enemySpawnInterval = 90;
    }

    // ── Game Loop ────────────────────────────────────────────────────────────
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long frameTime = 1_000_000_000L / FPS;
        long lastTime  = System.nanoTime();

        while (true) {
            long now   = System.nanoTime();
            long delta = now - lastTime;

            if (delta >= frameTime) {
                update();
                repaint();
                lastTime = now;
            }

            Thread.yield();
        }
    }

    // ── Update ───────────────────────────────────────────────────────────────
    private void update() {
        stars.update();

        if (state != State.PLAYING) return;

        player.update();

        // Spawn enemies
        enemySpawnTimer++;
        if (enemySpawnTimer >= enemySpawnInterval) {
            enemies.add(new Enemy(rng.nextInt(WIDTH - 40), -40));
            enemySpawnTimer = 0;
            // Speed up over time
            if (enemySpawnInterval > 30) enemySpawnInterval--;
        }

        enemies.forEach(Enemy::update);
        explosions.forEach(Explosion::update);
        explosions.removeIf(e -> !e.isAlive());

        checkCollisions();

        // Remove dead enemies
        enemies.removeIf(e -> !e.isAlive());

        // Enemy reaches bottom → lose a life
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (e.getY() > HEIGHT) {
                it.remove();
                loseLife();
            }
        }
    }

    // ── Collision Detection ──────────────────────────────────────────────────
    private void checkCollisions() {
        List<Bullet> playerBullets = player.getBullets();

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            // Player bullets → enemy
            Iterator<Bullet> bit = playerBullets.iterator();
            while (bit.hasNext()) {
                Bullet b = bit.next();
                if (b.isAlive() && b.getBounds().intersects(enemy.getBounds())) {
                    enemy.destroy();
                    b.destroy();
                    score += 10;
                    explosions.add(new Explosion(
                        enemy.getX() + 18, enemy.getY() + 18,
                        new Color(255, 120, 0)
                    ));
                    break;
                }
            }
            playerBullets.removeIf(b -> !b.isAlive());

            // Enemy bullets → player
            for (Bullet eb : enemy.getBullets()) {
                if (eb.isAlive() && eb.getBounds().intersects(player.getBounds())) {
                    eb.destroy();
                    explosions.add(new Explosion(
                        player.getX() + 20, player.getY() + 25,
                        new Color(0, 200, 255)
                    ));
                    loseLife();
                }
            }

            // Enemy collides with player directly
            if (enemy.getBounds().intersects(player.getBounds())) {
                enemy.destroy();
                explosions.add(new Explosion(
                    player.getX() + 20, player.getY() + 25,
                    new Color(255, 80, 80)
                ));
                loseLife();
            }
        }
    }

    private void loseLife() {
        lives--;
        if (lives <= 0) state = State.GAME_OVER;
    }

    // ── Rendering ────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(new Color(5, 5, 20));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        stars.draw(g);

        if (state == State.MENU) {
            drawMenu(g2);
        } else if (state == State.PLAYING) {
            drawGame(g2);
        } else {
            drawGame(g2);
            drawGameOver(g2);
        }
    }

    private void drawGame(Graphics2D g) {
        enemies.forEach(e -> e.draw(g));
        explosions.forEach(ex -> ex.draw(g));
        if (lives > 0) player.draw(g);
        drawHUD(g);
    }

    private void drawHUD(Graphics2D g) {
        // Score
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(new Color(0, 220, 255));
        g.drawString("SCORE: " + score, 15, 30);

        // Lives as small ship icons
        g.drawString("LIVES: ", WIDTH - 160, 30);
        for (int i = 0; i < lives; i++) {
            int lx = WIDTH - 80 + i * 25;
            int[] bx = { lx + 8, lx + 16, lx + 13, lx + 3, lx };
            int[] by = { lx - lx + 10, lx - lx + 16, lx - lx + 26, lx - lx + 26, lx - lx + 16 };
            // simple triangle icon
            g.setColor(new Color(70, 200, 255));
            g.fillPolygon(new int[]{lx+8, lx+16, lx}, new int[]{12, 26, 26}, 3);
        }
    }

    private void drawMenu(Graphics2D g) {
        // Title
        g.setFont(new Font("Courier New", Font.BOLD, 46));
        drawGlowText(g, "SPACE SHOOTER", WIDTH / 2, 220, new Color(0, 200, 255));

        g.setFont(new Font("Courier New", Font.PLAIN, 18));
        g.setColor(new Color(180, 230, 255));
        g.drawString("Arrow Keys / WASD  →  Move", WIDTH / 2 - 130, 310);
        g.drawString("SPACE  →  Shoot", WIDTH / 2 - 130, 338);

        // Blinking start prompt
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g.setFont(new Font("Courier New", Font.BOLD, 22));
            g.setColor(new Color(0, 255, 180));
            g.drawString("Press ENTER to Start", WIDTH / 2 - 110, 430);
        }
    }

    private void drawGameOver(Graphics2D g) {
        // Dark overlay
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("Courier New", Font.BOLD, 48));
        drawGlowText(g, "GAME OVER", WIDTH / 2, 300, new Color(255, 80, 80));

        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + score, WIDTH / 2 - 90, 360);

        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.setColor(new Color(0, 255, 180));
            g.drawString("Press ENTER to Restart", WIDTH / 2 - 115, 430);
        }
    }

    private void drawGlowText(Graphics2D g, String text, int cx, int cy, Color color) {
        FontMetrics fm = g.getFontMetrics();
        int tx = cx - fm.stringWidth(text) / 2;
        // Glow layers
        for (int r = 8; r >= 0; r -= 2) {
            float alpha = 0.04f + (8 - r) * 0.01f;
            g.setColor(new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, alpha));
            g.drawString(text, tx - r, cy + r);
            g.drawString(text, tx + r, cy - r);
        }
        g.setColor(color);
        g.drawString(text, tx, cy);
    }

    // ── Input ─────────────────────────────────────────────────────────────────
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (state == State.MENU || state == State.GAME_OVER) {
            if (k == KeyEvent.VK_ENTER) {
                initGame();
                state = State.PLAYING;
            }
            return;
        }

        switch (k) {
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> player.setMovingLeft(true);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setMovingRight(true);
            case KeyEvent.VK_UP,    KeyEvent.VK_W -> player.setMovingUp(true);
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> player.setMovingDown(true);
            case KeyEvent.VK_SPACE               -> player.setShooting(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> player.setMovingLeft(false);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setMovingRight(false);
            case KeyEvent.VK_UP,    KeyEvent.VK_W -> player.setMovingUp(false);
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> player.setMovingDown(false);
            case KeyEvent.VK_SPACE               -> player.setShooting(false);
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}

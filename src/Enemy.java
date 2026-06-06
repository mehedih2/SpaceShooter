import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enemy spaceship that moves and occasionally shoots.
 * Demonstrates OOP: Inheritance, Encapsulation
 */
public class Enemy extends GameObject {

    private static final Random RNG = new Random();

    private int    speedX;
    private int    shootTimer;
    private int    shootInterval;
    private List<Bullet> bullets;

    public Enemy(int x, int y) {
        super(x, y, 36, 36);
        speedX        = (RNG.nextBoolean() ? 1 : -1) * (1 + RNG.nextInt(2));
        shootInterval = 80 + RNG.nextInt(80);
        shootTimer    = RNG.nextInt(shootInterval);
        bullets       = new ArrayList<>();
    }

    @Override
    public void update() {
        x += speedX;

        // Bounce off walls
        if (x <= 0 || x >= GamePanel.WIDTH - width) speedX = -speedX;

        // Slow descent
        y += 1;

        // Shoot downward periodically
        shootTimer++;
        if (shootTimer >= shootInterval) {
            bullets.add(new Bullet(x + width / 2 - 3, y + height, 7, false));
            shootTimer = 0;
        }

        // Off-screen bottom → destroy
        if (y > GamePanel.HEIGHT) destroy();

        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isAlive());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Body
        int[] bx = { x + width / 2, x + width, x + width - 6, x + 6, x };
        int[] by = { y + height, y + height - 16, y, y, y + height - 16 };
        g2.setColor(new Color(220, 60, 60));
        g2.fillPolygon(bx, by, 5);

        // Cockpit
        g2.setColor(new Color(255, 160, 160));
        g2.fillOval(x + 10, y + 8, 16, 14);

        // Wing accents
        g2.setColor(new Color(160, 30, 30));
        g2.fillRect(x, y + 14, 6, 12);
        g2.fillRect(x + width - 6, y + 14, 6, 12);

        // Engine glow (bottom)
        g2.setColor(new Color(255, 120, 0, 130));
        g2.fillOval(x + 8, y + height - 8, 20, 14);

        // Draw enemy bullets
        bullets.forEach(b -> b.draw(g));
    }

    public List<Bullet> getBullets() { return bullets; }
}

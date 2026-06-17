import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

      
        if (x <= 0 || x >= GamePanel.WIDTH - width) speedX = -speedX;

    
        y += 1;

   
        shootTimer++;
        if (shootTimer >= shootInterval) {
            bullets.add(new Bullet(x + width / 2 - 3, y + height, 7, false));
            shootTimer = 0;
        }

        if (y > GamePanel.HEIGHT) destroy();

        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isAlive());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       
        int[] bx = { x + width / 2, x + width, x + width - 6, x + 6, x };
        int[] by = { y + height, y + height - 16, y, y, y + height - 16 };
        g2.setColor(new Color(220, 60, 60));
        g2.fillPolygon(bx, by, 5);

      
        g2.setColor(new Color(255, 160, 160));
        g2.fillOval(x + 10, y + 8, 16, 14);

    
        g2.setColor(new Color(160, 30, 30));
        g2.fillRect(x, y + 14, 6, 12);
        g2.fillRect(x + width - 6, y + 14, 6, 12);

     
        g2.setColor(new Color(255, 120, 0, 130));
        g2.fillOval(x + 8, y + height - 8, 20, 14);

      
        bullets.forEach(b -> b.draw(g));
    }

    public List<Bullet> getBullets() { return bullets; }
}

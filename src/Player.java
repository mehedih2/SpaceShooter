import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Player extends GameObject {

    private static final int SPEED      = 5;
    private static final int BULLET_CD  = 15; 

    private boolean movingLeft, movingRight, movingUp, movingDown;
    private boolean shooting;
    private int     cooldown;
    private List<Bullet> bullets;

    public Player(int x, int y) {
        super(x, y, 40, 50);
        bullets   = new ArrayList<>();
        cooldown  = 0;
    }

    @Override
    public void update() {
        
        if (movingLeft  && x > 0)                        x -= SPEED;
        if (movingRight && x < GamePanel.WIDTH - width)  x += SPEED;
        if (movingUp    && y > 0)                        y -= SPEED;
        if (movingDown  && y < GamePanel.HEIGHT - height) y += SPEED;

        
        if (cooldown > 0) cooldown--;
        if (shooting && cooldown == 0) {
            bullets.add(new Bullet(x + width / 2 - 3, y - 10, -10, true));
            cooldown = BULLET_CD;
        }

     
        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isAlive());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       
        g2.setColor(new Color(0, 180, 255, 120));
        g2.fillOval(x + 10, y + height - 10, 20, 18);

       
        int[] bx = { x + width / 2, x + width, x + width - 8, x + 8, x };
        int[] by = { y, y + 20, y + height, y + height, y + 20 };
        g2.setColor(new Color(70, 200, 255));
        g2.fillPolygon(bx, by, 5);

      
        g2.setColor(new Color(180, 240, 255));
        g2.fillOval(x + 12, y + 10, 16, 20);

     
        g2.setColor(new Color(30, 120, 200));
        g2.fillRect(x, y + 25, 8, 15);
        g2.fillRect(x + width - 8, y + 25, 8, 15);

       
        bullets.forEach(b -> b.draw(g));
    }

    public void setMovingLeft (boolean v) { movingLeft  = v; }
    public void setMovingRight(boolean v) { movingRight = v; }
    public void setMovingUp   (boolean v) { movingUp    = v; }
    public void setMovingDown (boolean v) { movingDown  = v; }
    public void setShooting   (boolean v) { shooting    = v; }

    public List<Bullet> getBullets() { return bullets; }
}

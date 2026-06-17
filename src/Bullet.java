import java.awt.*;


public class Bullet extends GameObject {

    private int speedY;
    private boolean fromPlayer;

    public Bullet(int x, int y, int speedY, boolean fromPlayer) {
        super(x, y, 6, 16);
        this.speedY     = speedY;
        this.fromPlayer = fromPlayer;
    }

    @Override
    public void update() {
        y += speedY;
      
        if (y < -height || y > GamePanel.HEIGHT + height) {
            destroy();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (fromPlayer) {
           
            g2.setColor(new Color(0, 255, 220, 200));
            g2.fillRoundRect(x, y, width, height, 4, 4);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x + 1, y + 1, width - 2, 4, 2, 2);
        } else {
          
            g2.setColor(new Color(255, 60, 60, 200));
            g2.fillRoundRect(x, y, width, height, 4, 4);
            g2.setColor(new Color(255, 200, 200));
            g2.fillRoundRect(x + 1, y + height - 5, width - 2, 4, 2, 2);
        }
    }

    public boolean isFromPlayer() { return fromPlayer; }
}

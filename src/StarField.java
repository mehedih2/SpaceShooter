import java.awt.*;
import java.util.Random;


public class StarField {

    private static final Random RNG = new Random();
    private static final int COUNT  = 120;

    private int[]   sx, sy, speed;
    private float[] brightness;

    public StarField() {
        sx         = new int[COUNT];
        sy         = new int[COUNT];
        speed      = new int[COUNT];
        brightness = new float[COUNT];

        for (int i = 0; i < COUNT; i++) {
            sx[i]         = RNG.nextInt(GamePanel.WIDTH);
            sy[i]         = RNG.nextInt(GamePanel.HEIGHT);
            speed[i]      = 1 + RNG.nextInt(3);
            brightness[i] = 0.4f + RNG.nextFloat() * 0.6f;
        }
    }

    public void update() {
        for (int i = 0; i < COUNT; i++) {
            sy[i] += speed[i];
            if (sy[i] > GamePanel.HEIGHT) {
                sy[i] = 0;
                sx[i] = RNG.nextInt(GamePanel.WIDTH);
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < COUNT; i++) {
            int b = (int)(brightness[i] * 255);
            g.setColor(new Color(b, b, b));
            int size = speed[i]; 
            g.fillOval(sx[i], sy[i], size, size);
        }
    }
}

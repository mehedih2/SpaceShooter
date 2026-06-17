import java.awt.*;
import java.util.Random;


public class Explosion extends GameObject {

    private static final Random RNG = new Random();

    private int   lifetime;
    private int   maxLife;
    private int[] px, py, pdx, pdy;
    private Color color;

    public Explosion(int cx, int cy, Color color) {
        super(cx, cy, 1, 1);
        this.color   = color;
        this.maxLife = 30;
        this.lifetime = 0;

        int count = 12;
        px  = new int[count];
        py  = new int[count];
        pdx = new int[count];
        pdy = new int[count];

        for (int i = 0; i < count; i++) {
            px[i]  = cx;
            py[i]  = cy;
            pdx[i] = RNG.nextInt(7) - 3;
            pdy[i] = RNG.nextInt(7) - 3;
        }
    }

    @Override
    public void update() {
        lifetime++;
        for (int i = 0; i < px.length; i++) {
            px[i] += pdx[i];
            py[i] += pdy[i];
        }
        if (lifetime >= maxLife) destroy();
    }

    @Override
    public void draw(Graphics g) {
        float alpha = 1f - (float) lifetime / maxLife;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            alpha
        ));
        for (int i = 0; i < px.length; i++) {
            int size = (int)(6 * alpha) + 2;
            g2.fillOval(px[i], py[i], size, size);
        }
    }
}

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Abstract base class for all game objects.
 * Demonstrates OOP: Abstraction, Encapsulation
 */
public abstract class GameObject {
    protected int x, y;
    protected int width, height;
    protected boolean alive;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alive = true;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() { return alive; }
    public void destroy()    { alive = false; }
    public int getX()        { return x; }
    public int getY()        { return y; }
}

import javax.swing.*;

/**
 * Main class — entry point for the Space Shooter game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Space Shooter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GamePanel panel = new GamePanel();
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null); // center on screen
            frame.setVisible(true);

            panel.startGame();
        });
    }
}

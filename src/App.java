import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int tileSize = 32, rows =16, cols=16;
        int width = tileSize*cols, height = tileSize*rows;

        JFrame frame = new JFrame("Space Invaders - Home");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.add(new HomePage(width, height, frame));
        frame.setVisible(true);
    }
}

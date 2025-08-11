import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns;  // 512px
        int boardHeight = tileSize * rows;    // 512px

        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HomePage homePage = new HomePage(boardWidth, boardHeight, frame);
        frame.add(homePage);

        frame.setVisible(true);
    }
}

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int tileSize = 32;
        int rows = 20;
        int columns = 30;  
        int boardWidth = tileSize * columns;
        int boardHeight = tileSize * rows;

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

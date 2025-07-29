import javax.swing.*;
import java.awt.*;

public class SpaceInvaders extends JPanel {

    class Block {
        int x, y, width, height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    int tileSize = 32;
    int columns = 16;
    int rows = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    Image shipImg;
    Block ship;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        shipImg = new ImageIcon(getClass().getResource("./img/ship.png")).getImage();

        int shipWidth = tileSize * 2;
        int shipHeight = tileSize;
        int shipX = (boardWidth / 2) - tileSize;
        int shipY = boardHeight - tileSize * 2;

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
    }
}

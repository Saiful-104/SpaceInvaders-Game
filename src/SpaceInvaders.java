import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

    int tileSize = 32;
    int rows = 16;
    int cols = 16;
    int boardWidth = tileSize * cols;
    int boardHeight = tileSize * rows;

    int shipWidth = tileSize * 2;
    int shipHeight = tileSize;
    int shipX = tileSize * cols / 2 - tileSize;
    int shipY = tileSize * rows - tileSize * 2;
    int shipSpeed = tileSize;

    Block ship;

    ArrayList<Block> aliens;
    int alienRows = 2;
    int alienCols = 3;
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    int alienXStart = tileSize;
    int alienYStart = tileSize;
    int alienSpeedX = 1;

    ArrayList<Block> bullets;
    int bulletWidth = tileSize / 4;
    int bulletHeight = tileSize / 4;
    int bulletSpeedY = -10;

    int alienBulletSpeedY = 5;

    ArrayList<Block> powerUps;
    int powerUpSize = tileSize;
    int powerUpSpeedY = 3;

    boolean gameOver = false;
    int score = 0;
    Random random = new Random();

    Timer timer;

    Image shipImg;
    ArrayList<Image> alienImgs;

    class Block {
        int x, y, width, height;
        Image img;
        boolean alive = true;
        boolean used = false;
        int type = 0; // 0=player bullet, 1=alien bullet, 2=power-up

        Block(int x, int y, int w, int h, Image img) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.img = img;
        }
    }

    public SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        loadImages();

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);

        aliens = new ArrayList<>();
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();

        createAliens();

        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    void loadImages() {
        shipImg = new ImageIcon(getClass().getResource("/img/ship.png")).getImage();

        alienImgs = new ArrayList<>();
        alienImgs.add(new ImageIcon(getClass().getResource("/img/alien.png")).getImage());
        alienImgs.add(new ImageIcon(getClass().getResource("/img/alien-cyan.png")).getImage());
        alienImgs.add(new ImageIcon(getClass().getResource("/img/alien-magenta.png")).getImage());
        alienImgs.add(new ImageIcon(getClass().getResource("/img/alien-yellow.png")).getImage());
    }

    void createAliens() {
        aliens.clear();
        for (int c = 0; c < alienCols; c++) {
            for (int r = 0; r < alienRows; r++) {
                Image img = alienImgs.get(random.nextInt(alienImgs.size()));
                int x = alienXStart + c * alienWidth;
                int y = alienYStart + r * alienHeight;
                aliens.add(new Block(x, y, alienWidth, alienHeight, img));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        // Aliens
        for (Block alien : aliens) {
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // Bullets
        for (Block b : bullets) {
            if (!b.used) {
                if (b.type == 0) {
                    g.setColor(Color.WHITE);
                    g.fillOval(b.x, b.y, b.width, b.height);
                } else if (b.type == 1) {
                    g.setColor(Color.RED);
                    g.fillOval(b.x, b.y, b.width, b.height);
                }
            }
        }

        // Power-ups
        g.setColor(Color.GREEN);
        for (Block p : powerUps) {
            g.fillOval(p.x, p.y, p.width, p.height);
        }

        // Score & Game Over Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if (gameOver) {
            g.drawString("Game Over! Score: " + score, 20, 40);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press any key to restart", 20, 70);
        } else {
            g.drawString("Score: " + score, 20, 40);
        }
    }

    void move() {
        if (gameOver)
            return;

        boolean edgeReached = false;
        for (Block alien : aliens) {
            if (alien.alive) {
                alien.x += alienSpeedX;
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    edgeReached = true;
                }
            }
        }
        if (edgeReached) {
            alienSpeedX *= -1;
            for (Block alien : aliens) {
                alien.y += alienHeight;
                if (alien.y + alien.height >= ship.y) {
                    gameOver = true;
                }
            }
        }

        for (Block b : bullets) {
            if (!b.used) {
                if (b.type == 0) {
                    b.y += bulletSpeedY;
                    if (b.y + b.height < 0)
                        b.used = true;
                } else if (b.type == 1) {
                    b.y += alienBulletSpeedY;
                    if (b.y > boardHeight)
                        b.used = true;
                }
            }
        }

        // Alien random shoot
        for (Block alien : aliens) {
            if (alien.alive && random.nextInt(300) == 0) {
                int bulletX = alien.x + alien.width / 2 - bulletWidth / 2;
                int bulletY = alien.y + alien.height;
                Block alienBullet = new Block(bulletX, bulletY, bulletWidth, bulletHeight, null);
                alienBullet.type = 1;
                bullets.add(alienBullet);
            }
        }

        // Player bullets hit aliens
        for (Block b : bullets) {
            if (b.type == 0 && !b.used) {
                for (Block alien : aliens) {
                    if (alien.alive && detectCollision(b, alien)) {
                        b.used = true;
                        alien.alive = false;
                        score += 100;

                        // 20% chance to drop power-up
                        if (random.nextInt(5) == 0) {
                            Block powerUp = new Block(alien.x, alien.y, powerUpSize, powerUpSize, null);
                            powerUp.type = 2;
                            powerUps.add(powerUp);
                        }
                    }
                }
            }
        }

        // Alien bullets hit player
        for (Block b : bullets) {
            if (b.type == 1 && !b.used && detectCollision(b, ship)) {
                gameOver = true;
            }
        }

        // Power-ups movement and collection
        for (int i = 0; i < powerUps.size(); i++) {
            Block p = powerUps.get(i);
            p.y += powerUpSpeedY;
            if (detectCollision(p, ship)) {
                bulletSpeedY -= 2; // Increase bullet speed (negative value)
                score += 200;
                powerUps.remove(i);
                i--;
            } else if (p.y > boardHeight) {
                powerUps.remove(i);
                i--;
            }
        }

        bullets.removeIf(b -> b.used);

        // Level up when all aliens dead
        boolean allDead = true;
        for (Block alien : aliens) {
            if (alien.alive) {
                allDead = false;
                break;
            }
        }
        if (allDead) {
            score += alienCols * alienRows * 100;
            alienCols = Math.min(alienCols + 1, cols / 2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            aliens.clear();
            bullets.clear();
            powerUps.clear();
            createAliens();
        }
    }

    boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            timer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gameOver) {
            // Restart game on any key press
            shipX = tileSize * cols / 2 - tileSize;
            ship.x = shipX;
            score = 0;
            alienCols = 3;
            alienRows = 2;
            alienSpeedX = 1;
            bulletSpeedY = -10;

            aliens.clear();
            bullets.clear();
            powerUps.clear();

            createAliens();
            gameOver = false;
            timer.start();
        } else {
            if (code == KeyEvent.VK_LEFT && ship.x - shipSpeed >= 0) {
                ship.x -= shipSpeed;
            } else if (code == KeyEvent.VK_RIGHT && ship.x + shipSpeed + ship.width <= boardWidth) {
                ship.x += shipSpeed;
            } else if (code == KeyEvent.VK_SPACE) {
                int bulletX = ship.x + shipWidth * 15 / 32;
                Block bullet = new Block(bulletX, ship.y, bulletWidth, bulletHeight, null);
                bullet.type = 0;
                bullets.add(bullet);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

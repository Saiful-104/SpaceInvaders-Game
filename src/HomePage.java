import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class HomePage extends JPanel implements ActionListener {
    int width, height, rocketY;
    boolean rocketGoingUp = false;
    Timer timer;
    JButton startButton;
    JFrame frame;
    ArrayList<Point> stars = new ArrayList<>();
    Random rand = new Random();

    public HomePage(int w, int h, JFrame f) {
        width = w;
        height = h;
        frame = f;
        rocketY = h - 100;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setLayout(null);

        for (int i = 0; i < 100; i++)
            stars.add(new Point(rand.nextInt(width), rand.nextInt(height)));

        startButton = new JButton("🚀 LET'S START");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(0, 120, 215));
        startButton.setForeground(Color.WHITE);
        startButton.setBounds(width / 2 - 100, height - 80, 200, 45);
        startButton.addActionListener(e -> {
            rocketGoingUp = true;
            timer.start();
            startButton.setVisible(false);
        });
        add(startButton);

        timer = new Timer(30, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (rocketGoingUp && rocketY > -100) {
            rocketY -= 5;

            for (Point star : stars) {
                star.y++;
                if (star.y > height) {
                    star.y = 0;
                    star.x = rand.nextInt(width);
                }
            }

            repaint();
        } else if (rocketY <= -100) {
            timer.stop();
            openGame();
        }
    }

    void openGame() {
        frame.getContentPane().removeAll();
        frame.add(new SpaceInvaders());
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Stars
        g.setColor(Color.WHITE);
        for (Point star : stars)
            g.fillOval(star.x, star.y, 2, 2);

        // titel tex
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("WELCOME TO", width / 2 - 110, 70);

        g.setFont(new Font("Arial", Font.BOLD, 38));
        g.setColor(Color.YELLOW);
        g.drawString("SPACE INVADERS", width / 2 - 170, 120);

        // 🚀 roket after edit version
        if (rocketGoingUp) {
            int rocketX = width / 2 - 20;

            // head
            int[] xPoints = {rocketX, rocketX + 20, rocketX + 40};
            int[] yPoints = {rocketY + 20, rocketY, rocketY + 20};
            g.setColor(Color.LIGHT_GRAY);
            g.fillPolygon(xPoints, yPoints, 3);

            // body
            g.setColor(Color.RED);
            g.fillRect(rocketX, rocketY + 20, 40, 60);

            // fins
            g.setColor(Color.DARK_GRAY);
            g.fillRect(rocketX - 10, rocketY + 40, 10, 20); // left fin
            g.fillRect(rocketX + 40, rocketY + 40, 10, 20); // right fin

            // flames
            g.setColor(Color.ORANGE);
            g.fillOval(rocketX + 5, rocketY + 80, 10, 15);
            g.setColor(Color.YELLOW);
            g.fillOval(rocketX + 15, rocketY + 82, 10, 13);
            g.setColor(Color.RED);
            g.fillOval(rocketX + 10, rocketY + 85, 10, 10);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class HomePage extends JPanel {
    private int width, height;
    private JButton startButton;
    private JFrame frame;
    private ArrayList<Point> stars = new ArrayList<>();
    private Random rand = new Random();

    public HomePage(int width, int height, JFrame frame) {
        this.width = width;
        this.height = height;
        this.frame = frame;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setLayout(null);

        for (int i = 0; i < 100; i++) {
            stars.add(new Point(rand.nextInt(width), rand.nextInt(height)));
        }

        startButton = new JButton(" LET'S START");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(0, 120, 215));
        startButton.setForeground(Color.WHITE);
        startButton.setBounds(width / 2 - 100, height - 80, 200, 45);
        startButton.addActionListener(e -> openGame());
        add(startButton);
    }

    private void openGame() {
        frame.getContentPane().removeAll();
        frame.add(new SpaceInvaders());
        frame.revalidate();
        frame.repaint();
        frame.getContentPane().getComponent(0).requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        for (Point star : stars) {
            g.fillOval(star.x, star.y, 2, 2);
        }

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("WELCOME TO", width / 2 - 110, 70);

        g.setFont(new Font("Arial", Font.BOLD, 38));
        g.setColor(Color.YELLOW);
        g.drawString("SPACE INVADERS", width / 2 - 170, 120);
    }
}

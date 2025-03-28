

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Panel extends JPanel implements ActionListener {

    private int height = 700;
    private int width = 700;
    private int size = 30;
    private int countmax = (height * width) / size;
    private int delay = 68;
    private int x[] = new int[countmax];
    private int y[] = new int[countmax];
    private int bodyparts = 6;
    private int score;
    private int eatx;
    private int eaty;
    private char direction = 'D';
    private boolean run = false;
    private Timer timer;
    private Random random;
    private Font font;
    private Music music;
    private JButton helpButton;
    private JMenuItem saveMenuItem;
    private JMenuBar menuBar;
    private JMenu fileMenu;

    private BufferedImage image;

    Panel() throws IOException {
        helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpDialog();
            }
        });
        this.add(helpButton);
        helpButton.setVisible(false); //present if game is over

        music = new Music();
        music.play("example.wav"); //background music
        font = new Font("Arial", Font.ITALIC, 50);
        random = new Random();
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.WHITE);
        this.setFocusable(true); //that inputs work
        this.addKeyListener(new adapter()); //keyboard listener

        menuBar = new JMenuBar(); //menu and save score in file
        fileMenu = new JMenu("File");
        saveMenuItem = new JMenuItem("Save your score");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScore();
            }
        });
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        //image load from file
        image = ImageIO.read(new File("flower.png"));

        start();
    }

    public void start() {
        newfood(); //new meal
        run = true; //present when the game is running and when its not
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (run) { //when the game is running always show the meal and the snake(head and body)
            //g.setColor(Color.RED);
            //g.fillOval(eatx, eaty, size, size);
            g.drawImage(image, eatx, eaty, size, size, this);

            for (int i = 0; i < bodyparts; i++) {
                if (i == 0) {
                    g.setColor(Color.CYAN);
                    g.fillRect(x[i], y[i], size, size);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x[i], y[i], size, size);
                }
            }
            g.setColor(Color.BLUE);
            g.setFont(font);
            g.drawString("Score: " + score, width / 2 - 50, 50); //actual score
        } else {
            end(g); //if run is false then game over
        }
    }

    public void newfood() { //create meal
        eatx = random.nextInt(width / size) * size;
        eaty = random.nextInt(height / size) * size;
    }

    public void move() {
        for (int i = bodyparts; i > 0; i--) //body shifting
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) //position switch to correct keys
        {
            case 'U':
                y[0] = y[0] - size;
                break;
            case 'D':
                y[0] = y[0] + size;
                break;
            case 'R':
                x[0] = x[0] + size;
                break;
            case 'L':
                x[0] = x[0] - size;
                break;
        }
    }

    public void food() { // if head collide with meal then increase body and score
        if (x[0] == eatx && y[0] == eaty) {
            bodyparts++;
            score++;
            newfood();

        }
    }


    public void hit() {
        // if collide the snake with itself
        if (IntStream.range(1, bodyparts).anyMatch(i -> x[0] == x[i] && y[0] == y[i])) {
            run = false;
        }

        //if collide with wall
        if (Arrays.stream(x).anyMatch(value -> value < 0) || Arrays.stream(x).anyMatch(value -> value > width) ||
                Arrays.stream(y).anyMatch(value -> value < 0) || Arrays.stream(y).anyMatch(value -> value > height)) {
            run = false;
        }
        if (run == false) {
            timer.stop();
        }
    }

    public void end(Graphics g) {
        g.setColor(Color.BLUE); //if its game over then showing help button and score
        g.setFont(font);
        g.drawString("Game Over!", width / 2 - 50, height / 2);
        g.setColor(Color.BLUE);
        g.drawString("Score: " + score, width / 2 - 50, height / 2 + 50);
        helpButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (run) // inf cycle: shift, is collided with food or wall or itself
        {
            move();
            food();
            hit();
        }
        repaint();

    }

    public class adapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:  // correct moving check, like if the snake is moving forward then it cant go direct opposite way
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    private void showHelpDialog() { //message on help button
        String helpMessage = "Game Rules:\n" +
                "Movement: Arrow keys\n" +
                "Goal: Eat the flowers and avoid colliding with your own body or the game area boundaries.\n" +
                "Have fun!";

        JOptionPane.showMessageDialog(this, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveScore() { //save to file
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                writer.write("Score: " + score);
                writer.close();
                JOptionPane.showMessageDialog(this, "The score is saved with success!", "Score Save", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error at the score saving!", "Score Save", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    JMenuBar getMenuBar() {
        return menuBar;
    }

}

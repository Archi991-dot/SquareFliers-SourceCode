package SquareFliers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class MainSquare implements ActionListener, MouseListener, KeyListener {

    //REQUIRED VARIABLES FOR DECLARATION AND GAMEPLAY
    public static MainSquare mainSquare;
    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    private JFrame jFrame;
    public boolean gameOver, started;
    public Random rand;

    //ALL IMPLEMENTATIONS BELOW
    public MainSquare() {

        //Main Declarations
        jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        rand = new Random();
        renderer = new Renderer();

        //Game Declarations
        jFrame.add(renderer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setTitle("Square Fliers");
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20 );
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    //ALL METHODS/FUNCTIONS FOR GAMEPLAY STARTUP, RULES, AND FINISHING

    public void addColumn(boolean start) {

        int space = 260;
        int width = 100;
        int height = 50 + rand.nextInt(260);

        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }
    public void paintColumn(Graphics g, Rectangle column) {

        //Columns
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);

    }

    public void jump()
    {
        if (gameOver)
        {
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }

        if (!started)
        {
            started = true;
        }
        else if (!gameOver)
        {
            if (yMotion > 1)
            {
                yMotion = 0;
            }

            yMotion -= 10;
        }
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        int speed = 10;
        ticks++;

        if(started) {

            for (int i = 0; i < columns.size(); i++) {

                Rectangle column = columns.get(i);
                column.x -= speed;

            }

            if (ticks % 2 == 0 && yMotion < 12) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {

                Rectangle column = columns.get(i);

                if (column.x + column.width < 0) {

                    columns.remove(column);

                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {
                if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;
                    if(bird.x <= column.x) {
                        bird.x = column.x - bird.width;
                    }
                    else {
                        if(column.y != 0) {
                            bird.y = column.y - bird.height;
                        }
                        else if(bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }
            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
            if(bird.y + yMotion >= HEIGHT - 150) {
                bird.y = HEIGHT - 150 - bird.height;
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {

        //Background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Lower Rectangle Background
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);

        //Upper Rectangle Background
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 150, WIDTH, 20);

        //The Square or The Bird
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column : columns) {
            paintColumn(g, column);
        }

        g.setColor(Color.darkGray.darker().darker());
        g.setFont(new Font("Cambria", 1, 100));

        if(!started) {
            g.drawString("CLICK or SPACE", 50, HEIGHT / 1 - 50);
        }

        if(gameOver) {
            g.drawString("GAME OVER!", 97, HEIGHT / 2 - 50);
        }

        if(!gameOver && started) {

            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }

    }

    public static void main(String[] args) {

        mainSquare = new MainSquare();

    }

    //MOUSE CLICK EVENTS
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        jump();
    }

    //NOT NEEDED
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    //KeyEvents for SPACEBAR

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }

    }
}

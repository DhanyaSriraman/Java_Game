import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    final int PANEL_WIDTH = 500;
    final int PANEL_HEIGHT = 500;
    Image enemy;
    //Image backgroundImage;
    Timer timer;
    int xVelocity = 1;
    int yVelocity = 1;
    int x = 0;
    int y = 0;

    MyPanel() throws IOException {
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setBackground(Color.white);
        System.out.println("hey1");
        enemy = new ImageIcon("C:\\Users\\Dhanya\\IdeaProjects\\Java_Game\\src\\image.png").getImage();

//        enemy = ImageIO.read( getClass().getResourceAsStream("smile.png"));
        System.out.println("hey2 "+enemy);
        if(enemy==null)
            System.out.println(enemy);
        //backgroundImage = new ImageIcon("space.png").getImage();
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g) {

        super.paint(g); // paint background

        Graphics2D g2D = (Graphics2D) g;

        //g2D.drawImage(backgroundImage, 0, 0, null);
        g2D.drawImage(enemy, x, y, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

//        if(x>=PANEL_WIDTH-enemy.getWidth(null) || x<0) {
//            xVelocity = xVelocity * -1;
//        }
//        x = x + xVelocity;


        x=250;
        if(y>=PANEL_HEIGHT-enemy.getHeight(null) || y<0) {
            yVelocity = yVelocity * -1;
        }
        y = y + yVelocity;
        repaint();
    }
}
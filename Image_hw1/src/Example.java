
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.io.File;
import java.io.IOException;

public class Example {

    public static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
        return frame;
    }
    
    public static void showPng(BufferedImage image) {
        JFrame frame = buildFrame();
        JPanel pane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };


        frame.add(pane);
    }


    
    public static void main(String[] args) throws IOException {
        File f = new File("D:\\1！中山大学大三第一学期\\数字图像处理\\hw1\\hw1\\hw1_input\\16.png");
        BufferedImage image = ImageIO.read(f);
        BufferedImage image1 = ImageIO.read(f);
       
        int w = image.getWidth();
        int h = image.getHeight();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int ARGB = image.getRGB(j, i);
                int A = (ARGB >> 24) & 0xFF;
                int R = (ARGB >> 16) & 0xFF;
                int G = (ARGB >> 8) & 0xFF;
                int B = ARGB & 0xFF;
                int gray = (int) (R * 0.3 + G * 0.59 + B * 0.11);
                int ARGB1 = ((A << 24) & 0xFF000000)
                    | ((gray << 16) & 0x00FF0000)
                    | ((gray << 8) & 0x0000FF00)
                    | (gray & 0x000000FF);

                image.setRGB(j, i, ARGB1);
            }
        }
        
        showPng(image);
    }

}

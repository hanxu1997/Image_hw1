import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class ShowImage {
    public static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle("15331416 ’‘∫Æ–Ò -16.png");
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
}

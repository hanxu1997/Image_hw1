import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BilineInterpolationToScale {


	private static BufferedImage scaledImg;

	public static BufferedImage imgScaling(BufferedImage originalImg, int scaledWidth, int scaledHeight) {
		int [][][] originakARGBInformation = getAllARGB(originalImg);
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();
		int [][][] scaledARGBInformation = new int [scaledHeight][scaledWidth][4];
		
		float xRatio = (float)height/(float)scaledHeight;
		float yRatio = (float)width/(float)scaledWidth;
		
		for (int x = 0; x < scaledHeight; x++) {
			double originalX = ((double)x) * xRatio;
			double j = Math.floor(originalX);
			double u = originalX - j;
			for (int y = 0; y < scaledWidth; y++) {
				double originalY = ((double)y) * yRatio;
				double k = Math.floor(originalY);
				double v = originalY - k;
				
				double ONE = 1.0d;
				double factor1 = (ONE - u) * (ONE - v);
				double factor2 = u * (ONE - v);
				double factor3 = (ONE - u) * v;
				double factor4 = u * v;
				
				for (int i = 0; i < 4; i++) {
					scaledARGBInformation[x][y][i] = (int)(
							factor1 * originakARGBInformation[ensureRange((int)j, height-1)][ensureRange((int)k, width-1)][i] + 
							factor2 * originakARGBInformation[ensureRange((int)(j+1), height-1)][ensureRange((int)k, width-1)][i] +
							factor3 * originakARGBInformation[ensureRange((int)(j), height-1)][ensureRange((int)(k+1), width-1)][i] +
							factor4 * originakARGBInformation[ensureRange((int)(j+1), height-1)][ensureRange((int)(k+1), width-1)][i]
							);
				}
				
			}
		}
		return getScaledImgByARGB(scaledARGBInformation, scaledWidth, scaledHeight);
		
	}
	
	public static int ensureRange(int num, int max) {
		if (num > max) {
			return max;
		}
		if (num < 0) {
			return 0;
		}
		return num;
	}
	
	
	public static int [][][] getAllARGB(BufferedImage originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();
		int [][][] ARGBInformation = new int[height][width][4];
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				int ARGB = originalImg.getRGB(y, x);// !!!!(y.x)不要取反
                int A = (ARGB >> 24) & 0xFF;
                int R = (ARGB >> 16) & 0xFF;
                int G = (ARGB >> 8) & 0xFF;
                int B = ARGB & 0xFF;
                ARGBInformation[x][y][0] = A;
                ARGBInformation[x][y][1] = R;
                ARGBInformation[x][y][2] = G;
                ARGBInformation[x][y][3] = B;
			}
		}
		return ARGBInformation;
	}
	
	public static BufferedImage getScaledImgByARGB(int [][][] scaledARGBInformation, int scaledWidth, int scaledHeight) {
		BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < scaledHeight; x++) {
			for (int y = 0; y < scaledWidth; y++) {
				int A = scaledARGBInformation[x][y][0];
				int R = scaledARGBInformation[x][y][1];
				int G = scaledARGBInformation[x][y][2];
				int B = scaledARGBInformation[x][y][3];
				int ARGB = ((A << 24) & 0xFF000000)
	                    | ((R << 16) & 0x00FF0000)
	                    | ((G << 8) & 0x0000FF00)
	                    | (B & 0x000000FF);
				scaledImg.setRGB(y, x, ARGB); // !!!!(y.x)不要取反
			}
		}
		return scaledImg;
	}
	
	public static void main(String[] args) throws IOException {
        File f = new File("C:\\Workspace\\hw1_input\\16.png");
        BufferedImage image = ImageIO.read(f);
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        scaledImg = imgScaling(image, 192, 128);
        Example.showPng(scaledImg);
        System.out.println(scaledImg.getWidth());
        System.out.println(scaledImg.getHeight());
        
	}
}

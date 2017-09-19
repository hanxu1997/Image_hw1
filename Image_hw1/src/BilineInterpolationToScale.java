import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BilineInterpolationToScale {


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
		BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_GRAY);
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
	public static void createFile(String createpath) throws IOException {
		File directory = new File(".");
		String path = null;
		path = directory.getCanonicalPath();//获取当前路径
		path += createpath;
		File file= new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		
	}

	
	public static void main(String[] args) throws IOException {
		createFile("\\scaled_Img");
        File f = new File(".\\input_Img\\16.png");
        BufferedImage image = ImageIO.read(f);
        // System.out.println(image.getType());
        // 原图type为10，BufferedImage.TYPE_BYTE_GRAY
        BufferedImage scaledImg = image;
        int j = 128;
        for (int i = 192; i >= 12; i = i/2) {
        	scaledImg = imgScaling(image, i, j);
        	String iString = String.valueOf(i);
        	String jString = String.valueOf(j);
        	ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\" + iString+ "X" + jString + "_scaled.png"));
        	j = j/2;
        }
        scaledImg = imgScaling(image, 300, 200);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\300X200_scaled.png"));
        scaledImg = imgScaling(image, 450, 300);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\450X300_scaled.png"));
        scaledImg = imgScaling(image, 500, 200);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\500X200_scaled.png"));
        scaledImg = imgScaling(image, 384, 256);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\384X256_scaled.png"));

        
	}
}

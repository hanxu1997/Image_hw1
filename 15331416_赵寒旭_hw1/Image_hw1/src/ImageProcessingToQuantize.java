import java.awt.image.BufferedImage;



public class ImageProcessingToQuantize {
	/**
	 * 灰度值对应R,G,B的转换因子
	 */
	public static final double R_FACTOR = 0.30;
	public static final double G_FACTOR = 0.59;
	public static final double B_FACTOR = 0.11;
	
	/**
	 * 量化灰度图像
	 * 遍历每个像素点，获得量化后像素值并set
	 * @param grayImg
	 * @param level
	 * @return
	 */
	public static BufferedImage imageQuantize(BufferedImage grayImg, int level) {
		int width = grayImg.getWidth();
		int height = grayImg.getHeight();
		BufferedImage quantizedImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // 初始化灰度图像
		
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				int grayARGB = grayImg.getRGB(y, x);
				int quantized_grayARGB = quantizedGray(grayARGB, level);
				quantizedImg.setRGB(y, x, quantized_grayARGB);
			}
		}
		return quantizedImg;
	}
	
	/**
	 * 已知原ARGB值
	 * 由量化level重新生成ARGB值
	 * @param grayARGB
	 * @param level
	 * @return
	 */
	public static int quantizedGray(int grayARGB, int level) {
		int length = (int)(255/(level-1));
		// System.out.println(length);
		int A = (grayARGB >> 24) & 0xFF;
		int grayValue = grayARGB & 0xFF; // 取分量
		int newGray = 0;
		double compareToLevel = ((double)grayValue - (int)(grayValue/length) * length)/length;
		if (compareToLevel >= 0.5) {
			newGray = ((int)(grayValue/length) + 1) * length;
		} else {
			newGray = (int)(grayValue/length) * length;
		}
		if (newGray > 255) {
			newGray = 255;
		}
		// 量化后重新生成该点ARGB值
		int quantizedGray = ((A << 24) & 0xFF000000)
				| ((newGray << 16) & 0x00FF0000)
				| ((newGray << 8) & 0x0000FF00)
				| (newGray & 0x000000FF);
		return quantizedGray;
	}
	
	/**
	 * 利用灰度转换公式将原图转换为灰度图像
	 * 注：所用输入图像类型为TYPE_BYTE_GRAY，已经是灰度图像，可以不用转化
	 * @param originalImg
	 * @return
	 */
	public static BufferedImage changeToGrayImg(BufferedImage originalImg) {
		int width = originalImg.getWidth();
        int height = originalImg.getHeight();
        BufferedImage grayImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int ARGB = originalImg.getRGB(j, i);
                int A = (ARGB >> 24) & 0xFF;
                int R = (ARGB >> 16) & 0xFF;
                int G = (ARGB >> 8) & 0xFF;
                int B = ARGB & 0xFF;
                int gray = (int) (R * R_FACTOR + G * G_FACTOR + B * B_FACTOR);
                int ARGB1 = ((A << 24) & 0xFF000000)
                    | ((gray << 16) & 0x00FF0000)
                    | ((gray << 8) & 0x0000FF00)
                    | (gray & 0x000000FF);
                grayImg.setRGB(j, i, ARGB1);
            }
        }
        return grayImg;
	}
	

	
}

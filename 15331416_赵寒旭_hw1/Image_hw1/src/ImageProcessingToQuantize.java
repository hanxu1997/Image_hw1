import java.awt.image.BufferedImage;

public class ImageProcessingToQuantize {
	/**
	 * 量化灰度图像
	 * 遍历每个像素点，获得量化后像素值并set
	 * @param grayImg
	 * @param level
	 * @return quantizedImg
	 */
	public static BufferedImage imageQuantize(BufferedImage grayImg, int level) {
		int width = grayImg.getWidth();
		int height = grayImg.getHeight();
		BufferedImage quantizedImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // 初始化灰度图像
		/**
		 * 遍历图像矩阵x行y列
		 * 获得每个像素点量化后的值，赋值给量化后图像
		 */
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
	 * @return quantizedGray
	 */
	public static int quantizedGray(int grayARGB, int level) {
		int length = (int) Math.floor(255/(level-1)); // 新分级每一级长度（向下取整）
		int A = (grayARGB >> 24) & 0xFF; // 取A分量
		int grayValue = (grayARGB >> 16)& 0xFF; // 取R分量为代表
		int newGray = getNewGray(grayValue, length); // 新的灰度值
		// 量化后重新生成该点ARGB值
		int quantizedGray = ((A << 24) & 0xFF000000)| ((newGray << 16) & 0x00FF0000)
						| ((newGray << 8) & 0x0000FF00) | (newGray & 0x000000FF);
		return quantizedGray;
	}	
	
	/**
	 * 由原灰度值和每级长度求得变换后灰度值
	 * @param grayValue
	 * @param length
	 * @return newGray
	 */
	public static int getNewGray(int grayValue, int length) {
		int newGray = 0; // 新的灰度值
		/**
		 * 判断灰度值落在哪一级
		 * 先向下取整，若灰度值到此级距离不小于每级长度一半，则级数+1
		 * 距离小于一半则级数不变
		 */
		int graylevel = (int)Math.floor(grayValue/length);
		double distance = grayValue -  graylevel * length;
		double halfofLevellength = (double)length/2;
		if (distance >= halfofLevellength) {
			newGray = (graylevel+ 1) * length;
		} else {
			newGray = graylevel * length;
		}
		// 确保不会越界
		if (newGray > 255) {
			newGray = 255;
		}
		return newGray;
	}
	
}

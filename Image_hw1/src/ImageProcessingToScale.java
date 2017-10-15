import java.awt.image.BufferedImage;

public class ImageProcessingToScale {
	/**
	 * 由原图像和变换后宽度长度
	 * 求得放缩后图像
	 * @param originalImg
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return
	 */
	public static BufferedImage imgScaling(BufferedImage originalImg, int scaledWidth, int scaledHeight) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();
		int [][]originalARGB = new int[height][width];
		int [][]scaledARGB = new int[scaledHeight][scaledWidth];
		// 用二维数组保存原图像每个像素的ARGB值
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				originalARGB[i][j] = originalImg.getRGB(j, i);
			}
		}
		// 计算变换比率
		double xRatio = (double)height/(double)scaledHeight;
		double yRatio = (double)width/(double)scaledWidth;
		// 遍历变换后图像每个像素
		for (int x = 0; x < scaledHeight; x++) {
			for (int y = 0; y < scaledWidth; y++) {
				double originalX = (double)(x*xRatio);
				double j = Math.floor(originalX);
				double u = originalX - j;
				double originalY = (double)(y*yRatio);
				double k = Math.floor(originalY);
				double v = originalY - k;
				double ONE = 1.0;
				// 计算变换因子
				double []factors = new double[4];
				factors[0] = (ONE - u) * (ONE - v);
				factors[1] = u * (ONE - v);
				factors[2] = (ONE - u) * v;
				factors[3] = u * v;

				// 对应原图像四个待插值点的ARGB值
				int []originalArgs = new int[4];
				int rangeRow = height - 1;
				int rangeCol = width - 1;
				originalArgs[0] = originalARGB[ensureRange((int)j, rangeRow)][ensureRange((int)k, rangeCol)];
				originalArgs[1] = originalARGB[ensureRange((int)(j+1), rangeRow)][ensureRange((int)k, rangeCol)];
				originalArgs[2] = originalARGB[ensureRange((int)(j), rangeRow)][ensureRange((int)(k+1), rangeCol)];
				originalArgs[3] = originalARGB[ensureRange((int)(j+1), rangeRow)][ensureRange((int)(k+1), rangeCol)];
				// 得到变换后图像(x,y)处ARGB值
				scaledARGB[x][y] = getScaledARGB(originalArgs, factors);	
			}
		}
		// 由变换后图像的ARGB数组获得处理后图像
		return getScaledImgByARGB(scaledARGB, scaledWidth, scaledHeight);	
	}
	

	/**
	 * 由四个待插值的点的ARGB值 和 转换因子
	 * 得到变换后此点的ARGB值
	 * @param originalARGB
	 * @param factors
	 * @return
	 */
	static int getScaledARGB(int[]originalARGB, double[]factors) {
		int []sumARGB = new int[4]; // 转换后的ARGB各个分量值
		int [][]num = new int[4][4]; // 4个ARGB分量
		// 四个A值：num[0][0:3]
		// 四个R值：num[1][0:3]
		// 四个G值：num[2][0:3]
		// 四个B值：num[3][0:3]
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == 0) {
					num[i][j] = (originalARGB[j] >> 24) & 0xFF; // A
				}
				if (i == 1) {
					num[i][j] = (originalARGB[j] >> 16) & 0xFF; // R
				}
				if (i == 2) {
					num[i][j] = (originalARGB[j] >> 8) & 0xFF; // G
				}
				if (i == 3) {
					num[i][j] = originalARGB[j] & 0xFF; // B
				}
			}
		}
		double temp = 0.0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				temp += factors[j]*num[i][j];
			}
			sumARGB[i] = (int)temp; // 存储转换后分量
			temp = 0.0;
		}
		// 由转换后各个分量求出ARGB值
		int ARGB = ((sumARGB[0] << 24) & 0xFF000000)
                	| ((sumARGB[1] << 16) & 0x00FF0000)
                	| ((sumARGB[2] << 8) & 0x0000FF00)
                	| (sumARGB[3] & 0x000000FF);
		return ARGB;
	}
	
	/**
	 * 从ARGB数组得到放缩后图像
	 * @param ARGB
	 * @param width
	 * @param height
	 * @return
	 */
	static BufferedImage getScaledImgByARGB(int[][]ARGB, int width, int height) {
		BufferedImage Img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Img.setRGB(j, i, ARGB[i][j]);
			}
		}
		return Img;
	}
	
	/**
	 * 确保在原图中取得待插值所用的点时不会越界
	 * @param num
	 * @param max
	 * @return
	 */
	public static int ensureRange(int num, int max) {
		if (num > max) {
			return max;
		}
		if (num < 0) {
			return 0;
		}
		return num;
	}
}

import java.awt.image.BufferedImage;

public class ImageProcessingToScale {
	/**
	 * ��ԭͼ��ͱ任���ȳ���
	 * ��÷�����ͼ��
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
		// �ö�ά���鱣��ԭͼ��ÿ�����ص�ARGBֵ
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				originalARGB[i][j] = originalImg.getRGB(j, i);
			}
		}
		// ����任����
		double xRatio = (double)height/(double)scaledHeight;
		double yRatio = (double)width/(double)scaledWidth;
		// �����任��ͼ��ÿ������
		for (int x = 0; x < scaledHeight; x++) {
			for (int y = 0; y < scaledWidth; y++) {
				double originalX = (double)(x*xRatio);
				double j = Math.floor(originalX);
				double u = originalX - j;
				double originalY = (double)(y*yRatio);
				double k = Math.floor(originalY);
				double v = originalY - k;
				double ONE = 1.0;
				// ����任����
				double []factors = new double[4];
				factors[0] = (ONE - u) * (ONE - v);
				factors[1] = u * (ONE - v);
				factors[2] = (ONE - u) * v;
				factors[3] = u * v;

				// ��Ӧԭͼ���ĸ�����ֵ���ARGBֵ
				int []originalArgs = new int[4];
				int rangeRow = height - 1;
				int rangeCol = width - 1;
				originalArgs[0] = originalARGB[ensureRange((int)j, rangeRow)][ensureRange((int)k, rangeCol)];
				originalArgs[1] = originalARGB[ensureRange((int)(j+1), rangeRow)][ensureRange((int)k, rangeCol)];
				originalArgs[2] = originalARGB[ensureRange((int)(j), rangeRow)][ensureRange((int)(k+1), rangeCol)];
				originalArgs[3] = originalARGB[ensureRange((int)(j+1), rangeRow)][ensureRange((int)(k+1), rangeCol)];
				// �õ��任��ͼ��(x,y)��ARGBֵ
				scaledARGB[x][y] = getScaledARGB(originalArgs, factors);	
			}
		}
		// �ɱ任��ͼ���ARGB�����ô����ͼ��
		return getScaledImgByARGB(scaledARGB, scaledWidth, scaledHeight);	
	}
	

	/**
	 * ���ĸ�����ֵ�ĵ��ARGBֵ �� ת������
	 * �õ��任��˵��ARGBֵ
	 * @param originalARGB
	 * @param factors
	 * @return
	 */
	static int getScaledARGB(int[]originalARGB, double[]factors) {
		int []sumARGB = new int[4]; // ת�����ARGB��������ֵ
		int [][]num = new int[4][4]; // 4��ARGB����
		// �ĸ�Aֵ��num[0][0:3]
		// �ĸ�Rֵ��num[1][0:3]
		// �ĸ�Gֵ��num[2][0:3]
		// �ĸ�Bֵ��num[3][0:3]
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
			sumARGB[i] = (int)temp; // �洢ת�������
			temp = 0.0;
		}
		// ��ת��������������ARGBֵ
		int ARGB = ((sumARGB[0] << 24) & 0xFF000000)
                	| ((sumARGB[1] << 16) & 0x00FF0000)
                	| ((sumARGB[2] << 8) & 0x0000FF00)
                	| (sumARGB[3] & 0x000000FF);
		return ARGB;
	}
	
	/**
	 * ��ARGB����õ�������ͼ��
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
	 * ȷ����ԭͼ��ȡ�ô���ֵ���õĵ�ʱ����Խ��
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

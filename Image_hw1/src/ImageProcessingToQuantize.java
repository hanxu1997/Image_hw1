import java.awt.image.BufferedImage;

public class ImageProcessingToQuantize {
	/**
	 * �����Ҷ�ͼ��
	 * ����ÿ�����ص㣬�������������ֵ��set
	 * @param grayImg
	 * @param level
	 * @return quantizedImg
	 */
	public static BufferedImage imageQuantize(BufferedImage grayImg, int level) {
		int width = grayImg.getWidth();
		int height = grayImg.getHeight();
		BufferedImage quantizedImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // ��ʼ���Ҷ�ͼ��
		/**
		 * ����ͼ�����x��y��
		 * ���ÿ�����ص��������ֵ����ֵ��������ͼ��
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
	 * ��֪ԭARGBֵ
	 * ������level��������ARGBֵ
	 * @param grayARGB
	 * @param level
	 * @return quantizedGray
	 */
	public static int quantizedGray(int grayARGB, int level) {
		int length = (int) Math.floor(255/(level-1)); // �·ּ�ÿһ�����ȣ�����ȡ����
		int A = (grayARGB >> 24) & 0xFF; // ȡA����
		int grayValue = (grayARGB >> 16)& 0xFF; // ȡR����Ϊ����
		int newGray = getNewGray(grayValue, length); // �µĻҶ�ֵ
		// �������������ɸõ�ARGBֵ
		int quantizedGray = ((A << 24) & 0xFF000000)| ((newGray << 16) & 0x00FF0000)
						| ((newGray << 8) & 0x0000FF00) | (newGray & 0x000000FF);
		return quantizedGray;
	}	
	
	/**
	 * ��ԭ�Ҷ�ֵ��ÿ��������ñ任��Ҷ�ֵ
	 * @param grayValue
	 * @param length
	 * @return newGray
	 */
	public static int getNewGray(int grayValue, int length) {
		int newGray = 0; // �µĻҶ�ֵ
		/**
		 * �жϻҶ�ֵ������һ��
		 * ������ȡ�������Ҷ�ֵ���˼����벻С��ÿ������һ�룬����+1
		 * ����С��һ����������
		 */
		int graylevel = (int)Math.floor(grayValue/length);
		double distance = grayValue -  graylevel * length;
		double halfofLevellength = (double)length/2;
		if (distance >= halfofLevellength) {
			newGray = (graylevel+ 1) * length;
		} else {
			newGray = graylevel * length;
		}
		// ȷ������Խ��
		if (newGray > 255) {
			newGray = 255;
		}
		return newGray;
	}
	
}

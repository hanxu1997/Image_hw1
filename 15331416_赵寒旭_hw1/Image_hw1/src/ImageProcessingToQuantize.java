import java.awt.image.BufferedImage;



public class ImageProcessingToQuantize {
	/**
	 * �Ҷ�ֵ��ӦR,G,B��ת������
	 */
	public static final double R_FACTOR = 0.30;
	public static final double G_FACTOR = 0.59;
	public static final double B_FACTOR = 0.11;
	
	/**
	 * �����Ҷ�ͼ��
	 * ����ÿ�����ص㣬�������������ֵ��set
	 * @param grayImg
	 * @param level
	 * @return
	 */
	public static BufferedImage imageQuantize(BufferedImage grayImg, int level) {
		int width = grayImg.getWidth();
		int height = grayImg.getHeight();
		BufferedImage quantizedImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // ��ʼ���Ҷ�ͼ��
		
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
	 * @return
	 */
	public static int quantizedGray(int grayARGB, int level) {
		int length = (int)(255/(level-1));
		// System.out.println(length);
		int A = (grayARGB >> 24) & 0xFF;
		int grayValue = grayARGB & 0xFF; // ȡ����
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
		// �������������ɸõ�ARGBֵ
		int quantizedGray = ((A << 24) & 0xFF000000)
				| ((newGray << 16) & 0x00FF0000)
				| ((newGray << 8) & 0x0000FF00)
				| (newGray & 0x000000FF);
		return quantizedGray;
	}
	
	/**
	 * ���ûҶ�ת����ʽ��ԭͼת��Ϊ�Ҷ�ͼ��
	 * ע����������ͼ������ΪTYPE_BYTE_GRAY���Ѿ��ǻҶ�ͼ�񣬿��Բ���ת��
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

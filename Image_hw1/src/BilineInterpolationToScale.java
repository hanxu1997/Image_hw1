import java.awt.image.BufferedImage;


public class BilineInterpolationToScale {

	/**
	 * ����ԭͼ�ͷ�����ͼ����
	 * �õ�ԭͼÿ�����ص��ARGBֵ
	 * ����������ͼ��ÿһ�����ص�
	 * ˫���Բ�ֵ�õ�������ÿ�����ص��ARGBֵ��Ϣ�����ɴ���Ϣ�õ�������ͼ�񷵻�
	 * @param originalImg
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return
	 */
	public static BufferedImage imgScaling(BufferedImage originalImg, int scaledWidth, int scaledHeight) {
		int [][][] originalARGBInformation = getAllARGB(originalImg); // �õ�ԭͼÿ�����ص��ARGBֵ
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
							factor1 * originalARGBInformation[ensureRange((int)j, height-1)][ensureRange((int)k, width-1)][i] + 
							factor2 * originalARGBInformation[ensureRange((int)(j+1), height-1)][ensureRange((int)k, width-1)][i] +
							factor3 * originalARGBInformation[ensureRange((int)(j), height-1)][ensureRange((int)(k+1), width-1)][i] +
							factor4 * originalARGBInformation[ensureRange((int)(j+1), height-1)][ensureRange((int)(k+1), width-1)][i]
							);
				}
				
			}
		}
		return getScaledImgByARGB(scaledARGBInformation, scaledWidth, scaledHeight);
		
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
	
	/**
	 * ����ά����洢ԭͼÿһ�����ص��ARGBֵ
	 * (x,y)��A,R,G,Bֵ�ֱ�洢��ARGBInformation[x][y][0:4]��
	 * @param originalImg
	 * @return
	 */
	public static int [][][] getAllARGB(BufferedImage originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();
		int [][][] ARGBInformation = new int[height][width][4];
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				int ARGB = originalImg.getRGB(y, x);// !!!!(y.x)��Ҫȡ��
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
	
	/**
	 * �ɴ洢������ͼ��ARGBֵ����ά����õ�������ͼ��
	 * @param scaledARGBInformation
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return
	 */
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
				scaledImg.setRGB(y, x, ARGB); // !!!!(y.x)��Ҫȡ��
			}
		}
		return scaledImg;
	}
	


}

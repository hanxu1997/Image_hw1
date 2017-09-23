import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class ImageHw1_Runner {
	/**
	 * �ڵ�ǰĿ¼���½��ļ���
	 * @param createpath
	 * @throws IOException
	 */
	public static void createFile(String createpath) throws IOException {
		File directory = new File(".");
		String path = null;
		path = directory.getCanonicalPath();//��ȡ��ǰ·��
		path += createpath;
		File file= new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		
	}
	
	public static void scale(BufferedImage image) throws IOException {

        // System.out.println(image.getType());
        // ԭͼtypeΪ10��BufferedImage.TYPE_BYTE_GRAY
        BufferedImage scaledImg = image;
        int j = 128;
        for (int i = 192; i >= 12; i = i/2) {
        	scaledImg = BilineInterpolationToScale.imgScaling(image, i, j);
        	String iString = String.valueOf(i);
        	String jString = String.valueOf(j);
        	ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\" + iString+ "X" + jString + "_scaled.png"));
        	j = j/2;
        }
        scaledImg = BilineInterpolationToScale.imgScaling(image, 300, 200);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\300X200_scaled.png"));
        scaledImg = BilineInterpolationToScale.imgScaling(image, 450, 300);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\450X300_scaled.png"));
        scaledImg = BilineInterpolationToScale.imgScaling(image, 500, 200);
        ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\500X200_scaled.png"));
       // scaledImg = BilineInterpolationToScale.imgScaling(image, 384, 256);
       // ImageIO.write(scaledImg, "png", new File(".\\scaled_Img\\384X256_scaled.png"));
	}
	
	public static void quantize(BufferedImage image) throws IOException {
       // BufferedImage original_gray = ImageProcessingToQuantize.changeToGrayImg(image);
       // ImageIO.write(original_gray, "png", new File(".\\input_Img\\gray.png"));
       // ת����Ҷ�ͼ��ԭ�Ҷ�ͼ��΢С���죨��ʽ�任��΢С������������ԭ�Ҷ�ͼת��
        for (int i = 128; i >= 2; i = i /4) {
        	// ���ṩ����ͼ��16.png��Ϊ�Ҷ�ͼ
        	BufferedImage quantized_gray = ImageProcessingToQuantize.imageQuantize(image, i);
        	// BufferedImage quantized_gray = ImageProcessingToQuantize.imageQuantize(original_gray, i);
        	String iString = String.valueOf(i);
        	ImageIO.write(quantized_gray, "png", new File(".\\quantized_Img\\" + iString+ "_level.png"));
        }
        
        BufferedImage quantized_gray = ImageProcessingToQuantize.imageQuantize(image, 4);
        ImageIO.write(quantized_gray, "png", new File(".\\quantized_Img\\4_level.png"));
	}
	public static void main(String[] args) throws IOException {
		createFile("\\scaled_Img");
		createFile("\\quantized_Img");
        File f = new File(".\\input_Img\\16.png");
        BufferedImage image = ImageIO.read(f);
		/**
		 * ����jar��
		 * java.net.URL imgURL = ImageHw1_Runner.class.getResource("/input_Img/16.png");
		 * BufferedImage image = ImageIO.read(imgURL);
		 */

        ShowImage.showPng(image);
		scale(image);
		quantize(image);
	}
}

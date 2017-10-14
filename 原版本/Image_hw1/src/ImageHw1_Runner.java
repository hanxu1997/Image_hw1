import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class ImageHw1_Runner {
	/**
	 * 在当前目录下新建文件夹
	 * @param createpath
	 * @throws IOException
	 */
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
	
	public static void scale(BufferedImage image) throws IOException {

        // System.out.println(image.getType());
        // 原图type为10，BufferedImage.TYPE_BYTE_GRAY
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
       // 转化后灰度图和原灰度图有微小差异（公式变换有微小误差），故以下用原灰度图转换
        for (int i = 128; i >= 2; i = i /4) {
        	// 所提供输入图像16.png即为灰度图
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
		 * 生成jar包
		 * java.net.URL imgURL = ImageHw1_Runner.class.getResource("/input_Img/16.png");
		 * BufferedImage image = ImageIO.read(imgURL);
		 */

        ShowImage.showPng(image);
		scale(image);
		quantize(image);
	}
}

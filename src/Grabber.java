import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Grabber {
	public static void main(String[] args) throws AWTException, IOException, InterruptedException{
		
		Thread.sleep(5000);
		Robot r = new Robot();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		long time = System.nanoTime();
		BufferedImage img = r.createScreenCapture(new Rectangle((int)screen.getWidth(), (int)screen.getHeight()));
	
		int x1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y1 = Integer.MAX_VALUE, y2 = Integer.MIN_VALUE;
		

		for(int x = 0; x < screen.width; x++){
			for(int y = 0; y < screen.height; y++){
				int[] pixel = new int[3];
				img.getRaster().getPixel(x, y, pixel);
				//System.out.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
				if(matches(pixel, 13, 85, 118, 1)){
					if(x < x1)
						x1 = x;
					if(y < y1)
						y1 = y;					
					if(y > y2)
						y2 = y;
					if(x > x2)
						x2 = x;
				}
			}
		}
		
		System.out.println(x1 + ", " + y1 + " \r\n " + x2 + ", " + y2);
		
		BufferedImage table = img.getSubimage(x1, y1, x2-x1, y2-y1);
		
		int[][][] matrix = new int[x2-x1][y2-x1][3];
		
		for(int x = 0; x < screen.width; x++){
			for(int y = 0; y < screen.height; y++){
				int[] pixel = new int[3];
				table.getRaster().getPixel(x, y, pixel);
				matrix[x][y] = pixel;
			}
		}
		
		System.out.println("Time: " + (System.nanoTime() - time) + "ns");
		ImageIO.write(table, "png", new File("./table.png"));
		
		
	}
	
	
	public static boolean matches(int[] pixels, int r, int g, int b, int tolerance){
		return Math.abs(pixels[0] - r) <= tolerance && Math.abs(pixels[1] - g) <= tolerance && Math.abs(pixels[2] - b) <= tolerance;
	}
	
}

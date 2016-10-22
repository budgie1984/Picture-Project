package imageprocessing;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import edu.princeton.cs.introcs.Picture;



 /* 
 * @author brian burroughs
*/


public class ConnectedComponentImage {

	Picture picture;
	private int width;
	private int height;
	WeightedQuickUnionUF wqf;
	ArrayList<Integer> roots;

	String answer;
	static int option;
	/**
	 * Initialise fields
	 * 
	 * @param fileLocation
	 */
	public ConnectedComponentImage(String fileLocation) {
		picture  = new Picture(fileLocation);
		countComponents();
		getRoots();
		

	}


	/**
	 * Returns the number of components identified in the image.
	 * 
	 * @return the number of components (between 1 and N)
	 */

	public int countComponents() {
		picture = new Picture(binaryComponentImage());
		width = picture.width();
		height = picture.height();
		int size = width * height;

		wqf = new WeightedQuickUnionUF(size);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				if ((y + 1 < height)&& (picture.get(x, y).equals(picture.get(x, y + 1)))) // compare current pixel to one above
				{
					wqf.union(id(x, y), id(x, y + 1)); // union current pixel and one above
				}
				if ((x + 1 < width)&& (picture.get(x, y).equals(picture.get(x + 1, y)))) // compare current pixel to right one	
				{
					wqf.union(id(x, y), id(x + 1, y)); // union current pixel and right one	
				}
				if ((y - 1 > 0)&& (picture.get(x, y).equals(picture.get(x, y - 1)))) // compare current pixel to one below
				{
					wqf.union(id(x, y), id(x, y - 1)); // union current pixel and one below
				}
				if ((x - 1 > 0)&& (picture.get(x, y).equals(picture.get(x - 1, y)))) // compare current pixel to left one
				{
					wqf.union(id(x, y), id(x - 1, y)); // union current pixel and left one
				}

			}
		}

		return wqf.count() -1;
	}

	public int id(int x, int y) {
		return (y * width) + x; // converts 2d grid to single flat 1d grid
	}

	/**
	 * Returns the original image with each object bounded by a red box.
	 * 
	 * @return a picture object with all components surrounded by a red box
	 */

	public void drawRedBoxesAroundComponents(){
	
		for (int i = 0; i < roots.size(); i++) {
			
			indentifyComponentImage(roots.get(i));
		}
	}
	
	/**
	 * Returns an image with one component bounded by a red box.
	 * 
	 * @return a picture object with one component surrounded by a red box
	 */
	public Picture indentifyComponentImage(int componentRoot ){
	
		int maxX = 0;
		int minX = width;
		int maxY = 0;
		int minY = height;

		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				  
				if (wqf.root(id(x, y))==(componentRoot) ) {

					if (x < minX)
						minX = x;
					if (x > maxX)
						maxX = x;
					if (y < minY)
						minY = y;
					if (y > maxY)
						maxY = y;
				}
				
			}

		}

		if (minX > maxX || minY > maxY) {
			System.out.println("It's All White Pixels!!!");
		} else {
			for (int x = minX; x <= maxX; x++) {
				picture.set(x, minY, Color.RED);
				picture.set(x, maxY, Color.RED);
			}

			for (int y = minY; y <= maxY; y++) {
				picture.set(minX, y, Color.RED);
				picture.set(maxX, y, Color.RED);
			}
		}

		picture.show();
		return picture;	
		
	}
	
	
	public void getRoots() {

		roots = new ArrayList<Integer>();

		for (int i = 0; i < width * height; i++) {

			if (!(roots.contains(wqf.root(i)))) {
				roots.add(wqf.root(i));
			}
		}
	}
	
	
	public Color[] randomColours(int size) {

		Color[] randomColours = new Color[size];

		for (int i = 0; i < randomColours.length; i++) {

			int R = (int) (Math.random() * 256);
			int G = (int) (Math.random() * 256);
			int B = (int) (Math.random() * 256);

			randomColours[i] = new Color(R, G, B);

		}
		return randomColours;
	}
	
	/**
	 * Returns a picture with each object updated to a random colour.
	 * 
	 * @return a picture object with all components coloured.
	 */

	public Picture colourComponentImage() {

		Color[] randomColours = randomColours(roots.size());

		for (int x = 0; x < picture.width(); x++) {
			for (int y = 0; y < picture.height(); y++) {

				int root = wqf.root(id(x, y));
				picture.set(x, y, randomColours[roots.indexOf(root)]);
			}
		}
		picture.show();
		return picture;
	}

	
	
//	public Picture getPicture() {
//		return picture;
//	}

	/**
	 * Returns a binarised version of the original image
	 * 
	 * @return a picture object with all components surrounded by a red box
	 */
	public Picture binaryComponentImage() {

		 picture = new Picture(picture);
		 width = picture.width();
		 height = picture.height();

		double thresholdPixelValue = 128.0;
		// convert to grayscale
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color c = picture.get(x, y);
				if (Luminance.lum(c) < thresholdPixelValue) {
					picture.set(x, y, Color.BLACK);
				} else {
					picture.set(x, y, Color.WHITE);
				}
			}
		}
		picture.show();
		return picture;
	}

	public void loadPic() {

		picture = new Picture("src/images/stars2.jpg");
		picture.show();

	}

	public void greyScalePic() {

		picture = new Picture("src/images/stars2.jpg");
		width = picture.width();
		height = picture.height();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = picture.get(x, y);
				Color gray = Luminance.toGray(color);
				picture.set(x, y, gray);
			}
		}
		picture.show();
	}

	public static void main(String[] args) {
		
		
		ConnectedComponentImage app = new ConnectedComponentImage("src/images/stars2.jpg");
		app.run();
		while (option != 0) {
			app.run();

		}

	}

	public void run() {

		answer = JOptionPane.showInputDialog("Please select an Option \n 1: Count Components In Image \n "
											+ "2: Randomise Component Colours "
											+ "\n 3: Load Image with Red Boxes  \n\n 4: LoadColour Image \n "
											+ "5: Load GreyScale Image\n\n Press 0 to exit",null);
		
		option = Integer.parseInt(answer);
		if (option == 1) {
			JOptionPane.showMessageDialog(null,"The Number of Components in this Image is: " + countComponents());
		}
		if (option == 2) {
			colourComponentImage();
		}
		if (option == 3) {
			drawRedBoxesAroundComponents();
		}
		if (option == 4) {
			loadPic();
		}
		if (option == 5) {
			greyScalePic();
		}
		if (option == 6) {
			
		}

	}

}


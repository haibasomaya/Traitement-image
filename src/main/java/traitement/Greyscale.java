/*
 * Greyscale.java
 * JAVA app for converting image to greyscale
 * @author Gabor
 * 2016
 */
package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Greyscale {

    public void run(){
        BufferedImage image = null;

        // read the image
        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            System.err.println(e);
        }

        // get image width and height
        int width = image.getWidth();
        int height = image.getHeight();

        // convert to greyscale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int a = c.getAlpha();
                // to get gree color ze should make (g+r+b)/3

                // to get gree color ze should make (g+r+b)/3
                int gr = (r + g + b) / 3;

                // replace RGB value with average
                Color gree = new Color(gr, gr, gr, a);
                image.setRGB(i, j, gree.getRGB());
            }
        }

        // write image
        try {
            File file_out = new File("image_grey.jpg");
            ImageIO.write(image, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}

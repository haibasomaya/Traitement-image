/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//import static traitement.Prewitt.file;

public class Prewit {
    // public static File file = null;

    public  void run(){
        BufferedImage image = null;
        // the sobel matrix in two 2D arrays
        int[][] Pwx = {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
        int[][] Pwy = {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};

        // a sobel template 2D array for calculation
        int[][] prewitt;

        // read the image
        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            System.err.println(e);
        }

        // get image width and height
        int width = image.getWidth();
        int height = image.getHeight();

        // at first need to greyscale and populate sob[][] array
        prewitt = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;

                prewitt[x][y] = avg;
                // replace RGB value with average
                pixel = (avg << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(x, y, pixel);
            }
        }

        // sobel calculation
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int Ix = (Pwx[0][0] * prewitt[x - 1][y - 1]) + (Pwx[0][1] * prewitt[x][y - 1])
                        + (Pwx[0][2] * prewitt[x + 1][y - 1]) + (Pwx[1][0] * prewitt[x - 1][y])
                        + (Pwx[1][1] * prewitt[x][y]) + (Pwx[1][2] * prewitt[x + 1][y])
                        + (Pwx[2][0] * prewitt[x - 1][y + 1]) + (Pwx[2][1] * prewitt[x][y + 1])
                        + (Pwx[2][2] * prewitt[x + 1][y + 1]);

                int Iy = (Pwy[0][0] * prewitt[x - 1][y - 1]) + (Pwy[0][1] * prewitt[x][y - 1])
                        + (Pwy[0][2] * prewitt[x + 1][y - 1]) + (Pwy[1][0] * prewitt[x - 1][y])
                        + (Pwy[1][1] * prewitt[x][y]) + (Pwy[1][2] * prewitt[x + 1][y])
                        + (Pwy[2][0] * prewitt[x - 1][y + 1]) + (Pwy[2][1] * prewitt[x][y + 1])
                        + (Pwy[2][2] * prewitt[x + 1][y + 1]);

                int pixel = (int) Math.sqrt((Ix * Ix) + (Iy * Iy));

                if (pixel > 255) {
                    pixel = 255;
                } else if (pixel < 0) {
                    pixel = 0;
                }

                Color pix = new Color(pixel, pixel, pixel);
                image.setRGB(x, y, pix.getRGB());
            }
        }

        // write image
        try {
            File file_out = new File("Prewitt.jpg");
            image = image.getSubimage(1, 1, width - 2, height - 2);
            ImageIO.write(image, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}

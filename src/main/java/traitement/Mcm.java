/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import javax.imageio.ImageIO;

/**
 *
 * @author Acer
 */
public class Mcm {

    BufferedImage image, output = null;
    Direve drive = new Direve();

    public void run(int nombreIterations) {

        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        double img[][] = new double[width + 2][height + 2];
        double mcm[][] = new double[width + 2][height + 2];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r + g + b) / 3;
                img[i][j] = moyen_pixel;

            }
        }
        output = new BufferedImage(width, height, TYPE_INT_RGB);

        for (int k = 0; k < nombreIterations; k++) {
            double img_yy[][] = drive.ImageYY(img, width, height);
            double img_xx[][] = drive.ImageXX(img, width, height);
            double img_xy[][] = drive.ImageXY(img, width, height);
            double img_x[][] = drive.ImageX(img, width, height);
            double img_y[][] = drive.ImageY(img, width, height);

            for (int x = 1; x < width - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    mcm[x][y] = (img_xx[x][y] * img_y[x][y] * img_y[x][y] - 2 * img_xy[x][y] * img_y[x][y] * img_x[x][y] + img_yy[x][y] * img_x[x][y] * img_x[x][y]) / (img_x[x][y] * img_x[x][y] + img_y[x][y] * img_y[x][y]);
                }
            }

            for (int i = 0; i < width - 1; i++) {
                for (int j = 0; j < height - 1; j++) {
                    img[i][j] += 0.25 * mcm[i][j];
                }
            }
            double poc = (k * 100) / nombreIterations;
            System.out.println(poc + "%" + "  de traitement ");
        }
        System.out.println("traitement de MCM est finit ");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int conv = (int) img[i][j];
                output.setRGB(i, j, (conv << 16 | conv << 8 | conv));
                conv = (conv < 0) ? 0 : conv;
                conv = (conv > 255) ? 255 : conv;
                int rgb = new Color(conv, conv, conv).getRGB();
                output.setRGB(i, j, rgb);
            }
        }
        try {
            File file_out = new File("image_MCM.jpg");
            output = output.getSubimage(1, 1, output.getWidth() - 2, output.getHeight() - 2);
            ImageIO.write(output, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}

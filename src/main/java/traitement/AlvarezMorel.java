package traitement;

import bin.Photos;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AlvarezMorel {

    Direve direve = new Direve();

    public void run(int nombreIterations) {
        BufferedImage image = null, output = null;

        int i, x, y, j, k;
        double dt = 0.5;

        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        double[][] img = new double[width + 2][height + 2];
        double[][] Alvarez = new double[width + 2][height + 2];
        double[][] grad = new double[width + 2][height + 2];
        double[][] laplac = new double[width + 2][height + 2];
        double[][] Emss = new double[width + 2][height + 2];

        // r�cup�ration des pix en gris
        for (j = 0; j < width; j++) {
            for (k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r + g + b) / 3;
                img[j + 2][k + 2] = moyen_pixel;
                pixel = (moyen_pixel << 24) | (moyen_pixel << 16) | (moyen_pixel << 8) | moyen_pixel;
                image.setRGB(j, k, pixel);
            }
        }

        for (i = 0; i < nombreIterations; i++) {

            double[][] I_x = direve.ImageX(img, width, height);
            double[][] I_y = direve.ImageY(img, width, height);
            double[][] I_xx = direve.ImageXX(img, width, height);
            double[][] I_yy = direve.ImageYY(img, width, height);
            double[][] I_xy = direve.ImageXY(img, width, height);

            // Traitement
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {

                    grad[x][y] = Math.sqrt(I_x[x][y] * I_x[x][y] + I_y[x][y] * I_y[x][y]);
                    laplac[x][y] = I_xx[x][y] + I_yy[x][y];
                    Emss[x][y] = (I_xx[x][y] * I_y[x][y] * I_y[x][y] - 2 * I_xy[x][y] * I_x[x][y] * I_y[x][y] + I_yy[x][y] * I_x[x][y] * I_x[x][y]) / (I_x[x][y] * I_x[x][y] + I_y[x][y] * I_y[x][y]);
                    Alvarez[x][y] = fctseuille2(grad[x][y]) * ((1 - fctseuille2(grad[x][y])) * laplac[x][y] + fctseuille2(grad[x][y]) * Emss[x][y]);
                }
            }
            for (x = 0; x < width - 1; x++) {
                for (y = 0; y < height - 1; y++) {
                    img[x][y] += dt * Alvarez[x][y];
                }
            }

            for (x = 0; x < width; x++) {
                for (y = 0; y < height; y++) {
                    int conv = (int) img[x + 2][y + 2];
                    image.setRGB(x, y, (conv << 16 | conv << 8 | conv));
                }
            }
            double poc = (i * 100) / nombreIterations;
            System.out.println(poc + "%" + " de traitement.");
        }
        System.out.println("traitement de alvarz_Morel est finit.");
        try {
            File file_out = new File("alvarz_Morel.jpg");
            output = image.getSubimage(1, 1, width - 2, height - 2);
            ImageIO.write(image, "JPG", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // fonction réguliaire
    private double fctseuille1(double g) {
        return 1 / (1 + Math.pow(Math.abs(g) / 3, 2));
    }

    private double fctseuille2(double g) {
        return 1 / (1 + Math.pow(Math.abs(g), 2));
    }

    public static double seuillage_exp(double gradient, double K) {

        return (Math.exp(-(Math.pow(Math.abs(gradient) / K, 2))));
    }
    

}

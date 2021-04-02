package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OsherRudin {

    File file = null;

    public void run(int nombreIterations) {

        BufferedImage image = null, output = null;
        Direve direve = new Direve();
        double I_x[][], I_y[][], I_xx[][], I_yy[][], I_xy[][];
        int i, j, k, x, y;
        double dt = 0.25;

        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        double img[][] = new double[width + 2][height + 2];
        double grad[][] = new double[width + 2][height + 2];
        double emss[][] = new double[width + 2][height + 2];
        double osherrudin[][] = new double[width + 2][height + 2];

        // remplissage 
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

            I_x = direve.ImageX(img, width, height);
            I_y = direve.ImageY(img, width, height);
            I_xy = direve.ImageXY(img, width, height);
            I_yy = direve.ImageYY(img, width, height);
            I_xx = direve.ImageXX(img, width, height);

            for (x = 0; x < width - 1; x++) {
                for (y = 0; y < height - 1; y++) {
                    grad[x][y] = Math.sqrt((I_x[x][y] * I_x[x][y] + I_y[x][y] * I_y[x][y]));
                    emss[x][y] = (I_xx[x][y] * I_y[x][y] * I_y[x][y] - 2 * I_xy[x][y] * I_x[x][y] * I_y[x][y] + I_yy[x][y] * I_x[x][y] * I_x[x][y]) / (I_x[x][y] * I_x[x][y] + I_y[x][y] * I_y[x][y]);
                    osherrudin[x][y] = +sign(emss[x][y]) * Math.abs(grad[x][y]);

                }
            }
            // Enregistrement des pixel dans l'img
            for (x = 0; x < width; x++) {
                for (y = 0; y < height; y++) {
                    img[x][y] += dt * osherrudin[x][y];
                    int conv = (int) img[x + 1][y + 1];
                    image.setRGB(x, y, (conv << 16 | conv << 8 | conv));
                    conv = (conv < 0) ? 0 : conv;
                    conv = (conv > 255) ? 255 : conv;
                    int rgb = new Color(conv, conv, conv).getRGB();
                }
            }
            double poc = (i * 100) / nombreIterations;
            System.out.println(poc + "%" + " de traitement.");
        }
        System.out.println("traitement de OsherRudin est finit.");
        try {
            File file_out = new File("OsherRudin.jpg");
            output = image.getSubimage(1, 1, image.getWidth() - 2, image.getHeight() - 2);
            ImageIO.write(output, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // Fonction sign
    private int sign(double x) {
        int signe = 0;
        if (x < 0) {
            signe = -1;
        } else if (x > 0) {
            signe = 1;
        }
        return signe;
    }
}

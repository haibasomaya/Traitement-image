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

public class Malik_Perona {

    public BufferedImage source;
    public BufferedImage image_out;
    public BufferedImage output;

    double img[][];
    double img_xx[][];
    double img_yy[][];
    double sum[][];
    double temp[][];

    public static int i, x, y;

    public BufferedImage anisodiff(double K, int nbr) {
        double lambda = 0.25;
        try {
            source = ImageIO.read(Photos.getFile());
            image_out = source;
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = source.getWidth();
        int height =  source.getHeight();
        //gradient_arrays
        double D_S[][] = new double[width][height];
        double D_N[][] = new double[width][height];
        double D_W[][] = new double[width][height];
        double D_E[][] = new double[width][height];
        //gradien_image_arrays
        double C_S[][] = new double[width][height];
        double C_N[][] = new double[width][height];
        double C_W[][] = new double[width][height];
        double C_E[][] = new double[width][height];
        //traitement de débruitage
        img = new double[width][height];
        img_xx = new double[width][height];
        img_yy = new double[width][height];
        sum = new double[width][height];

        double delta_t = 0.5;
        int j, k;
        for (j = 0; j < source.getWidth(); j++) {
            for (k = 0; k < source.getHeight(); k++) {
                {
                    img[j][k] = new Color(source.getRGB(j, k)).getRed();
                }
            }
        }
        for (i = 0; i < nbr; i++) {
            for (x = 0; x < image_out.getWidth(); x++) {
                for (y = 0; y < image_out.getHeight(); y++) {

                    if (x == 0 || x == image_out.getWidth() - 1 || y == 0 || y == image_out.getHeight() - 1) {
                        img_xx[x][y] = img_yy[x][y] = img[x][y] = 0;
                    }// Image boundary cleared
                    else {
                        //calculate gradient_array values
                        D_N[x][y] = img[x - 1][y] - img[x][y];
                        D_S[x][y] = img[x + 1][y] - img[x][y];

                        D_W[x][y] = img[x][y - 1] - img[x][y];
                        D_E[x][y] = img[x][y + 1] - img[x][y];
                        //calculate gradient_array image values
                        C_N[x][y] = seuillage_fraction(Math.abs(D_N[x][y]), K);
                        C_S[x][y] = seuillage_fraction(Math.abs(D_S[x][y]), K);
                        C_W[x][y] = seuillage_fraction(Math.abs(D_W[x][y]), K);
                        C_E[x][y] = seuillage_fraction(Math.abs(D_E[x][y]), K);
                        //construction des pixels de l'image output (fina)
                        sum[x][y] = D_N[x][y] * C_N[x][y] + D_S[x][y] * C_S[x][y] + D_W[x][y] * C_W[x][y] + D_E[x][y] * C_E[x][y];
                    }
                }
            }

            //Construction de l'image
            for (x = 0; x < image_out.getWidth(); x++) {
                for (y = 0; y < image_out.getHeight(); y++) {
                    if (x == 0 || x == image_out.getWidth() - 1 || y == 0 || y == image_out.getHeight() - 1) {
                        sum[x][y] = img[x][y] = 0;
                    }// Image boundary cleared
                    else {
                        img[x][y] += lambda * sum[x][y];
                    }
                    int conv = (int) img[x][y];
                    image_out.setRGB(x, y, (conv << 16 | conv << 8 | conv));
                    conv = (conv < 0) ? 0 : conv;
                    conv = (conv > 255) ? 255 : conv;
                    int rgb = new Color(conv, conv, conv).getRGB();
                    image_out.setRGB(x, y, rgb);
                }
            }
            double poc = (i * 100) / nbr;
            System.out.println(poc + "%" + " traitement.");
        }
        System.out.println("traitement de Malik_Perona est finit.");
        

        try {
            File file_out = new File("Malik_Perona.jpg");
            image_out = image_out.getSubimage(1, 1, source.getWidth() - 2, source.getHeight() - 2);
            ImageIO.write(image_out, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
        return image_out;
    }

    public static double seuillage_fraction(double gradient, double K) {

        return (1 / (1 + Math.pow(Math.abs(gradient) / K, 2)));
    }

    public static double seuillage_exp(double gradient, double K) {

        return (Math.exp(-(Math.pow(Math.abs(gradient) / K, 2))));
    }

}

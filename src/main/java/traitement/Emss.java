package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Emss {

    BufferedImage image;
    double[][] cadreDePixel = new double[3][3];

    public void run(int nombreIterations) {

        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage EMSS_image = new BufferedImage(width, height, TYPE_INT_RGB);
        EMSS_image = image;

        double[][] img1 = new double[width][height];

        BufferedImage outputImg = new BufferedImage(width, height, TYPE_INT_RGB);

        outputImg = EMSS_image;

        for (int i = 0; i < EMSS_image.getWidth(); i++) {
            for (int j = 0; j < EMSS_image.getHeight(); j++) {
                int pixel = outputImg.getRGB(i, j);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r + g + b) / 3;
                img1[i][j] = moyen_pixel;

            }
        }
        double[][] emss = new double[width][height];

        for (int k = 0; k < nombreIterations; k++) {
            for (int i = 1; i < EMSS_image.getWidth() - 1; i++) {
                for (int j = 1; j < EMSS_image.getHeight() - 1; j++) {

                    cadreDePixel[0][0] = img1[i - 1][j - 1];
                    cadreDePixel[0][1] = img1[i - 1][j];
                    cadreDePixel[0][2] = img1[i - 1][j + 1];
                    cadreDePixel[1][0] = img1[i][j - 1];
                    cadreDePixel[1][1] = img1[i][j];
                    cadreDePixel[1][2] = img1[i][j + 1];
                    cadreDePixel[2][0] = img1[i + 1][j - 1];
                    cadreDePixel[2][1] = img1[i + 1][j];
                    cadreDePixel[2][2] = img1[i + 1][j + 1];

                    double x, y, xx, yy, xy;

                    x = Direve.convolutionMatrice(Direve.MX, cadreDePixel, 3, 3);
                    y = Direve.convolutionMatrice(Direve.MY, cadreDePixel, 3, 3);
                    xx = Direve.convolutionMatrice(Direve.MXX, cadreDePixel, 3, 3);
                    yy = Direve.convolutionMatrice(Direve.MYY, cadreDePixel, 3, 3);
                    xy = Direve.convolutionMatrice(Direve.MXY, cadreDePixel, 3, 3);

                    emss[i][j] = ((xx * y * y - 2 * xy * x * y + yy * x * x) / (x * x + y * y));

                }
            }

            for (int i = 1; i < EMSS_image.getWidth() - 1; i++) {
                for (int j = 1; j < EMSS_image.getHeight() - 1; j++) {

                    img1[i][j] += 0.25 * emss[i][j];
                }

            }

            for (int i = 0; i < EMSS_image.getWidth(); i++) {
                for (int j = 0; j < EMSS_image.getHeight(); j++) {

                    int conv = (int) img1[i][j];
                    outputImg.setRGB(i, j, (conv << 16 | conv << 8 | conv));
                    conv = (conv < 0) ? 0 : conv;
                    conv = (conv > 255) ? 255 : conv;
                    int rgb = new Color(conv, conv, conv).getRGB();
                    outputImg.setRGB(i, j, rgb);

                }
            }
            double poc = (k * 100) / nombreIterations;
            System.out.println(poc + "%" + " de traitement");
        }
        System.out.println("traitement de EMSS est finit ");

        try {
            File file_out = new File("image_EMSS.jpg");
            outputImg = outputImg.getSubimage(1, 1, outputImg.getWidth() - 2, outputImg.getHeight() - 2);
            ImageIO.write(outputImg, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}

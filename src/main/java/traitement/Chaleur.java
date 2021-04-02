package traitement;

import bin.Photos;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.Color;

public class Chaleur {

    public void run(int nombreIterations) {
        Direve direve = new Direve();
        BufferedImage image = null, outputImg;

        double delta_t = 0.25;
        double img_yy[][];
        double img_xx[][];
        double img1[][];
        double sum[][];

        int i, j, n, x, y;
        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = image.getWidth();
        int height = image.getHeight();

        img1 = new double[image.getWidth() + 2][image.getHeight() + 2];
        sum = new double[image.getWidth() + 2][image.getHeight() + 2];

        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {
                {
                    img1[i + 1][j + 1] = new Color(image.getRGB(i, j)).getRed();
                }
            }
        }
        for (n = 0; n < nombreIterations; n++) {
            img_xx = direve.ImageXX(img1, width, height);
            img_yy = direve.ImageYY(img1, width, height);
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {
                    sum[x][y] = img_xx[x][y] + img_yy[x][y];
                }
            }
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {
                    if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                        sum[x][y] = img1[x][y] = 0;
                    } else {
                        img1[x][y] += delta_t * sum[x][y];
                        int conv = (int) img1[x + 1][y + 1];
                        image.setRGB(x, y, (conv << 16 | conv << 8 | conv));
                        conv = (conv < 0) ? 0 : conv;
                        conv = (conv > 255) ? 255 : conv;
                        int rgb = new Color(conv, conv, conv).getRGB();
                        image.setRGB(x, y, rgb);
                    }
                }
            }
            double poc = (n * 100) / nombreIterations;
            System.out.println(poc + "%" + " traitement de Chaleur");
        }
        System.out.println("traitement de Chaleur esr finit ");
        try {
            File file_out = new File("Chaleur.jpg");
            outputImg = image.getSubimage(1, 1, width - 2, height - 2);
            ImageIO.write(image, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

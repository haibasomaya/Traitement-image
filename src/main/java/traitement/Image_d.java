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
import javax.imageio.ImageIO;

/**
 *
 * @author Acer
 */
public class Image_d {

    Direve direve = new Direve();

    public void run(int d) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            System.err.println(e);
        }
        BufferedImage outputImg = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        double[][] img = new double[image.getWidth() + 2][image.getHeight() + 2];
        double[][] output = new double[image.getWidth() + 2][image.getHeight() + 2];
        int lig = image.getWidth();
        int col = image.getHeight();

        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                {
                    img[j + 1][k + 1] = new Color(image.getRGB(j, k)).getRed();
                }
            }
        }
        System.out.println(d + " traitement.Image_d.run()" + img.length);
        
        String name = "";
        switch (d) {
            case 1:
                output = direve.ImageX(img, lig, col);
                name = "dirivé_Ix.jpg";
                break;
            case 2:
                output = direve.ImageY(img, lig, col);
                name = "dirivé_Iy.jpg";
                break;
            case 3:
                output = direve.ImageXX(img, lig, col);
                name = "dirivé_Ixx.jpg";
                break;
            /* case 4:
                output = direve.ImageXY(img, lig, col);
                break;*/
            case 5:
                output = direve.ImageYY(img, lig, col);
                name = "dirivé_Iyy.jpg";
                break;
            default:
                break;
        }
        for (int x = 0; x < lig; x++) {
            for (int y = 0; y < col; y++) {
                int conv = (int) output[x][y];
                if (conv >= 255) {
                    conv = 255;
                } else if (conv <= 0) {
                    conv = 0;
                }
                Color pix = new Color(conv, conv, conv);
                outputImg.setRGB(x, y, pix.getRGB());
            }
        }
        try {
            File file_out = new File(name);
            outputImg = outputImg.getSubimage(1, 1, outputImg.getWidth() - 2, outputImg.getHeight() - 2);
            ImageIO.write(outputImg, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}

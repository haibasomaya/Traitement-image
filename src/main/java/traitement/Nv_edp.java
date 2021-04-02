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
import static javafx.scene.input.KeyCode.O;
import javax.imageio.ImageIO;

/**
 *
 * @author Acer
 */
public class Nv_edp {

    BufferedImage image = null, outputImg = null;
    Direve direve = new Direve();
    double Ix_R[][], Iy_R[][], Ixx_R[][], Iyy_R[][], Ixy_R[][];
    double Ix_V[][], Iy_V[][], Ixx_V[][], Iyy_V[][], Ixy_V[][];
    double Ix_B[][], Iy_B[][], Ixx_B[][], Iyy_B[][], Ixy_B[][];
    double dt = 0.5, ka;

    public void run(int N) {

        // Lecture de l'image
        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        double placien[][] = new double[width + 2][height + 2];

        double G11[][] = new double[width + 4][height + 4];
        double G12[][] = new double[width + 4][height + 4];
        double G22[][] = new double[width + 4][height + 4];

        double img_Rouge[][] = new double[width + 4][height + 4];
        double img_Vert[][] = new double[width + 4][height + 4];
        double img_Bleu[][] = new double[width + 4][height + 4];

        double grad_R[][] = new double[width + 4][height + 4];
        double grad_B[][] = new double[width + 4][height + 4];
        double grad_V[][] = new double[width + 4][height + 4];

        double teta_R[][] = new double[width + 4][height + 4];
        double teta_B[][] = new double[width + 4][height + 4];
        double teta_V[][] = new double[width + 4][height + 4];

        double ksi_R[][] = new double[width + 4][height + 4];
        double ksi_B[][] = new double[width + 4][height + 4];
        double ksi_V[][] = new double[width + 4][height + 4];

        // r?cup?ration des pix R,G,B 
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                img_Rouge[j + 2][k + 2] = r;
                img_Vert[j + 2][k + 2] = g;
                img_Bleu[j + 2][k + 2] = b;
            }
        }

        for (int i = 0; i <= N; i++) {
            Ix_R = direve.ImageX(img_Rouge, width, height);
            Iy_R = direve.ImageY(img_Rouge, width, height);
            Ixy_R = direve.ImageXY(img_Rouge, width, height);
            Ixx_R = direve.ImageXX(img_Rouge, width, height);
            Iyy_R = direve.ImageYY(img_Rouge, width, height);

            Ix_V = direve.ImageX(img_Vert, width, height);
            Iy_V = direve.ImageY(img_Vert, width, height);
            Ixy_V = direve.ImageXY(img_Vert, width, height);
            Ixx_V = direve.ImageXX(img_Vert, width, height);
            Iyy_V = direve.ImageYY(img_Vert, width, height);

            Ix_B = direve.ImageX(img_Bleu, width, height);
            Iy_B = direve.ImageY(img_Bleu, width, height);
            Ixy_B = direve.ImageXY(img_Bleu, width, height);
            Ixx_B = direve.ImageXX(img_Bleu, width, height);
            Iyy_B = direve.ImageYY(img_Bleu, width, height);

            for (int x = 1; x < width - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    G11[x][y] = Ix_R[x][y] * Ix_R[x][y] + Ix_V[x][y] * Ix_V[x][y] + Ix_B[x][y] * Ix_B[x][y];
                    G12[x][y] = Iy_R[x][y] * Ix_R[x][y] + Iy_V[x][y] * Ix_V[x][y] + Iy_B[x][y] * Ix_B[x][y];
                    G22[x][y] = Iy_R[x][y] * Iy_R[x][y] + Iy_V[x][y] * Iy_V[x][y] + Iy_B[x][y] * Iy_B[x][y];

                    //laplacien
                    double A = (Ix_R[x][y] * Ix_R[x][y]) - (Iy_R[x][y] * Iy_R[x][y]) + (Ix_V[x][y] * Ix_V[x][y]) - (Iy_V[x][y] * Iy_V[x][y]) + (Ix_B[x][y] * Ix_B[x][y]) - (Iy_B[x][y] * Iy_B[x][y]);
                    double B = (Ix_R[x][y] * Iy_R[x][y]) + (Ix_V[x][y] * Iy_V[x][y]) + (Ix_B[x][y] * Iy_B[x][y]);
                    placien[x][y] = A * A + 4 * B * B;
                    if (placien[x][y] == 0.0) {
                        placien[x][y] = 1;
                    }

                    //grad
                    grad_R[x][y] = (Ix_R[x][y] * Ix_R[x][y] + Iy_R[x][y] * Iy_R[x][y]);
                    grad_V[x][y] = (Ix_V[x][y] * Ix_V[x][y] + Iy_V[x][y] * Iy_R[x][y]);
                    grad_B[x][y] = (Ix_B[x][y] * Ix_B[x][y] + Iy_B[x][y] * Iy_B[x][y]);
                    if (grad_R[x][y] == 0.0) {
                        grad_R[x][y] = 0.00001;
                    }
                    if (grad_V[x][y] == 0.0) {
                        grad_V[x][y] = 0.00001;
                    }
                    if (grad_B[x][y] == 0.0) {
                        grad_B[x][y] = 0.00001;
                    }

                    ksi_R[x][y] = (Ixx_R[x][y] * Iy_R[x][y] * Iy_R[x][y] - 2 * Ixy_R[x][y] * Ix_R[x][y] * Iy_R[x][y] + Iyy_R[x][y] * Ix_R[x][y] * Ix_R[x][y]) / grad_R[x][y];
                    ksi_V[x][y] = (Ixx_V[x][y] * Iy_V[x][y] * Iy_V[x][y] - 2 * Ixy_V[x][y] * Ix_V[x][y] * Iy_V[x][y] + Iyy_V[x][y] * Ix_V[x][y] * Ix_V[x][y]) / grad_V[x][y];
                    ksi_B[x][y] = (Ixx_B[x][y] * Iy_B[x][y] * Iy_B[x][y] - 2 * Ixy_B[x][y] * Ix_B[x][y] * Iy_B[x][y] + Iyy_B[x][y] * Ix_B[x][y] * Ix_B[x][y]) / grad_B[x][y];

                    teta_R[x][y] = (Ixx_R[x][y] * Ix_R[x][y] * Ix_R[x][y] + 2 * Ixy_R[x][y] * Ix_R[x][y] * Iy_R[x][y] + Iyy_R[x][y] * Iy_R[x][y] * Iy_R[x][y]) / grad_R[x][y];
                    teta_V[x][y] = (Ixx_V[x][y] * Ix_V[x][y] * Ix_V[x][y] + 2 * Ixy_V[x][y] * Ix_V[x][y] * Iy_V[x][y] + Iyy_V[x][y] * Iy_V[x][y] * Iy_V[x][y]) / grad_V[x][y];
                    teta_B[x][y] = (Ixx_B[x][y] * Ix_B[x][y] * Ix_B[x][y] + 2 * Ixy_B[x][y] * Ix_B[x][y] * Iy_B[x][y] + Iyy_B[x][y] * Iy_B[x][y] * Iy_B[x][y]) / grad_B[x][y];

                    //clacule de k
                    double landaPLUS = (G11[x][y] + G22[x][y] + Math.sqrt(Math.pow((G11[x][y] - G22[x][y]), 2) + 4 * Math.pow(G12[x][y], 2))) / 2;
                    double D = 1 / Math.sqrt((Math.pow(G12[x][y], 2) + Math.pow(G11[x][y] - landaPLUS, 2)));
                    if (D == 0.0) {
                        D = 1;
                    }
                    double Ksi_x = (G11[x][y] - landaPLUS) * D;
                    double Ksi_y = G12[x][y] * D;

                    double dx = ((G11[x][y] - G22[x][y]) * ((Ixx_R[x][y] * Iy_R[x][y] + Ixx_V[x][y] * Iy_V[x][y] + Ixx_B[x][y] * Iy_B[x][y]) + (Ixy_R[x][y] * Ix_R[x][y] + Ixy_V[x][y] * Ix_V[x][y] + Ixy_B[x][y] * Ix_B[x][y])) - 2 * G12[x][y] * ((Ixx_R[x][y] * Ix_R[x][y] + Ixx_V[x][y] * Ix_V[x][y] + Ixx_B[x][y] * Ix_B[x][y]) - (Ixy_R[x][y] * Iy_R[x][y] + Ixy_V[x][y] * Iy_V[x][y] + Ixy_B[x][y] * Iy_B[x][y]))) / placien[x][y];
                    double dy = ((G11[x][y] - G22[x][y]) * ((Iyy_R[x][y] * Ix_R[x][y] + Iyy_V[x][y] * Ix_V[x][y] + Iyy_B[x][y] * Ix_B[x][y]) + (Ixy_R[x][y] * Iy_R[x][y] + Ixy_B[x][y] * Iy_B[x][y] + Ixy_V[x][y] * Iy_V[x][y])) + 2 * G12[x][y] * ((Iyy_R[x][y] * Iy_R[x][y] + Iyy_V[x][y] * Iy_V[x][y] + Iyy_B[x][y] * Iy_B[x][y]) - (Ixy_R[x][y] * Ix_R[x][y] + Ixy_B[x][y] * Ix_B[x][y] + Ixy_V[x][y] * Ix_V[x][y]))) / placien[x][y];

                    ka = Ksi_x * dx + Ksi_y * dy;

                    img_Rouge[x][y] += dt * (teta_R[x][y] * (grad_R[x][y]) + ksi_R[x][y] * FctG(ka));
                    img_Bleu[x][y] += dt * (teta_B[x][y] * (grad_B[x][y]) + ksi_B[x][y] * FctG(ka));
                    img_Vert[x][y] += dt * (teta_V[x][y] * (grad_V[x][y]) + ksi_V[x][y] * FctG(ka));

                }
            }

            // Creation de l'image, nouveau pixel vers l'img
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int r = (int) img_Rouge[x + 2][y + 2];
                    int g = (int) img_Vert[x + 2][y + 2];
                    int b = (int) img_Bleu[x + 2][y + 2];
                    if (r > 255) {
                        r = 255;
                    } else if (r < 0) {
                        r = 0;
                    }

                    if (b > 255) {
                        b = 255;
                    } else if (b < 0) {
                        b = 0;
                    }

                    if (g < 0) {
                        g = 0;
                    } else if (g > 255) {
                        g = 255;
                    }

                    Color c = new Color(r, g, b);
                    image.setRGB(x, y, c.getRGB());
                }
            }
            double poc = (i * 100) / N;
            System.out.println(poc + "%" + "de traitement");
        }
        System.out.println("traitement de Nv_edp esr finit ");

        try {
            File file_out = new File("NV_edp.jpg");
            outputImg = image.getSubimage(1, 1, width - 2, height - 2);
            ImageIO.write(image, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static double FctG(double k) {

        return Math.exp(-Math.pow((k), 2) / 16);
    }
}

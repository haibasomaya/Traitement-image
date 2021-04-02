package traitement;

import bin.Photos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Derich {

    BufferedImage image = null, outputImg = null;
    Direve direve = new Direve();
    double Ix[][], Iy[][], Ixx[][], Iyy[][], Ixy[][];
    int i, x, y, j, k;
    double dt = 0.5;

    public void run(int nombreIterations) {
        // Lecture de l'image
        try {
            image = ImageIO.read(Photos.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        double SapirR[][] = new double[width + 4][height + 4];
        double SapirV[][] = new double[width + 4][height + 4];
        double SapirB[][] = new double[width + 4][height + 4];
        double img_Rouge[][] = new double[width + 4][height + 4];
        double img_Vert[][] = new double[width + 4][height + 4];
        double img_Bleu[][] = new double[width + 4][height + 4];
        double Sapir_Rouge[][] = new double[width + 4][height + 4];
        double Sapir_Vert[][] = new double[width + 4][height + 4];
        double Sapir_Bleu[][] = new double[width + 4][height + 4];
        double G11[][] = new double[width + 4][height + 4];
        double G12[][] = new double[width + 4][height + 4];
        double G22[][] = new double[width + 4][height + 4];
        double emss_R[][] = new double[width + 4][height + 4];
        double emss_B[][] = new double[width + 4][height + 4];
        double emss_V[][] = new double[width + 4][height + 4];

        // r?cup?ration des pix R,G,B 
        for (j = 0; j < width; j++) {
            for (k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                img_Rouge[j + 2][k + 2] = r;
                img_Vert[j + 2][k + 2] = g;
                img_Bleu[j + 2][k + 2] = b;
            }
        }

        for (i = 0; i < nombreIterations; i++) {

            Ix = direve.ImageX(img_Bleu, width, height);
            Iy = direve.ImageY(img_Bleu, width, height);
            Ixy = direve.ImageXY(img_Bleu, width, height);
            Ixx = direve.ImageXX(img_Bleu, width, height);
            Iyy = direve.ImageYY(img_Bleu, width, height);
// COUCHE ROUGE -------------------------------------------------------------------------------------------------
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {
                    G11[x][y] = Ix[x][y] * Ix[x][y];
                    G12[x][y] = Ix[x][y] * Iy[x][y];
                    G22[x][y] = Iy[x][y] * Iy[x][y];
                    double landaPLUS = (G11[x][y] + G22[x][y] + Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    double landaMOINS = (G11[x][y] + G22[x][y] - Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    SapirR[x][y] = Math.sqrt(landaPLUS);
                    emss_R[x][y] = (Ixx[x][y] * Iy[x][y] * Iy[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Ix[x][y] * Ix[x][y]) / (Ix[x][y] * Ix[x][y] + Iy[x][y] * Iy[x][y]);
                    Sapir_Rouge[x][y] = (Ixx[x][y] * Ix[x][y] * Ix[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Iy[x][y] * Iy[x][y]) / (x * x + y * y);
                    Sapir_Rouge[x][y] = (fctSeuille(SapirV[x][y]) * Sapir_Vert[x][y] + emss_V[x][y]);
                }
            }
            // Traitement  R 			
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    img_Rouge[x][y] += dt * Sapir_Rouge[x][y];
                }
            }
//---------------------------------------------------------------------------------------------------------------------

// COUCHE VERT --------------------------------------------------------------------------------------------------------
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {
                    G11[x][y] = Ix[x][y] * Ix[x][y];
                    G12[x][y] = Ix[x][y] * Iy[x][y];
                    G22[x][y] = Iy[x][y] * Iy[x][y];
                    double landaPLUS = (G11[x][y] + G22[x][y] + Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    double landaMOINS = (G11[x][y] + G22[x][y] - Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    SapirV[x][y] = Math.sqrt(landaPLUS);
                    emss_V[x][y] = (Ixx[x][y] * Iy[x][y] * Iy[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Ix[x][y] * Ix[x][y]) / (Ix[x][y] * Ix[x][y] + Iy[x][y] * Iy[x][y]);
                    Sapir_Vert[x][y] = (Ixx[x][y] * Ix[x][y] * Ix[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Iy[x][y] * Iy[x][y]) / (x * x + y * y);
                    Sapir_Vert[x][y] = fctSeuille(SapirV[x][y]) * Sapir_Vert[x][y] + emss_V[x][y];
                }
            }
            // Traitement  R 			
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    img_Vert[x][y] += dt * Sapir_Vert[x][y];
                }
            }
//---------------------------------------------------------------------------------------------------------------------

// COUCHE BLUE --------------------------------------------------------------------------------------------------------
            for (x = 1; x < width - 1; x++) {
                for (y = 1; y < height - 1; y++) {
                    G11[x][y] = Ix[x][y] * Ix[x][y];
                    G12[x][y] = Ix[x][y] * Iy[x][y];
                    G22[x][y] = Iy[x][y] * Iy[x][y];
                    double landaPLUS = (G11[x][y] + G22[x][y] + Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    double landaMOINS = (G11[x][y] + G22[x][y] - Math.sqrt(((G11[x][y] - G22[x][y]) * (G11[x][y] - G22[x][y])) + 4 * G12[x][y] * G12[x][y])) / 2;
                    SapirB[x][y] = Math.sqrt(landaPLUS);
                    emss_B[x][y] = (Ixx[x][y] * Iy[x][y] * Iy[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Ix[x][y] * Ix[x][y]) / (Ix[x][y] * Ix[x][y] + Iy[x][y] * Iy[x][y]);
                    Sapir_Bleu[x][y] = (Ixx[x][y] * Ix[x][y] * Ix[x][y] - 2 * Ixy[x][y] * Ix[x][y] * Iy[x][y] + Iyy[x][y] * Iy[x][y] * Iy[x][y]) / (x * x + y * y);
                    Sapir_Bleu[x][y] = fctSeuille(SapirV[x][y]) * Sapir_Vert[x][y] + emss_V[x][y];
                }
            }
            // Traitement  R 			
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    img_Bleu[x][y] += dt * Sapir_Bleu[x][y];
                }
            }
//---------------------------------------------------------------------------------------------------------------------
            // Cr?ation de l'image, nouveau pixel vers l'img
            for (x = 0; x < width; x++) {
                for (y = 0; y < height; y++) {
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

            double poc = (i * 100) / nombreIterations;
            System.out.println(poc + "%" + "de traitement");
        }
        System.out.println("traitement de Derich esr finit ");
        try {
            File file_out = new File("Derich.jpg");
            outputImg = image.getSubimage(1, 1, width - 2, height - 2);
            ImageIO.write(image, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

// Pour ?liminer grad s'il est fort
    private double fctSeuille(double g) {
        return 1 / (1 + Math.pow(g, 2));
    }

}

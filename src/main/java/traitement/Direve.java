package traitement;

public class Direve {

//Descritisation Matrice	
    public static double[][] MX = {{0.1464466, 0.0, -0.1464466}, {0.2071067, 0.0, -0.2071067}, {0.1464466, 0.0, -0.1464466}};

    public static double[][] MY = {{0.1464466, 0.2071067, 0.1464466}, {0.0, 0.0, 0.0}, {-0.1464466, -0.2071067, -0.1464466}};

    public static double[][] MXX = {{0.25, -0.5, 0.25}, {0.5, -1.0, 0.5}, {0.25, -0.5, 0.25}};

    public static double[][] MYY = {{0.25, 0.5, 0.25}, {-0.5, -1.0, -0.5}, {0.25, 0.5, 0.25}};

    public static double[][] MXY = {{0.25, 0.0, -0.25}, {0.0, 0.0, 0.0}, {-0.25, 0.0, 0.25}};

    public double[][] ImageX(double img[][], int lig, int col) {

        double Ix[][] = new double[lig][col];
        for (int x = 1; x < lig; x++) {
            for (int y = 1; y < col; y++) {
                Ix[x][y] = (MX[0][0] * img[x - 1][y - 1])
                        + (MX[0][1] * img[x - 1][y])
                        + (MX[0][2] * img[x - 1][y + 1])
                        + (MX[1][0] * img[x][y - 1])
                        + (MX[1][1] * img[x][y])
                        + (MX[1][2] * img[x][y + 1])
                        + (MX[2][0] * img[x + 1][y - 1])
                        + (MX[2][1] * img[x + 1][y])
                        + (MX[2][2] * img[x + 1][y + 1]);
            }
        }

        return Ix;
    }

    public double[][] ImageY(double img[][], int lig, int col) {

        double Iy[][] = new double[lig][col];
        for (int x = 1; x < lig - 1; x++) {
            for (int y = 1; y < col - 1; y++) {
                Iy[x][y] = (MY[0][0] * img[x - 1][y - 1])
                        + (MY[0][1] * img[x - 1][y])
                        + (MY[0][2] * img[x - 1][y + 1])
                        + (MY[1][0] * img[x][y - 1])
                        + (MY[1][1] * img[x][y])
                        + (MY[1][2] * img[x][y + 1])
                        + (MY[2][0] * img[x + 1][y - 1])
                        + (MY[2][1] * img[x + 1][y])
                        + (MY[2][2] * img[x + 1][y + 1]);
            }
        }
        
        
        return Iy;
    }

    public double[][] ImageXX(double img[][], int lig, int col) {

        double Ixx[][] = new double[lig][col];
        for (int x = 1; x < lig - 1; x++) {
            for (int y = 1; y < col - 1; y++) {
                Ixx[x][y] = (MXX[0][0] * img[x - 1][y - 1]) //<------taytiha liya fhad steer 
                        + (MXX[0][1] * img[x - 1][y])
                        + (MXX[0][2] * img[x - 1][y + 1])
                        + (MXX[1][0] * img[x][y - 1])
                        + (MXX[1][1] * img[x][y])
                        + (MXX[1][2] * img[x][y + 1])
                        + (MXX[2][0] * img[x + 1][y - 1])
                        + (MXX[2][1] * img[x + 1][y])
                        + (MXX[2][2] * img[x + 1][y + 1]);
            }
        }

        return Ixx;
    }

    public double[][] ImageYY(double img[][], int lig, int col) {

        double Iyy[][] = new double[lig][col];
        for (int x = 1; x < lig - 1; x++) {
            for (int y = 1; y < col - 1; y++) {
                Iyy[x][y] = (MYY[0][0] * img[x - 1][y - 1])
                        + (MYY[0][1] * img[x - 1][y])
                        + (MYY[0][2] * img[x - 1][y + 1])
                        + (MYY[1][0] * img[x][y - 1])
                        + (MYY[1][1] * img[x][y])
                        + (MYY[1][2] * img[x][y + 1])
                        + (MYY[2][0] * img[x + 1][y - 1])
                        + (MYY[2][1] * img[x + 1][y])
                        + (MYY[2][2] * img[x + 1][y + 1]);
            }
        }
        return Iyy;
    }

    public double[][] ImageXY(double img[][], int lig, int col) {
        double Ixy[][] = new double[lig][col];
        for (int x = 1; x < lig - 1; x++) {
            for (int y = 1; y < col - 1; y++) {
                Ixy[x][y] = (MXY[0][0] * img[x - 1][y - 1])
                        + (MXY[0][1] * img[x - 1][y])
                        + (MXY[0][2] * img[x - 1][y + 1])
                        + (MXY[1][0] * img[x][y - 1])
                        + (MXY[1][1] * img[x][y])
                        + (MXY[1][2] * img[x][y + 1])
                        + (MXY[2][0] * img[x + 1][y - 1])
                        + (MXY[2][1] * img[x + 1][y])
                        + (MXY[2][2] * img[x + 1][y + 1]);
            }
        }
        return Ixy;
    }

    public static double convolutionMatrice(double[][] M1, double[][] M2, int x, int y) {
        double somme = 0;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                somme += M1[i][j] * M2[i][j];
            }
        }
        return somme;
    }

}



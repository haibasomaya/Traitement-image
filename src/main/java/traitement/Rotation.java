/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traitement;

import In_controller.Traitement_image;
import bin.Photos;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Acer
 */
public class Rotation {

    public void rotation(File file, int angle) {

        BufferedImage imgIn = null;
        try {
            imgIn = ImageIO.read(Photos.getFile());
        } catch (IOException ex) {
            Logger.getLogger(Rotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage imgIn2 = new BufferedImage(imgIn.getHeight(), imgIn.getWidth(), imgIn.getType());
        int w = imgIn.getWidth();
        int h = imgIn.getHeight();

        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), w / 2, h / 2);
        // tx.rotate(Math.PI / 2, imgIn2.getWidth() / 2, imgIn.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(imgIn, imgIn2);

        try {
            File file_out = new File("rotation.jpg");
//            imgIn2 = imgIn.getSubimage(1, 1, imgIn.getWidth(), imgIn.getHeight());
            ImageIO.write(imgIn2, "jpg", file_out);
            Photos.setFile(file_out);
        } catch (IOException ex) {
            Logger.getLogger(Traitement_image.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

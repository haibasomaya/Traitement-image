/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import java.io.File;

/**
 *
 * @author Acer
 */
public class Photos {
    private static File file = null;

    public Photos() {
    }

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        Photos.file = file;
    }

}

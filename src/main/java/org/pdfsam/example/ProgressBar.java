/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pdfsam.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.pdfsam.ui.RingProgressIndicator;

/**
 *
 * @author Acer
 */
public class ProgressBar extends Application {

    @Override
    public void start(Stage primaryStage) {

        RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
        ringProgressIndicator.setRingWidth(200);
        ringProgressIndicator.makeIndeterminate();

        StackPane root = new StackPane();
        root.getChildren().add(ringProgressIndicator);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("ATTENDEZ LE RESULTAT S'AFFICHERA DANS QULQUE INSTANT..");
        primaryStage.setScene(scene);
        primaryStage.show();
        new workerTheard(ringProgressIndicator, 1).start();

    }

    /*
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    class workerTheard extends Thread {

        RingProgressIndicator rpi;
        int val;

        public workerTheard(RingProgressIndicator rpi, int v) {
            this.rpi = rpi;
            this.val = v;

        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.runLater(() -> {
                    rpi.setProgress(val);
                });
                val++;
                if (val == 100) {
                    val = 0;
                    break;
                }
            }
            try {
                try {
                    ProgressBar.this.finalize();
                } catch (Throwable ex) {
                    Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

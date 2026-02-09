package cherry.gui;

import javafx.application.Application;

/**
 * Serves as a launcher class to workaround classpath issues.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}

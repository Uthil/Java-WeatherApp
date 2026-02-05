package eap_pli24_ge3.weatherApp.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;


// Set custom icon for the application.
public class AppLogo {

    public static Image setIconImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(AppLogo.class.getResource("/logo128x128.png")));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

}
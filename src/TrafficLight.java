import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * TrafficLight class which allows you to create a TrafficLight and give it different attributes.
 * Will place it on the GUI and will change colors.
 * Part of TrafficLight package written for the final project of CMSC335.
 *
 * Written by Kyle Busey on March 2nd, 2022.
 */

enum TrafficLightColor {
    RED, GREEN, YELLOW
}

public class TrafficLight implements Runnable {

    private TrafficLightColor tlc; // holds the current traffic light color
    private final BufferedImage image;
    private final int trafficLightID;
    private final double position;

    private final AtomicBoolean isActive = new AtomicBoolean(false);

    public TrafficLight(int trafficLightNumber) throws IOException {
        this.trafficLightID = trafficLightNumber;
        this.tlc = TrafficLightColor.RED;
        this.position = calculateLightPosition(trafficLightNumber);
        this.image = ImageIO.read(new File("images/trafficlight.png")); //traffic light picture
    }

    public int getTrafficLightID() {
        return trafficLightID;
    }

    public BufferedImage getImage() {
        return image;
    }

    public double getPosition() {
        return position;
    }

    // Return current color.
    public TrafficLightColor getColor() {
        return tlc;
    }

    public void setLightStatus(int statusID) {
        this.isActive.set(statusID == 1);
    }

    /**
     * Calculate x position based on how many traffic lights are currently on the GUI. Moves X position.
     * @param trafficLightID the number of traffic lights currently.
     * @return X coordinate
     */
    private int calculateLightPosition(int trafficLightID) {
        switch (trafficLightID) {
            case 1: return 150;
            case 2: return 380;
            case 3: return 610;
            case 4: return 850;
        }
        return 0;
    }

    /**
     * Changes traffic light to next color.
     */
    private void changeColor() {
        switch (tlc) {
            case RED -> tlc = TrafficLightColor.GREEN;
            case YELLOW -> tlc = TrafficLightColor.RED;
            case GREEN -> tlc = TrafficLightColor.YELLOW;
        }
    }

    @Override
    public void run() {
        while (isActive.get()) { //while light is on
            try {
                switch (tlc) {
                    case GREEN -> Thread.sleep(10000); // green for 10 seconds
                    case YELLOW -> Thread.sleep(2000);  // yellow for 2 seconds
                    case RED -> Thread.sleep(12000); // red for 12 seconds
                }
            } catch (InterruptedException exc) {
                break;
            }
            changeColor(); //change color after light duration
        }
    }

}
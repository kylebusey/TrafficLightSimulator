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


//colors the light can be
enum TrafficLightColor {
    RED, GREEN, YELLOW
}

class TrafficLight implements Runnable {

    private TrafficLightColor tlc; // holds the current traffic light color
    private BufferedImage image;
    private String trafficLightNumber = "";
    private int x= 0, y=0;
    private Thread thread;

    //atomic variable to check if the light is active
    private AtomicBoolean isActive = new AtomicBoolean(false);


    /**
     * TrafficLight constructor which gives a traffic light a number, sets the color to red, and
     * calculates an X position based on how many traffic lights are currently there.
     * @param count the number of the traffic light.
     * @throws IOException
     */
    public TrafficLight(int count) throws IOException {
        this.trafficLightNumber = String.valueOf(count);
        tlc = TrafficLightColor.RED;
        this.x = calculateXPosition(count);
        this.y = getYPosition();
        this.image = ImageIO.read(new File("images/trafficlight.png")); //traffic light picture
    }


    /**
     * Getter method that returns traffic light number.
     * @return the number of the traffic light
     */
    public String getTrafficLightName() {
        return trafficLightNumber;
    }


    /**
     * Calculate x position based on how many traffic lights are currently on the GUI. Moves X position.
     * @param count the number of traffic lights currently.
     * @return X coordinate
     */
    public int calculateXPosition(int count) {

        switch (count) {
            case 1: return 50;
            case 2: return 200;
            case 3: return 350;
            case 4: return 500;
        }
        return 0;
    }

    /**
     * Getter for x position.
     * @return x position
     */
    public int getXPosition() {
        return x;
    }


    /**
     * Getter for Y position, will always be 0.
     * @return 0
     */
    public int getYPosition() {
        return 0;
    }

    /**
     * Getter method for image of traffic light.
     * @return the image variable.
     */
    public BufferedImage getImage() {
        return image;
    }

    public void suspend() throws InterruptedException {
        isActive.set(false);
    }

    /**
     * Start thread method which creates a new thread and sets it to TrafficLight object and starts it.
     */
    public void startThread() {
        thread = new Thread( this);
        isActive.set(true); //traffic light is now active
        System.out.println("Light thread created");
        thread.start();
    }

    // Start up the light.
    public void run() {
        isActive.set(true);
        while (isActive.get()) { //while traffic light is on
            try {
                switch (tlc) {
                    case GREEN:
                        Thread.sleep(10000); // green for 10 seconds
                        break;
                    case YELLOW:
                        Thread.sleep(2000);  // yellow for 2 seconds
                        break;
                    case RED:
                        Thread.sleep(12000); // red for 12 seconds
                        break;
                }
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }
            changeColor();
        }
    }

    /**
     * Changes color to next color.
     */
    synchronized void changeColor() {
        switch (tlc) {
            case RED:
                tlc = TrafficLightColor.GREEN;
                break;
            case YELLOW:
                tlc = TrafficLightColor.RED;
                break;
            case GREEN:
                tlc = TrafficLightColor.YELLOW;
        }
    }


    // Return current color.
    synchronized TrafficLightColor getColor() {
        return tlc;
    }

}
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Part of TrafficLight package written for the final project of CMSC335.
 *
 * Written by Kyle Busey on March 2nd, 2022.
 */

public class Car implements Runnable {

    private static final double DEFAULT_SPEED = 37.5; //Speed of all cars ran
    private final int carID;

    private final BufferedImage image;
    private double position;

    private final AtomicBoolean redLight = new AtomicBoolean(false); //car is at redlight
    private final AtomicBoolean isActive = new AtomicBoolean(false); //car turned on
    private final AtomicBoolean behindCar = new AtomicBoolean(false); //car cannot move forward

    /**
     * Constructor for car which gives the car an ID number, calculates starting X position based on
     * how many cars are currently on the GUI, and gives car an image.
     */
    public Car(int carID) throws IOException {
        this.carID = carID;
        this.position = calculateStartingPosition(carID);
        this.image = ImageIO.read(new File("images/car.png"));
    }

    public int getCarID() {
        return carID;
    }

    public double getPosition() {
        return position;
    }

    public BufferedImage getImage() {
        return image;
    }

    public AtomicBoolean getBehindCar() {
        return behindCar;
    }

    /**
     * Calculate starting position based on how many cars are on the GUI.
     * @return the x position of the new car
     */
    private double calculateStartingPosition(int carCounter) {

        switch(carCounter) {
            case 1:
                return 0;
            case 2:
                return 75;
            case 3:
                return 200;
            case 4:
                return 275;
            case 5:
                return 450;
            default:
                return 0;
        }
    }

    public void setBehindCar(boolean behindCar) {
        if(behindCar) {
            this.behindCar.set(true);
        } else if(behindCar == false) {
            this.behindCar.set(false);
        }
    }

    public void setCarPosition(double position) {
        this.position = position;
    }

    /**
     * StatusID is set through the GUI buttons which determines whether the cars will turn on
     * @param statusID the status of the components.
     */
    public void setCarStatus(int statusID) {
        if(statusID == 1) {
            this.isActive.set(true);
        } else {
            this.isActive.set(false);
        }
    }

    /**
     * StatusID is set through the GUI buttons which determines whether the cars will turn on
     * @param statusID the status of the components.
     */
    public void setRedLightStatus(int statusID) {
        if(statusID == 1) {
            this.redLight.set(true);
        } else {
            this.redLight.set(false);
        }
    }

    /**
     * Calculates speed of the car, will be 0 if at red light or behind another car but will be default speed if green or yellow light.
     * @return the speed of the car
     */
    public double calculateSpeed() {
        return (redLight.get() || behindCar.get()) ? 0 : DEFAULT_SPEED/10;
    }

    /**
     * Checks if car has gone past the display line and needs to be reset to the beginning.
     * @return true or false based on car location
     */
    private boolean hasExtendedLine() {
        return position > 990;
    }

    /**
     * Move the car object based on the speed of the car.
     */
    private void move() throws InterruptedException {
        position += calculateSpeed();
    }

    @Override
    public void run() {
        while(isActive.get()) { //car is turned on
            while (!hasExtendedLine()) { //move the car while it is listed on the GUI
                try {
                    move();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
            this.setCarPosition(0); //Reset to the beginning of the GUI once extended line
        }
    }

}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 *
 * This class does
 * Part of TrafficLight package written for the final project of CMSC335.
 *
 * Written by Kyle Busey on March 2nd, 2022.
 */


public class Car implements Runnable {


    private static final double DEFAULT_SPEED = 37.5; //default speed of a car

    private BufferedImage image;
    private String carName = "";
    private double xPosition = 0;
    private int yPosition = 0;
    private static double speedX = 0;

    public final AtomicBoolean redLight = new AtomicBoolean(false); //if car is at redlight variable

    private final AtomicBoolean threadActive = new AtomicBoolean(false); //if car thread is active

    private Thread thread;


    /**
     * Constructor for car which gives the car a number, calculates speed, calculates starting X position based on
     * how many cars are currently on the GUI, and gives car an image.
     * @param count the number of the current car
     * @throws IOException
     */
    public Car(int count) throws IOException {
        this.carName = String.valueOf(count);
        this.speedX = calculateSpeed();
        this.xPosition = calculateStartingXPos();
        this.yPosition = 0;
        this.image = ImageIO.read(new File("images/car.png"));
    }


    /**
     * Getter for car name.
     * @return the car name
     */
    public String getCarName() {
        return carName;
    }

    /**
     * Calculate starting X position based on how many cars are on the GUI.
     * @return the x position of the new car
     */
    public synchronized double calculateStartingXPos() {
        switch(this.carName) {
            case "1": {
                return 10;
            }
            case "2": {
                return 140;
            }
            case "3": {
                return 240;
            }
        }
        return 0;
    }

    /**
     * Getter for x position
     * @return x position
     */
    public synchronized double getXPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Getter for y position.
     * @return always returns 0
     */
    public synchronized int getYPosition() {
        return 0;
    }


    /**
     * Calculates speed of the car, will be 0 if at red light but will be default_speed/10 if green or yellow ligth.
     * @return the speed of the car
     */
    public synchronized double calculateSpeed() {
        if(redLight.get()) {
            speedX = 0;
        } else {
            speedX = DEFAULT_SPEED/10;
        }
        return speedX;
    }

    /**
     * getter method for image of the car
     * @return image of the car
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Checks if car has gone past the line and needs to be reset.
     * @return true or false based on car location
     */
    public boolean hasExtendedLine() {
        return this.xPosition > 725;
    }


    /**
     * Creates thread for car and sets thread to active, starts it.
     */
    public void startThread() {
        thread = new Thread( this);
        threadActive.set(true); //car thread is now active
        System.out.println("Car thread created");
        thread.start();
    }

    /**
     * Stops thread by setting threadActive to false.
     * @throws InterruptedException
     */
    public void stopThread() throws InterruptedException {
        threadActive.set(false);
    }

    /**
     * If thread is at redlight and resume is called, set redLight to false (no longer at redlight) and notify.
     */
    public synchronized void resume() {
        if(redLight.get()) {
            redLight.set(false);
            notify();
        }
    }

    /**
     * Move the car object based on the speed of the car.
     */
    private void move() {
        if(hasExtendedLine()) {
            xPosition = 10; //reset x position of car if extended past GUI
        }

        xPosition += calculateSpeed(); //calculate speed based on multiple factors
    }


    /**
     * Overridden run method which allows the car object to function.
     */
    @Override
    public void run() {
        while (threadActive.get()) { //while car is active
            try {
                synchronized (this) {
                    while (redLight.get()) { //wait if stuck at redlight
                        wait();
                    }
                }

                if (threadActive.get()) { //if car is on
                    while (!hasExtendedLine()) {
                        move(); //move car if not past line
                        Thread.sleep(150);
                    }
                }


            } catch (InterruptedException ex) {

            }
        }
    }



} //class

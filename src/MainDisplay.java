import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;


/**
 * MainDisplay method which does the painting of the panel in the GUI.
 */
public class MainDisplay extends JPanel {

    private ArrayList<Car> carList; //list of cars to be painted
    private ArrayList<TrafficLight> trafficLights; //list of lights to be painted

    private static final int CAR_SIZEW = 40; //default size of car width
    private static final int CAR_SIZEH = 30; //default size of car height

    private static final int LIGHT_SIZEW = 50; //default size of light width
    private static final int LIGHT_SIZEH = 50; //default size of light height


    /**
     * Create maindisplay object which gets lists of cars and lights and sets the size of the drawing panel.
     * @param trafficManager the manager which will give lists and dimensions of panel.
     */
    public MainDisplay(TrafficManager trafficManager) {
        super();

        trafficLights = trafficManager.getTrafficLights();
        carList = trafficManager.getCarList();
        this.setSize(trafficManager.getWidth(), trafficManager.getHeight());
    }


    /**
     * Overridden paintComponent method which draws a line for the objects to go on, and paints each TrafficLight and Car object
     * as well as different attributes.
     * @param g the graphics object to use paint.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 40, 700, 40); //main line for the panel
        g.setFont(new Font("Serif", Font.BOLD, 10));


        //draw traffic lights
        synchronized (trafficLights) {
            trafficLights.forEach(trafficLight -> {
                //draw each image of a traffic light and set it to x position and y position as well as the default size
                g.drawImage(trafficLight.getImage(), trafficLight.getXPosition(), trafficLight.getYPosition() - 10, LIGHT_SIZEW, LIGHT_SIZEH, this);
                switch(trafficLight.getColor()) { //set color of string
                    case RED -> g.setColor(Color.RED);
                    case GREEN -> g.setColor(Color.GREEN);
                    case YELLOW -> g.setColor(Color.YELLOW);
                }
                //label for intersection which is colored depending on traffic light color
                g.drawString("Intersection " + trafficLight.getTrafficLightName(), trafficLight.getXPosition() - 10, trafficLight.getYPosition() + 75);

            });
        }

        synchronized (carList) {

            //draw each car
            carList.forEach(car -> {
                //draw each image of a car and set it to x position and y position as well as the default size
                g.drawImage(car.getImage(), (int) car.getXPosition(), car.getYPosition() + 10, CAR_SIZEW, CAR_SIZEH, this);
                g.setColor(Color.BLACK);
                //draw name of car
                g.drawString(car.getCarName(), (int) car.getXPosition() + 15, car.getYPosition() + 50);
                //draw x coordinate
                g.drawString("X: "+ car.getXPosition(), (int) car.getXPosition() + 15, car.getYPosition() + 75);
                //draw speed of car
                g.drawString("Speed: "+ car.calculateSpeed(), (int) car.getXPosition() + 15, car.getYPosition() + 100);
            });
        }

    }



}
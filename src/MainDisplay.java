import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * MainDisplay method which does the painting of the panel in the GUI.
 */
public class MainDisplay extends JPanel {

    private final ArrayList<Car> carList; //cars and lights to be painted
    private final ArrayList<TrafficLight> trafficLights;

    private static final int CAR_SIZEW = 40; //car height and width
    private static final int CAR_SIZEH = 30;

    private static final int LIGHT_SIZEW = 50;
    private static final int LIGHT_SIZEH = 50;


    /**
     * Create maindisplay object which gets lists of cars and lights and sets the size of the drawing panel.
     * @param lightManager the manager which will give lists and dimensions of panel.
     */
    public MainDisplay(LightManager lightManager, CarManager carManager) {
        super();
        trafficLights = lightManager.getTrafficLights();
        carList = carManager.getCarList();
        this.setSize(1000, 400);
    }


    /**
     * Overridden paintComponent method which draws a line for the objects to go on, and paints each TrafficLight and Car object
     * as well as different attributes.
     * @param g the graphics object to use paint.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 40, 1000, 40); //main line for the panel
        g.setFont(new Font("Serif", Font.BOLD, 10));


        //draw traffic lights
            trafficLights.forEach(trafficLight -> {
                //draw each image of a traffic light and set it to x position and y position as well as the default size
                g.drawImage(trafficLight.getImage(), (int) trafficLight.getPosition(),  0, LIGHT_SIZEW, LIGHT_SIZEH, this);
                switch(trafficLight.getColor()) { //set color of string
                    case RED -> g.setColor(Color.RED);
                    case GREEN -> g.setColor(Color.GREEN);
                    case YELLOW -> g.setColor(Color.YELLOW);
                }
                //label for intersection which is colored depending on traffic light color
                g.drawString("Intersection " + trafficLight.getTrafficLightID(), (int) (trafficLight.getPosition() - 10), 75);
            });

            //draw each car
            carList.forEach(car -> {
                //draw each image of a car and set it to x position and y position as well as the default size
                g.drawImage(car.getImage(), (int) car.getPosition(), 10, CAR_SIZEW, CAR_SIZEH, this);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(car.getCarID()), (int) car.getPosition() + 15, 50);
                g.drawString("X: "+ car.getPosition(), (int) car.getPosition() + 15, 75);
                g.drawString("Speed: "+ car.calculateSpeed(), (int) car.getPosition() + 15, 100);
            });


    }



}
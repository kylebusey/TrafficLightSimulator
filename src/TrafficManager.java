import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TrafficManager class which incorporates both TrafficLight and Car classes.
 */

public class TrafficManager implements Runnable {

    private static final int TRAFFIC_LIGHT_LIMIT = 4;
    private static final int CAR_LIMIT = 5;
    private static ArrayList<TrafficLight> trafficLights = new ArrayList<>(); //list of trafficLights
    private static ArrayList<Car> carList = new ArrayList<>(); //list of Cars

    static int trafficLightCount = 0; //traffic light counter
    private static int carCount = 0; //car counter

    private static int width, height;
    private static long deltaMilliSeconds = 0;

    public AtomicBoolean isActive = new AtomicBoolean(false); //if manager is active


    /**
     * Constructor of TrafficManager which takes in a width and height of the drawing area.
     * @param width width of drawingArea
     * @param height height of drawingArea
     */
    public TrafficManager(int width, int height, long deltaMilliSeconds) {
        this.width = width;
        this.height = height;
    }

    /**
     * Getter for width
     * @return width of area
     */
    public static int getWidth() {
        return width;
    }

    /**
     * Getter for height
     * @return height of area
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for carList
     * @return the list of cars
     */
    public ArrayList<Car> getCarList() {
        return carList;
    }

    /**
     * Getter for trafficList
     * @return the list of lights
     */
    public static ArrayList<TrafficLight> getTrafficLights() {
        return trafficLights;
    }


    /**
     * Add Traffic Light method which adds a certain amount of traffic lights to the arrayList and ensures
     * that it doesn't go over the limited amount of lights.
     * @param amountAdded the amount of lights to be added
     * @throws IOException
     */
    public void addTrafficLight(int amountAdded) throws Exception {

        // Initialize variables.
        int added = 0;

        // A synchronized statement that receives the instrinic lock of the ArrayList named "bubbles".

        // While the number of bubbles to add is greater than the number of bubbles already added...

        while (added < amountAdded) {

            trafficLightCount += 1;
            if(trafficLightCount <= TRAFFIC_LIGHT_LIMIT) {
                TrafficLight trafficLight = new TrafficLight(trafficLightCount);

                trafficLights.add(trafficLight);
                added += 1;
            } else {
                throw new Exception("Limit reached for traffic lights.");
            }
        }

    }
    /**
     * Add car method which adds a certain amount of cars to the arrayList and ensures
     * that it doesn't go over the limited amount of cars.
     * @param amountAdded the amount of cars to be added
     * @throws IOException
     */
    public void addCar(int amountAdded) throws Exception {

        // Initialize variables.
        int added = 0;


        // A synchronized statement that receives the instrinic lock of the ArrayList named "bubbles".

        // While the number of bubbles to add is greater than the number of bubbles already added...
        while (added < amountAdded) {
            carCount += 1;
            if(carCount <= CAR_LIMIT) { //if amount of cars doesn't exceed limit
                Car car = new Car(carCount);
                carList.add(car);
                added += 1;
            } else {
                throw new Exception("Limit reached for cars.");
            }
        }

    }


    /**
     * CheckLights method which goes through each traffic light and checks the color. Starts with the default 3
     * and one more can be added if necessary.
     * @throws InterruptedException
     */
    private void checkLights() throws InterruptedException {

        switch (trafficLights.get(0).getColor()) {
            case RED -> { //if light is red
                for (Car car : carList) {
                    if (insideTLOne(car)) { //if light is inside traffic light one
                        car.redLight.set(true);
                    }
                }
            }

            case GREEN -> {
                for (Car car : carList) {
                    if (car.redLight.get()) {
                        car.resume();
                    }

                }
            }


        }
        switch (trafficLights.get(1).getColor()) {
            case RED -> {
                for (Car car : carList) {
                    if (insideTLTwo(car)) { //if light is inside traffic light two
                        car.redLight.set(true);
                    }
                }
            }


            case GREEN -> {
                for (Car car : carList) {
                    if (car.redLight.get()) {
                        car.resume();
                    }
                }
            }
        }
        switch (trafficLights.get(2).getColor()) {
            case RED -> {
                for (Car car : carList) {
                    if (insideTLThree(car)) { //if light is inside traffic light three
                        car.redLight.set(true);
                    }
                }
            }

            case GREEN -> {
                for (Car car : carList) {
                    if (car.redLight.get()) {
                        car.resume();
                    }
                }
            }
        }

        if(trafficLightCount > 3) {
            switch (trafficLights.get(3).getColor()) {
                case RED -> {
                    for (Car car : carList) {
                        if (insideTLFour(car)) {
                            car.redLight.set(true);
                        }
                    }
                }

                case GREEN -> {
                    for (Car car : carList) {
                        if (car.redLight.get()) {
                            car.resume();
                        }
                    }
                }
            }
        }


    }


    /**
     * Checks if car is inside of the range of the lights.
     * @param car the car to be checked
     * @return true or false
     */
    private boolean insideTLOne(Car car) {
        return car.getXPosition() > 0 && car.getXPosition() < 50;
    }

    private boolean insideTLTwo(Car car) {
        return car.getXPosition() > 50 && car.getXPosition() < 200;
    }

    private boolean insideTLThree(Car car) {
        return car.getXPosition() > 200 && car.getXPosition() < 350;
    }

    private boolean insideTLFour(Car car) {
        return car.getXPosition() > 350 && car.getXPosition() < 500;
    }




    @Override
    public void run() {
        isActive.set(true);
        while(isActive.get()) { //while thread is active
            try {
                checkLights(); //check lights over and over again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}

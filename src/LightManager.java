import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TrafficManager class which incorporates both TrafficLight and Car classes.
 */

public class LightManager implements Runnable {

    private static final int TRAFFIC_LIGHT_LIMIT = 4;

    private static final ArrayList<TrafficLight> trafficLights = new ArrayList<>(); //list of trafficLights
    private static final ArrayList<Thread> trafficThreads = new ArrayList<>(); //list of threads
    private static int trafficLightCount = 0; //traffic light counter

    public AtomicBoolean managerActive = new AtomicBoolean(false);

    public LightManager() {
    }

    public static ArrayList<TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    public static ArrayList<Thread> getTrafficThreads() { return trafficThreads; }

    /**
     * CheckLights method which goes through each traffic light and checks the color. Starts with the default 3
     * and one more can be added if necessary.
     * @throws InterruptedException
     */
    private synchronized void checkLights() throws InterruptedException {


        for (TrafficLight trafficLight : trafficLights) {

            switch (trafficLight.getColor()) {
                case RED -> {
                    for (Car car : CarManager.getCarList()) {
                        if (insideLight(car, trafficLight)) {
                            car.setRedLightStatus(1);
                        }
                    }
                }

                case GREEN -> {
                    for (Car car : CarManager.getCarList()) {
                        if (insideLight(car, trafficLight)) {
                            car.setRedLightStatus(0);
                        }
                    }
                }
            }
        }
    }

    private static boolean insideLight(Car car, TrafficLight light) {
        return (light.getPosition() - car.getPosition()) < 40 && (light.getPosition() - car.getPosition()) > 15;
    }

    /**
     * Add Traffic Light method which adds a certain amount of traffic lights to the arrayList and ensures
     * that it doesn't go over the limit amount for lights.
     * @param amountAdded the amount of lights to be added
     * @throws IOException
     */
    public void addTrafficLight(int amountAdded) throws Exception {

        for(int i = 0; i < amountAdded; i++) {
            if(trafficLightCount < TRAFFIC_LIGHT_LIMIT) {
                trafficLightCount++;

                TrafficLight trafficLight = new TrafficLight(trafficLightCount);
                trafficLights.add(trafficLight);

                Thread trafficThread = new Thread(trafficLight, "light: "+i);
                trafficThreads.add(trafficThread);

            } else {
                throw new Exception("Limit reached for traffic lights.");
            }
        }
    }

    public void setManagerStatus(int statusID) {
        this.managerActive.set(statusID == 1);
    }

    @Override
    public void run() {
        while(managerActive.get()) {
            try {
                checkLights();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

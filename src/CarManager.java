import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CarManager implements Runnable {

    private static final int CAR_LIMIT = 5;
    private static int carCount = 0;

    private static final ArrayList<Car> carList = new ArrayList<>();
    private static final ArrayList<Thread> carThreads = new ArrayList<>();
    private final AtomicBoolean managerActive = new AtomicBoolean(false);

    public CarManager() {
    }

    public static ArrayList<Car> getCarList() {
        return carList;
    }

    public static ArrayList<Thread> getCarThreads() {
        return carThreads;
    }

    public void setManagerStatus(int statusID) {
        this.managerActive.set(statusID == 1);
    }

    /**
     * Iterates through the list of cars and checks whether their positions
     * are behind another car on the list. If behind, slow down.
     */
    private static void checkCarPositions() {
        double currentCarPos;
        double prevCarPos = 0;

        for (int i = 0; i < carList.size(); i++) {
            currentCarPos = carList.get(i).getPosition();
            if (!(i == 0)) {
                if (behindCar(currentCarPos, prevCarPos)) {
                    carList.get(i - 1).setBehindCar(true);
                } else if (carList.get(i-1).getBehindCar().get()) {
                    carList.get(i-1).setBehindCar(false);
                }
                prevCarPos = carList.get(i).getPosition();
            } else {
                prevCarPos = carList.get(i).getPosition();
            }
        }
    }

    private static boolean behindCar(double carPos, double prevCarPos) {
        return (carPos - prevCarPos < 50 && carPos - prevCarPos > 0);
    }


    /**
     * Add car method which adds a certain amount of cars to the arrayList and ensures
     * that it doesn't go over the limit for amount of cars.
     * @param amountAdded the amount of cars to be added
     * @throws IOException
     */
    public void addCar(int amountAdded) throws Exception {

        for(int i = 0; i < amountAdded; i++) {
            if(carCount < CAR_LIMIT) {
                carCount++;
                Car car = new Car(carCount);
                carList.add(car);

                Thread carThread = new Thread(car, "Car "+ i);
                carThreads.add(carThread);

            } else {
                throw new Exception("Limit reached for cars.");
            }
        }
    }


    @Override
    public void run() {
        while(managerActive.get()) {
            checkCarPositions();
        }
    }


}

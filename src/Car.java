

public class Car {

    private int carID, speed;

    public Car(int carID) {
        this.carID = carID;
        this.speed = calculateSpeed();
    }

    private int calculateSpeed() {
        return 0;
    }

    private int getCarID() {
        return carID;
    }

    private int getSpeed() {
        return speed;
    }

}

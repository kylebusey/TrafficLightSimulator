import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Part of TrafficLight classes written for the final project of CMSC335.
 *
 * Driver class of the program which constructs a GUI and creates a TrafficManager and MainDisplay object.
 *
 * Written by Kyle Busey on March 2nd, 2022.
 */

public class Driver extends JFrame {


    //Jbutton names which are processed in an array
    private static final String[] BUTTON_NAMES = {
            "Start", "Stop", "Add Car", "Add Traffic Light"
    };

    private MainDisplay main;
    private static final int DEFAULT_TRAFFIC_LIGHTS = 3, DEFAULT_CARS = 3; //initial amounts for lights & cars
    private static int statusID = 0;

    //create jframe
    public Driver() throws Exception {
        super("Traffic Light Simulator");
        assembleGUI();
        setSize(new Dimension(1000, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Assemble GUI method which creates multiple JPanels and different components to create the GUI and actionlisteners.
     */
    private void assembleGUI() throws Exception {

        LightManager lightManager = new LightManager();
        CarManager carManager = new CarManager();

        Thread lightManagerThread = new Thread(lightManager);
        Thread carManagerThread = new Thread(carManager);

        ArrayList<Thread> lightThreads = LightManager.getTrafficThreads();
        ArrayList<Thread> carThreads = CarManager.getCarThreads();

        ArrayList<JButton> buttonList = createButtons(BUTTON_NAMES);

        //main display object which paints a panel
        main = new MainDisplay(lightManager, carManager);

        //create 3 cars and 3 traffic lights (default amount)
        carManager.addCar(DEFAULT_CARS);
        lightManager.addTrafficLight(DEFAULT_TRAFFIC_LIGHTS);

        //PANELS
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        JPanel carPanel = new JPanel(new BorderLayout());
        JPanel tableData = new JPanel();

        //TOP PANEL
        JLabel introductionMessage = new JLabel("Traffic Light Simulator", SwingConstants.CENTER);
        introductionMessage.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel simulationStatus = new JLabel("Simulation Status: " + getSimulatorStatus(), SwingConstants.CENTER);

        topPanel.add(introductionMessage, BorderLayout.NORTH);
        topPanel.add(simulationStatus, BorderLayout.CENTER);

        for (JButton jButton : buttonList) {
            buttonPanel.add(jButton);
        }

        infoPanel.add(topPanel, BorderLayout.NORTH);
        infoPanel.add(buttonPanel, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.NORTH);
        add(main);

        //Table Panel for nesting interPanel and carPanel
        tableData.setLayout(new BoxLayout(tableData, BoxLayout.Y_AXIS));
        tableData.setPreferredSize(new Dimension(200, 100));
        tableData.add(carPanel);
        add(tableData, BorderLayout.SOUTH);


        //Start Button
        buttonList.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (getSimulatorStatus().equals("Ready to Begin")) {

                    statusID = 1;
                    simulationStatus.setText("Simulation Status: "+ getSimulatorStatus());

                    for(Car car: CarManager.getCarList()) {
                        car.setCarStatus(statusID);
                    }

                    for(TrafficLight trafficLight: LightManager.getTrafficLights()) {
                        trafficLight.setLightStatus(statusID);
                    }

                    for(Thread carThread: carThreads) {
                        carThread.start();
                    }

                    for(Thread lightThread: lightThreads) {
                        lightThread.start();
                    }

                    lightManager.setManagerStatus(statusID);
                    lightManagerThread.start();

                    carManager.setManagerStatus(statusID);
                    carManagerThread.start();

                    new Timer(40, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            main.repaint();
                        }
                    }).start();

                } else {
                    try {
                        JOptionPane.showMessageDialog(null, "Error: Simulation needs to be restarted.");
                        throw new Exception("Error: Simulation needs to be restarted.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Stop Button
        buttonList.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSimulatorStatus().equals("Started")) {
                    statusID = 2;
                    lightManager.setManagerStatus(statusID);
                    carManager.setManagerStatus(statusID);
                    simulationStatus.setText("Simulation Status: " + getSimulatorStatus());

                    for (Car car : CarManager.getCarList()) {
                        car.setCarStatus(0);
                    }

                    for (TrafficLight trafficLight : LightManager.getTrafficLights()) {
                        trafficLight.setLightStatus(0);
                    }

                    for (Thread carThread : carThreads) {
                        carThread.interrupt();
                    }

                    for (Thread lightThread : lightThreads) {
                        lightThread.interrupt();
                    }
                } else {
                    try {
                        JOptionPane.showMessageDialog(null, "Error: Simulation is not active.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Add Car button
        buttonList.get(2).addActionListener(e -> {
            if (getSimulatorStatus().equals("Ready to Begin")) {
                try {
                    carManager.addCar(1);
                    main.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unable to add cars during/after a simulation.");
            }
        });

        //Add Intersection button
        buttonList.get(3).addActionListener(e -> {
            if (getSimulatorStatus().equals("Ready to Begin")) {
            try {
                lightManager.addTrafficLight(1);
                main.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unable to add intersections during/after a simulation.");
            }
        });


    }

    /**
     * Iterates over String array for buttons.
     * @param buttonNames the names of buttons needed for GUI.
     * @return a list of JButtons for the GUI
     */
    private ArrayList<JButton> createButtons(String[] buttonNames) {
        ArrayList<JButton> buttonList = new ArrayList<>();

        for (String buttonName : buttonNames) {
            buttonList.add(createButton(buttonName));
        }
        return buttonList;
    }

    /**
     * createButton method to create a JButton and return.
     * @param name the name of the JButton
     * @return the JButton that was created
     */
    private JButton createButton(String name) {
        return new JButton(name);
    }

    /**
     * Used for GUI JTextField. Returns a string giving the status of the program depending on the statusID.
     * @return the status of the program.
     */
    private String getSimulatorStatus() {
        return switch (statusID) {
            case 1 -> "Started";
            case 2 -> "Ended";
            default -> "Ready to Begin";
        };
    }

    //Main Method
    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();
        driver.setVisible(true);
    }

}

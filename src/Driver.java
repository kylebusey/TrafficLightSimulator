import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 *
 * This class does
 * Part of TrafficLight classes written for the final project of CMSC335.
 *
 * Driver class of the program which constructs a GUI and creates a TrafficManager and MainDisplay object.
 *
 * Written by Kyle Busey on March 2nd, 2022.
 */

public class Driver extends JFrame {



    //Jbutton names
    private static final String[] BUTTON_NAMES = {
            "Start", "Pause", "Resume", "Stop", "Add Car", "Add Traffic Light"
    };

    private TrafficManager trafficManager;
    MainDisplay main;

    static JLabel currentTimeLabel = new JLabel();


    private final int DEFAULT_TRAFFIC_LIGHTS = 3;
    private final int DEFAULT_CARS = 2;

    static Thread time;

    private int statusID = 0;

    //create jframe
    public Driver() throws Exception {
        super("Traffic Light Simulator");

        assembleGUI();
        setSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Assemble GUI method which creates multiple JPanels and different components to create the GUI and actionlisteners.
     * @throws IOException
     */
    private void assembleGUI() throws Exception {

        ArrayList<JButton> buttonList = createButtons(BUTTON_NAMES);



        //traffic manager object which gives specifications for a panel
        trafficManager = new TrafficManager(700, 250, 1L);

        //main display object which paints a panel
        main = new MainDisplay(trafficManager);

        //create 2 cars and 3 traffic lights (default amount)
        trafficManager.addCar(DEFAULT_CARS);
        trafficManager.addTrafficLight(DEFAULT_TRAFFIC_LIGHTS);

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

        //BUTTON PANEL
        for (int i = 0; i < buttonList.size(); i++) {
            buttonPanel.add(buttonList.get(i));
        }

        //INFO PANEL, nests topPanel and buttonPanel as well as adds currentTime
        JLabel currentTime = new JLabel("Current Time: " + currentTimeLabel.getText(), SwingConstants.CENTER);
        currentTime.setFont(new Font("Serif", Font.BOLD, 12));

        infoPanel.add(topPanel, BorderLayout.NORTH);
        infoPanel.add(buttonPanel, BorderLayout.CENTER);
        infoPanel.add(currentTime, BorderLayout.SOUTH);

        add(infoPanel, BorderLayout.NORTH);
        add(main);

        //Table Panel for nesting interPanel and carPanel
        tableData.setLayout(new BoxLayout(tableData, BoxLayout.Y_AXIS));
        tableData.setPreferredSize(new Dimension(200, 100));
        tableData.add(carPanel);
        add(tableData, BorderLayout.SOUTH);


        //Start Button action listener
        buttonList.get(0).addActionListener(e -> {
            statusID = 1;
            simulationStatus.setText("Simulation Status: "+ getSimulatorStatus());

            for(int i = 0; i < trafficManager.getTrafficLights().size(); i++) {
                trafficManager.getTrafficLights().get(i).startThread();
            }

            for(int i = 0; i < trafficManager.getCarList().size(); i++) {
                trafficManager.getCarList().get(i).startThread();
            }

            Thread thread = new Thread(trafficManager);
            thread.start();
        });

        //Pause Button action listener
        buttonList.get(1).addActionListener(e -> {
            statusID = 2;
            simulationStatus.setText("Simulation Status: " + getSimulatorStatus());



        });

        //Resume Button action listener
        buttonList.get(2).addActionListener(e -> {

            for(int i = 0; i < trafficManager.getCarList().size(); i++) {
                trafficManager.getCarList().get(i).resume();
            }

            statusID = 3;
            simulationStatus.setText("Simulation Status: " + getSimulatorStatus());


        });

        //Stop Button action listener
        buttonList.get(3).addActionListener(e -> {


            for(int i = 0; i < trafficManager.getCarList().size(); i++) {
                try {
                    trafficManager.getCarList().get(i).stopThread();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            for(int i = 0; i < trafficManager.getTrafficLights().size(); i++) {
                try {
                    trafficManager.getTrafficLights().get(i).suspend();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }


            statusID = 4;
            simulationStatus.setText("Simulation Status: " + getSimulatorStatus());
        });

        //Add Car action listener
        buttonList.get(4).addActionListener(e -> {

            try {
                trafficManager.addCar(1);
                ArrayList<Car> carList = trafficManager.getCarList();
                Car car = carList.get(carList.size()-1);
                car.startThread();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });


        //Add Intersection action listener
        buttonList.get(5).addActionListener(e -> {

            try {
                trafficManager.addTrafficLight(1);
                ArrayList<TrafficLight> trafficLights= trafficManager.getTrafficLights();
                TrafficLight trafficLight = trafficLights.get(trafficLights.size()-1);
                Thread thread = new Thread(trafficLight);
                thread.start();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }});


        new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.repaint();
            }
        }).start();
    }

    /**
     * Basic method to iterate over a list to create JButtons
     * @param buttonNames the names of buttons needed for GUI.
     * @return a list of JButtons for the GUI
     */
    private ArrayList<JButton> createButtons(String[] buttonNames) {
        ArrayList<JButton> buttonList = new ArrayList<>();

        for(int i = 0; i < buttonNames.length; i++) {
            buttonList.add(createButton(buttonNames[i]));
        }
        return buttonList;
    }

    /**
     * Simple createbutton method to create a JButton and return.
     * @param name the name of the JButton
     * @return the JButton that was created
     */
    private JButton createButton(String name) {
        JButton button = new JButton(name);
        return button;
    }

    /**
     * Used for GUI JTextField. Returns a string giving the status of the program depending on the statusID.
     * @return the status of the program.
     */
    private String getSimulatorStatus() {
        switch(statusID) {
            case 1: return "Active";
            case 2: return "Paused";
            case 3: return "Resumed";
            case 4: return "Stopped";
        }
        return "Not Active";
    }

    //Main Method
    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();
        driver.setVisible(true);
    }



}

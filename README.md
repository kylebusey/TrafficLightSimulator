![Banner](https://raw.githubusercontent.com/kylebusey/TrafficLightSimulator/master/Traffic_Light_Simulator.png)

# Traffic Light Simulator

## About
This program was written as the final project of CMSC335, Concurrent Programming in Java. The intended goal of the project was to create a stable and consistent multi-threaded program. The program uses a Swing GUI to display the simulation.

## User Guide
![Banner](https://raw.githubusercontent.com/kylebusey/TrafficLightSimulator/master/TrafficLightSimulatorExample.png)

- Start the simulation by hitting the "Start" button.
- Stop the simulation by hitting the "Stop" button.
- Adding a car to the simulation is done by clicking the "Add Car" button.
- Adding a light to the simulation is done by clicking the "Add Traffic Light" button.
**Please note that any cars/lights must be added before the start of the simulation.**
---
## Features
- Uses a multi-threaded approach to run a responsive traffic simulation. 
- Has four buttons for starting/stopping the simulation as well as adding a car or traffic light.
- Cars will move forward on the road when not in front of a red light and when their path is clear of other cars.
- Traffic lights sleep for a designated period of time respective to the current color. **EX: A green light stays green for 10 seconds.**
- Light manager and car manager components control everything in the simulation.


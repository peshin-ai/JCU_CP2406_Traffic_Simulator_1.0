/*
  Name: Ung Ta Hoang Tuan
  Subject: CP2406 - Programming III
  Start date: 7/4/2021
  Github link: https://github.com/peshin-ai/JCU_CP2406_Traffic_Simulator_1.0

 */
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner UserInput = new Scanner(System.in);
        int InitialRoads;
        int InitialCars;
        int LightInRoad =2;
        int gasStationSpawns = 1;
        int trafficSignSpawns = 1;

        do{
            System.out.print("Please enter a positive number of road to spawn on the traffic simulator: ");
            while(!UserInput.hasNextInt()){
                System.out.print("Please enter an appropriate positive Integer! ");
                UserInput.next();
            }
            InitialRoads = UserInput.nextInt();
        }while (InitialRoads <= 0);
        do{
            System.out.print("Please enter a positive number of cars to spawn on the traffic simulator: ");
            while(!UserInput.hasNextInt()){
                System.out.print("Please enter an appropriate positive Integer! ");
                UserInput.next();
            }
            InitialCars = UserInput.nextInt();
        }while (InitialCars <= 0);



        ArrayList<Road> roads = new ArrayList<>();
        ArrayList<Car> cars = new ArrayList<>();
        ArrayList<TrafficLight> lights = new ArrayList<>();
        ArrayList<TrafficSign> trafficSigns = new ArrayList<>();
        ArrayList<GasStation> gasStations = new ArrayList<>();
        showUI(UserInput, roads, cars, lights, trafficSigns, gasStations, InitialRoads, InitialCars, LightInRoad, gasStationSpawns, trafficSignSpawns);
        showGamePlay(UserInput, cars, lights, gasStations, trafficSigns);
        System.out.println("Enter the number of road to spawn");
    }

    public static void showUI(Scanner sc, ArrayList<Road> roads, ArrayList<Car> cars, ArrayList<TrafficLight> lights, ArrayList<TrafficSign> trafficSigns, ArrayList<GasStation> gasStations, int roadSpawns, int carSpawns, int lightSpawns, int gasStationSpawns, int trafficSignSpawn) {
        System.out.println("Object Creation:\n---------------------");
        System.out.println("Roads:");
        for (int number = 0; number < roadSpawns; number++) {
            System.out.println("Please input parameters for road_" + number + "...");
            System.out.print("Length:");
            int lengthInput = sc.nextInt();
            int speedLimitInput = 1; // force speed limit to be 1 for prototype.
            roads.add(new Road(Integer.toString(number), speedLimitInput, lengthInput, new int[]{0, 0}));
        }
        System.out.println("\nRoads;");
        for (Road road : roads
        ) {
            road.showRoadInfo();
        }

        System.out.println("\nCars;");
        for (int number = 0; number < carSpawns; number++) {
            cars.add(new Car(Integer.toString(number), roads.get(0))); // all created cars will begin on road_0.
            cars.get(number).showOutPut();
        }


        System.out.println("\nTraffic Lights;");

        for (int number = 0; number < lightSpawns; number++) {
            lights.add(new TrafficLight(Integer.toString(number), roads.get(0))); // all created lights will begin on road_0.
            lights.get(number).showLightInfo();
        }
        System.out.print("\nTraffic Sign;");

        for (int number = 0; number < trafficSignSpawn; number ++){
            trafficSigns.add(new TrafficSign(Integer.toString(number), roads.get(0)));
            trafficSigns.get(number).showSignInfo();
        }


        System.out.println("\nGas Stations;");

        for (int number = 0; number < gasStationSpawns; number++) {
            gasStations.add(new GasStation(Integer.toString(number), roads.get(0)));
            gasStations.get(number).showInfo();
        }

        System.out.println();

        System.out.println("Settings:");
        roads.get(1).setStartLocation(new int[]{roads.get(0).getLength() + 1, 0}); // place road_1 to a position at the end of road_0.
        roads.get(1).setEndLocation(new int[]{roads.get(0).getLength() + 1 + roads.get(1).getLength(), 0});
        roads.get(1).showRoadInfo();
        roads.get(0).getConnectedRoads().add(roads.get(1)); // connect road_0 to road_1
        System.out.println();
    }

    public static void showGamePlay(Scanner sc, ArrayList<Car> cars, ArrayList<TrafficLight> lights, ArrayList<GasStation> gasStations, ArrayList<TrafficSign> trafficSigns
    ) {
        System.out.println("Simulation:");
        int time = 0;
        System.out.print("Set time scale in milliseconds:");
        int speedOfSim = sc.nextInt();
        int carsFinished = 0;
        while (carsFinished < cars.size()) {
            for (TrafficLight light : lights) {
                light.operate();
                light.showLightInfo();
            }
            for (TrafficSign trafficSign: trafficSigns){
                trafficSign.stateOfSign();
                trafficSign.showSignInfo();
            }

            for (GasStation station : gasStations) {
                station.showInfo();
            }

            for (Car car : cars) {
                car.move();
                car.showOutPut();
                if (car.getCurrentRoad().getConnectedRoads().isEmpty() && (car.getSpeed() == 0)) {
                    carsFinished += 1;
                }
            }
            time += 1;
            System.out.println(time + " Seconds have passed.\n");
            try {
                Thread.sleep(speedOfSim); // set speed of simulation.
            } catch (InterruptedException sim) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
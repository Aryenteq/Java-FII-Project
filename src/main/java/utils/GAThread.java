package utils;

import GA.VehicleRouting;

public class GAThread implements Runnable {

    @Override
    public void run() {
        VehicleRouting solver = new VehicleRouting();
        solver.run();
    }
}

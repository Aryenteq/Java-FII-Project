package utils;

import GA.Parameters;
import GA.VehicleRouting;

public class GAThread implements Runnable {

    public GAThread() {
    }

    @Override
    public synchronized void run() {
        VehicleRouting solver = new VehicleRouting();
        solver.run();
    }
}

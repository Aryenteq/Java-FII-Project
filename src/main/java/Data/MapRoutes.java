package Data;

import Database.LocationStructure;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MapRoutes extends Task<Void> {
    private static final String OSRM_URL = "http://router.project-osrm.org/route/v1/driving";
    private static final int numThreads = Runtime.getRuntime().availableProcessors();
    public static List<LocationStructure> locations;
    public static Map<String, String> routes = new ConcurrentHashMap<>();
    //public static Map<String, String> routes = new HashMap<>();
    public static double maxConnections;
    public static double currentConnections;
    private static List<IntPair> locationsPairs = new ArrayList<>();

    private static synchronized void incrementConnections() {
        currentConnections++;
    }

    public static void reset() {
        locations = new ArrayList<>();
        routes = new HashMap<>();
        currentConnections = 0;
        maxConnections = 0;
    }

    public static void loadLocations(List<LocationStructure> locations) {
        MapRoutes.locations = locations;
        maxConnections = (double) (locations.size() * (locations.size() - 1)) / 2;
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                locationsPairs.add(new IntPair(i, j));
            }
        }
    }

    public static Map<String, String> getRoutes() {
        return routes;
    }

    public static List<LocationStructure> getLocations() {
        return locations;
    }

    public static double getMaxConnections() {
        return maxConnections;
    }

    private synchronized static String tryGetRoute(double[] coordsAddress1, double[] coordsAddress2) {
        return getDrivingRoute(coordsAddress1[0], coordsAddress1[1], coordsAddress2[0], coordsAddress2[1]);
    }

    private static String getDrivingRoute(double lat1, double lon1, double lat2, double lon2){
        try {
            String requestUrl = String.format("%s/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=geojson", OSRM_URL, lon1, lat1, lon2, lat2);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUrl)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String temp1 = response.body();
            temp1 = removeContent(temp1);
            temp1 = removeContent(temp1);

            return temp1;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "null";
    }

    private static String removeContent(String input) {
        int openCount = 0;
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '{') {
                if (openCount <= 1) {
                    startIndex = i;
                    openCount++;
                }
            } else if (ch == '}') {
                endIndex = i;
                break;
            }
        }
        return input.substring(startIndex, endIndex + 1);
    }

    @Override
    protected Void call() throws Exception {
        Thread[] threads = new Thread[numThreads];
        for (int t = 0; t < numThreads; t++) {
            final int index = t;
            threads[t] = new Thread(() -> {
                for (int lp = index; lp < locationsPairs.size(); lp+=numThreads) {
                    int i = locationsPairs.get(lp).getFirst();
                    int j = locationsPairs.get(lp).getSecond();
                    double[] first = {locations.get(i).getLatitude(), locations.get(i).getLongitude()};
                    double[] second = {locations.get(j).getLatitude(), locations.get(j).getLongitude()};
                    String tempRoute = tryGetRoute(first, second);
                    routes.put(new IntPair(i, j).toString(), tempRoute);
                    routes.put(new IntPair(j, i).toString(), tempRoute);
                    incrementConnections();
                    updateProgress(currentConnections, maxConnections);
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

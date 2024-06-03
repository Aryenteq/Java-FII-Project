package org.example.demo;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MapController {
    LoadAddressesController loadAddressesController;
    MapController(LoadAddressesController loadAddressesController) {
        this.loadAddressesController = loadAddressesController;
    }
    private static final String USER_AGENT = "JavaApp/1.0 (example@example.com)";
//    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
//    private static final String OSRM_URL = "http://router.project-osrm.org/route/v1/driving";

    public void loadAddress(double lat, double lng) {
        this.loadAddressesController.setTextAreaValue(reverseGeocode(lat, lng));
    }
    private String reverseGeocode(double lat, double lng) {
        try {

            String requestUrl = String.format("%s?format=json&lat=%.6f&lon=%.6f", "https://nominatim.openstreetmap.org/reverse", lat, lng);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("User-Agent", USER_AGENT)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getString("display_name");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void printZoom(String zoom) {
        System.out.println(zoom);
    }
}

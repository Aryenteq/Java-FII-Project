package Controllers;

import Data.CoordsPair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MapController {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    LoadAddressesController loadAddressesController;
    HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();


    MapController(LoadAddressesController loadAddressesController) {
        this.loadAddressesController = loadAddressesController;
    }

    public void loadAddress(double lat, double lng) {

        this.loadAddressesController.setTextAreaValue(reverseGeocode(lat, lng), lat, lng);

    }

    private String reverseGeocode(double lat, double lng) {
        try {

            String requestUrl = String.format("%s?format=json&lat=%.6f&lon=%.6f", "https://nominatim.openstreetmap.org/reverse", lat, lng);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = httpRequestBuilder.uri(URI.create(requestUrl)).build();
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

    public CoordsPair getAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String requestUrl = NOMINATIM_URL + "?q=" + encodedAddress + "&format=json&limit=1";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = httpRequestBuilder.uri(URI.create(requestUrl)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body().substring(1, response.body().length() - 1);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (!jsonObject.isEmpty()) {
                double lat = jsonObject.getDouble("lat");
                double lon = jsonObject.getDouble("lon");
                return new CoordsPair(lat, lon);
            }

        } catch (Exception e) {
            System.err.println("Error fetching coordinates for city: " + address);
            e.printStackTrace();
        }
        return null;
    }
}

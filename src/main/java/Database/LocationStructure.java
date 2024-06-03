package Database;

public class LocationStructure {
    int id;
    String name;
    double latitude;
    double longitude;
    boolean solved;

    public LocationStructure(int id, String name, double lat, double lon, boolean solved) {
        this.id = id;
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.solved = solved;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return STR."LocationStructure{id=\{id}, name='\{name}\{'\''}, latitude=\{latitude}, longitude=\{longitude}, solved=\{solved}\{'}'}\n";
    }
}

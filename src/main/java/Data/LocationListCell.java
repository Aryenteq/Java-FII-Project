package Data;

import Database.LocationStructure;

public class LocationListCell {
    public LocationStructure cellData;

    public LocationListCell(LocationStructure cellData) {
        this.cellData = cellData;
    }

    @Override
    public String toString() {
        return cellData.getName();
    }
}

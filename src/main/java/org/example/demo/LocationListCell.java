package org.example.demo;

import Database.LocationStructure;

public class LocationListCell {
    LocationStructure cellData;
    LocationListCell(LocationStructure cellData) {
        this.cellData = cellData;
    }

    @Override
    public String toString() {
        return cellData.getName();
    }
}

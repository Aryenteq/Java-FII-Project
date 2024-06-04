package Data;

import Controllers.MapVisualizationController;
import Database.LocationDAO;
import Database.LocationStructure;
import GA.Parameters;
import GA.VehicleRouting;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CustomGraph {
    public static int nodesNumber;
    public static List<List<Double>> distances;
    private String name;
    private List<Node> nodeArr;
    private String edgeWeightType;
    private Graph graph;

    public CustomGraph() {
        if (Parameters.isUseDatabase()) {
            initializeGraphFromDB();
        } else {
            initializeGraphFromFile();
        }
    }

    private void initializeGraphFromDB() {
//        try {
//            LocationDAO locationDAO = new LocationDAO();
            name = "Database";
            nodeArr = new ArrayList<>();
//            ResultSet resultSet = locationDAO.getSelectedLocations();
            List<LocationStructure> locationsData = MapVisualizationController.locations;
            int index = 0;
            for(LocationStructure location : locationsData) {
                nodeArr.add(new Node(index, (float) location.getLatitude(), (float) location.getLongitude()));
                index++;
            }
//            int index = 0;
//            while (resultSet.next()) {
//                double latitude = resultSet.getDouble("latitude");
//                double longitude = resultSet.getDouble("longitude");
//                nodeArr.add(new Node(index, (float) latitude, (float) longitude));
//                index++;
//            }
            nodesNumber = nodeArr.size();

            if (!Parameters.isUseGraph4j()) {
                distances = new ArrayList<>(nodesNumber);
                for (int i = 0; i < nodesNumber; i++)
                    distances.add(new ArrayList<>(Collections.nCopies(nodesNumber, 0.0)));
            }

            if (Parameters.isUseGraph4j()) {
                graph = GraphBuilder.empty().estimatedNumVertices(nodesNumber).estimatedAvgDegree(nodesNumber - 1).buildGraph();
                for (int i = 0; i < nodesNumber; i++) {
                    graph.addVertex(i);
                }
            }

            edgeWeightType = "EUC_2D";
            for (int i = 0; i < nodesNumber; i++) {
                for (int j = 0; j < nodesNumber; j++) {
                    if (i != j) {
                        Node n1 = nodeArr.get(i);
                        Node n2 = nodeArr.get(j);

                        double distance = haversineDistance(n1, n2);

                        if (!Parameters.isUseGraph4j()) {
                            distances.get(i).set(j, distance);
                        } else if (i < j) {
                            graph.addEdge(i, j, distance);
                        }
                    }
                }
            }

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private void initializeGraphFromFile() {
        String filePath = "/Routes/" + Parameters.getFileName();
        try (InputStream inputStream = VehicleRouting.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            try (Scanner file = new Scanner(inputStream)) {
                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    if (line.contains("NAME")) {
                        name = line.split(":")[1].trim();
                    } else if (line.contains("DIMENSION")) {
                        nodesNumber = Integer.parseInt(line.split(":")[1].trim());
                        nodeArr = new ArrayList<>(nodesNumber);
                        if (!Parameters.isUseGraph4j()) {
                            distances = new ArrayList<>(nodesNumber);
                            for (int i = 0; i < nodesNumber; i++)
                                distances.add(new ArrayList<>(Collections.nCopies(nodesNumber, 0.0)));
                        }
                    } else if (line.contains("EDGE_WEIGHT_TYPE")) {
                        edgeWeightType = line.split(":")[1].trim();
                    } else if (line.contains("NODE_COORD_SECTION")) {
                        break;
                    }
                }

                for (int i = 0; i < nodesNumber; i++) {
                    String[] tokens = file.nextLine().trim().split("\\s+");
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException("Invalid node data format: " + Arrays.toString(tokens));
                    }
                    assert nodeArr != null;
                    nodeArr.add(new Node(i, Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                }

                if (Parameters.isUseGraph4j()) {
                    graph = GraphBuilder.empty().estimatedNumVertices(nodesNumber).estimatedAvgDegree(nodesNumber - 1).buildGraph();
                    for (int i = 0; i < nodesNumber; i++) {
                        graph.addVertex(i);
                    }
                }

                assert edgeWeightType != null;
                if (edgeWeightType.equals("EUC_2D")) {
                    for (int i = 0; i < nodesNumber; i++) {
                        for (int j = 0; j < nodesNumber; j++) {
                            if (i != j) {
                                Node n1 = nodeArr.get(i);
                                Node n2 = nodeArr.get(j);

                                double distance = Math.sqrt(Math.pow(n1.x() - n2.x(), 2) + Math.pow(n1.y() - n2.y(), 2));

                                if (!Parameters.isUseGraph4j()) {
                                    distances.get(i).set(j, distance);
                                } else if (i < j) {
                                    graph.addEdge(i, j, distance);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Node> getNodeArr() {
        return nodeArr;
    }

    // If DB is used - temporary (or not) get the distance in km
    // Based on Earths curve
    private double haversineDistance(Node n1, Node n2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double lat1 = n1.x();
        double lon1 = n1.y();
        double lat2 = n2.x();
        double lon2 = n2.y();
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // convert to kilometers
    }


    public int getNodesNumber() {
        return nodesNumber;
    }

    public List<List<Double>> getDistances() {
        return distances;
    }

    public Graph getGraph() {
        return graph;
    }

    void printDistances() {
        System.out.println("Distance Matrix:");
        for (int i = 0; i < nodesNumber; i++) {
            for (int j = 0; j < nodesNumber; j++) {
                System.out.print(distances.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
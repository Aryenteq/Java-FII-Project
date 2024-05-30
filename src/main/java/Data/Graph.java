package Data;

import GA.Parameters;
import GA.VehicleRouting;

import java.io.InputStream;
import java.util.*;

public class Graph {
    public static int nodesNumber;
    public static List<List<Double>> distances;
    private String name;
    private List<Node> nodeArr;
    private String edgeWeightType;

    public Graph() {
        String filePath = "/Routes/" + Parameters.fileName;
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
                        distances = new ArrayList<>(nodesNumber);
                        for (int i = 0; i < nodesNumber; i++)
                            distances.add(new ArrayList<>(Collections.nCopies(nodesNumber, 0.0)));
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
                    nodeArr.add(new Node(i,
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ));
                }

                if (edgeWeightType.equals("EUC_2D")) {
                    for (int i = 0; i < nodesNumber; i++) {
                        for (int j = 0; j < nodesNumber; j++) {
                            if (i != j) {
                                Node n1 = nodeArr.get(i);
                                Node n2 = nodeArr.get(j);

                                double distance = Math.sqrt(Math.pow(n1.x() - n2.x(), 2) + Math.pow(n1.y() - n2.y(), 2));
                                distances.get(i).set(j, distance);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
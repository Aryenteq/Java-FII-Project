package Stages;

import java.net.URL;

public class StageResources {
    public static URL getResource(String name) {
        return StageResources.class.getResource(name);
    }
}

package philps.lights.demo.Scenes;

import com.fasterxml.jackson.databind.JsonNode;
import philps.lights.demo.Light.Light;

import java.sql.Time;
import java.util.List;

public class SingularScene extends Scenes {
    private List<Light> lights;
    private Time duration;

    public SingularScene(JsonNode config){

    }

}

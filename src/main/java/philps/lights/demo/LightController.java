package philps.lights.demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import philps.lights.demo.Scenes.DynamicScene;
import philps.lights.demo.Scenes.SingularScene;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/lights")
public class LightController {
    private ObjectMapper objectMapper;
    private Lights lights;
    @Autowired
    public LightController(Lights lights) {
        this.lights = lights;
    }
    @GetMapping("/list")
    public void testLights() throws JsonProcessingException {
        lights.getLights();
    }
    @PostMapping("/newScene")
    @ResponseBody
    public ResponseEntity<String> addScene(@RequestBody String requestBody) throws JsonProcessingException {
        JsonNode res = this.objectMapper.readTree(requestBody);
        if(!res.has("type")){
            return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);
        }
        switch(res.get("type").asText()){
            case "SingularScene":
                this.lights.addScene(new SingularScene(res.get("data")));
                break;
            case "DynamicScene":
                this.lights.addScene(new DynamicScene(res.get("data")));
                break;
            default:
                return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

package philps.lights.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/lights")
public class LightController {

    private final LightHandler lightHandler;
    @Autowired
    public LightController(LightHandler lightHandler) {
        this.lightHandler = lightHandler;
    }
    @GetMapping("/login")
    public boolean bridgeLoginAuthorized() throws IOException {
        return this.lightHandler.tryLogin();
    }

}
//this.webClient = webClientBuilder.baseUrl("http://192.168.1.198/api").build();

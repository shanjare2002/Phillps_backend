package philps.lights.demo;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.netty.http.client.HttpClient;


import java.util.stream.DoubleStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/lights")
public class LightController {

    private final WebClient webClient;
    @Autowired
    public LightController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://192.168.1.198/api").build();

        // Prepare JSON payload
        String jsonPayload = "{\"devicetype\":\"my_hue_app#iphone peter\"}\n";

        // Make a POST request using WebClient
        String responseBody = this.webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonPayload))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking for simplicity in this example

        // Print the response
        System.out.println(responseBody);
    }
}

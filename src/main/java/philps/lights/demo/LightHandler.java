package philps.lights.demo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
@Component
public class LightHandler {
    private final LoginHandler loginHandler;
    private final String baseURL;
    private final WebClient webClient;

    public LightHandler(LoginHandler loginHandler, WebClient.Builder webClientBuilder){
        this.loginHandler = loginHandler;
        this.baseURL = "http://" + this.loginHandler.getInternalipaddress() + "/api";
        this.webClient = webClientBuilder.baseUrl(baseURL).build();
    }
    public boolean tryLogin() throws IOException {
        String jsonPayload = "{\"devicetype\":\"my_hue_app\"}\n";
        String responseBody = this.webClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(jsonPayload))
        .retrieve()
        .bodyToMono(String.class)
        .block();
        JsonNode responseNode = parseJsonString(responseBody);
        System.out.println(responseNode);
        return responseNode.has("error");
    }
    private static JsonNode parseJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }
}

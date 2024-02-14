package philps.lights.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
@Service
public class LoginHandler {

    private final WebClient webClient;
    private String internalipaddress;


    public LoginHandler(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://discovery.meethue.com/").build();
        this.internalipaddress = fetchHubAddress();
    }

    private static JsonNode parseJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    public String getInternalipaddress() {
        return this.internalipaddress;
    }

    public String fetchHubAddress() {
        try {
            String response = this.webClient.get().retrieve().bodyToMono(String.class).block();
            JsonNode rootNode = parseJsonString(response);
            this.internalipaddress = rootNode.get(0).get("internalipaddress").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.internalipaddress;
    }
}
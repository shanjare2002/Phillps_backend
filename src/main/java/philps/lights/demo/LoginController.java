package philps.lights.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@Service
public class LoginController {

    private LoginHandler loginHandler;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Autowired
    public LoginController() {
        this.loginHandler = new LoginHandler(webClientBuilder());
    }

    @GetMapping("/hubAddress")
    public String hubAddressExists() {
        return this.loginHandler.getInternalipaddress();
    }
}


class LoginHandler {

    private final WebClient webClient;
    private String internalipaddress;
    private String resp = "[{\"id\":\"ecb5fafffe3045d0\",\"internalipaddress\":\"192.168.1.198\",\"port\":\"443\"}]";


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

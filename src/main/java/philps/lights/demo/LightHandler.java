package philps.lights.demo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.File;
import java.io.IOException;




@Service
@Component
public class LightHandler {
    private final LoginHandler loginHandler;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private JsonNode lightList;
    private final String userPath  = "src/main/java/philps/lights/demo/userHandler.json";
    private String username;

    public LightHandler(LoginHandler loginHandler) throws IOException {
        this.loginHandler = loginHandler;
        this.objectMapper =  new ObjectMapper();
        this.lightList = this.objectMapper.createObjectNode();
        readHandleUserFile();
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        this.webClient = WebClient.builder()
                .baseUrl("https://" + loginHandler.getInternalipaddress())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public  boolean tryLogin() throws IOException {
        if (this.username.isEmpty()) {
            String jsonPayload = "{\"devicetype\":\"my_hue_app\"}\n";
            String response = this.webClient.post()
                    .uri("/api")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(jsonPayload))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode responseNode = this.objectMapper.readTree(response);
            boolean res = responseNode.get(0).has("success");
            if(res){
                this.username = responseNode.get(0).get("success").get("username").asText();
                writeHandleUserFile();
            }
            return res;
        }
        String response;
        try {
            response = this.webClient.get()
                    .uri("/clip/v2/resource/device")
                    .header("hue-application-key", this.username)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        this.lightList = this.objectMapper.readTree(response);
        return this.lightList.get("errors").isEmpty();
    }
    private void readHandleUserFile() throws IOException {
        File jsonFile = new File(this.userPath);
        JsonNode jsonNode = this.objectMapper.readTree(jsonFile);
        this.username = jsonNode.get("username").asText();
    }
    private void writeHandleUserFile() throws IOException {
        File jsonFile = new File(this.userPath);
        JsonNode jsonNode = this.objectMapper.readTree(jsonFile);
        ((ObjectNode) jsonNode).put("username", this.username);
        this.objectMapper.writeValue(jsonFile, jsonNode);
    }
}
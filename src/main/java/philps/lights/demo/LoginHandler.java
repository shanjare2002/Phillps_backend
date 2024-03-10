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

import javax.net.ssl.SSLException;
import java.io.IOException;


@Service
@Component
public class LoginHandler {

    private  WebClient webClient;
    private final file_io fileOps;
    private ObjectNode login;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public LoginHandler(WebClient.Builder webClientBuilder) throws IOException {
        String userPath = "src/main/java/philps/lights/demo/userHandler.json";
        this.fileOps = new file_io(userPath);
        this.login = this.fileOps.readHandleUserFile();
        this.webClient = webClientBuilder.baseUrl("https://discovery.meethue.com/").build();
        if (this.login.get("internalIPAddress").asText().equals("")) {
            this.login.put("internalIPAddress", fetchHubAddress());
            this.fileOps.writeHandleUserFile(this.login);
        }
    }

    public String getInternalipaddress() {
        return this.login.get("internalIPAddress").asText();
    }

    public String fetchHubAddress() {
        try {
            JsonNode rootNode = objectMapper.readTree(webClient.get().retrieve().bodyToMono(String.class).block());
            return (rootNode != null && rootNode.isArray() && !rootNode.isEmpty())
                    ? rootNode.get(0).get("internalipaddress").asText()
                    : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public  boolean tryLogin() throws IOException {
        this.webClient = this.generateSecureWebClient();
        if (this.login.get("username").asText().isEmpty()) {
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
                this.login.put("username",  responseNode.get(0).get("success").get("username").asText());
                fileOps.writeHandleUserFile(this.login);
            }
            return res;
        }
        String response;
        try {
            response = this.webClient.get()
                    .uri("/clip/v2/resource/device")
                    .header("hue-application-key", this.login.get("username").asText())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return this.objectMapper.readTree(response).asText().isEmpty();
    }
    public ObjectNode getLogin() {
        return login;
    }
    public WebClient generateSecureWebClient() throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        WebClient localClient = WebClient.builder()
                .baseUrl("https://" + this.login.get("internalIPAddress").asText())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        return localClient;
    }
}

package philps.lights.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class LoginHandler {

    private final WebClient webClient;
    private String internalipaddress = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private String userPath  = "src/main/java/philps/lights/demo/userHandler.json";


    public LoginHandler(WebClient.Builder webClientBuilder) throws IOException {
        this.readHandleUserFile();
        this.webClient = webClientBuilder.baseUrl("https://discovery.meethue.com/").build();
        if(this.internalipaddress.equals("")){
            this.internalipaddress = fetchHubAddress();
            System.out.println(this.internalipaddress);
            writeHandleUserFile();
        }
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
        private void readHandleUserFile() throws IOException {
            String res = Files.readString(Path.of(this.userPath));
            JsonNode jsonNode = this.objectMapper.readTree(res);
            this.internalipaddress = (jsonNode.get("internalIPAddress")).asText();
    }
        private void writeHandleUserFile() throws IOException {
            JsonNode jsonNode = this.objectMapper.readTree(new File(this.userPath));
            ((ObjectNode) jsonNode).put("internalIPAddress", this.internalipaddress);
            objectMapper.writeValue(new File(userPath), jsonNode);
            }
        }

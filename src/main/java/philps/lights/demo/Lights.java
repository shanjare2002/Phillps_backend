package philps.lights.demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import philps.lights.demo.Light.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import philps.lights.demo.Scenes.Scenes;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service

public class Lights {
      private ObjectNode login;
      private LoginHandler loginHandler;
      private ArrayList<Light> lightList =  new ArrayList<>();
      private WebClient webClient;
      private List<Scenes> scenes;
      private final HashMap<String, String> nameToId = new HashMap<>();
      private final ObjectMapper objectMapper = new ObjectMapper();
      @Autowired
      public Lights(LoginHandler loginHandler, WebClient.Builder webClientBuilder) throws SSLException {
          this.loginHandler = loginHandler;
          this.login = loginHandler.getLogin();
          this.webClient = this.loginHandler.generateSecureWebClient();
      }
      public void getLights() throws JsonProcessingException {
          String response = "";
          try {
             response = this.webClient.get()
                      .uri("/clip/v2/resource/device")
                      .header("hue-application-key", this.login.get("username").asText())
                      .retrieve()
                      .bodyToMono(String.class)
                      .block();

          } catch (Exception e) {
              e.printStackTrace();
          }
          JsonNode jsonNode =  this.objectMapper.readTree(response).get("data");
          for(JsonNode lightElement: jsonNode){
              Light light = objectMapper.treeToValue(lightElement, Light.class);
              this.nameToId.put(light.getMetadata().getName(), light.getId());
          }
      }
      public void addScene(Scenes newScene){
          this.scenes.add(newScene);
      }
}

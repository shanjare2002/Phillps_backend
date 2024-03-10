package philps.lights.demo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class file_io {
    private String userPath;
    private File file;
    private final ObjectMapper objectMapper;

    public file_io(String userPath){
        this.userPath = userPath;
        this.file =  new File(this.userPath);
        this.objectMapper = new ObjectMapper();
    }

    public ObjectNode readHandleUserFile() throws IOException {
        String res = Files.readString(Path.of(this.userPath));
        return (ObjectNode) this.objectMapper.readTree(res);
    }
    public boolean writeHandleUserFile(JsonNode node){
        try {
            objectMapper.writeValue(this.file, node);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package philps.lights.demo.Light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Metadata {
    private String name;
    private String archetype;

    @JsonCreator
    public Metadata(
            @JsonProperty("name") String name,
            @JsonProperty("archetype") String archetype) {
        this.name = name;
        this.archetype = archetype;
    }

    public ObjectNode toJsonNode() {
        ObjectNode metadataNode = JsonNodeFactory.instance.objectNode();
        metadataNode.put("name", name);
        metadataNode.put("archetype", archetype);
        return metadataNode;
    }

    public String getName() {
        return name;
    }
}

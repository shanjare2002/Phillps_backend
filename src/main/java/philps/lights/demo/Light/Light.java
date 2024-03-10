package philps.lights.demo.Light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class Light {
    private String id;
    private String id_v1;
    private ProductData productData;
    private Metadata metadata;
    private ObjectNode identify;
    private UserTest usertest;
    private List<service> services;
    private String type;
    @JsonCreator
    public Light(
            @JsonProperty("id") String id,
            @JsonProperty("id_v1") String idV1,
            @JsonProperty("product_data") ProductData productData,
            @JsonProperty("metadata") Metadata metadata,
            @JsonProperty("identify") ObjectNode identify,
            @JsonProperty("usertest") UserTest usertest,
            @JsonProperty("services") List<service> services,
            @JsonProperty("type") String type) {
        this.id = id;
        this.id_v1 = idV1;
        this.productData = productData;
        this.metadata = metadata;
        this.identify = identify;
        this.usertest = usertest;
        this.services = services;
        this.type = type;
    }
    public String getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}
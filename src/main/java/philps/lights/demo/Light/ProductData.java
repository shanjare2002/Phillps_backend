package philps.lights.demo.Light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductData {
    private String modelId;
    private String manufacturerName;
    private String productName;
    private String productArchetype;
    private boolean certified;
    private String softwareVersion;
    private String hardwarePlatformType;
    @JsonCreator
    public ProductData(
            @JsonProperty("model_id") String modelId,
            @JsonProperty("manufacturer_name") String manufacturerName,
            @JsonProperty("product_name") String productName,
            @JsonProperty("product_archetype") String productArchetype,
            @JsonProperty("certified") boolean certified,
            @JsonProperty("software_version") String softwareVersion,
            @JsonProperty("hardware_platform_type") String hardwarePlatformType) {
        this.modelId = modelId;
        this.manufacturerName = manufacturerName;
        this.productName = productName;
        this.productArchetype = productArchetype;
        this.certified = certified;
        this.softwareVersion = softwareVersion;
        this.hardwarePlatformType = hardwarePlatformType;
    }

}


package philps.lights.demo.Light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class service {
    private String rid;
    private String rtype;

    // Constructor
    @JsonCreator
    public service(
            @JsonProperty("rid") String rid,
            @JsonProperty("rtype") String rtype) {
        this.rid = rid;
        this.rtype = rtype;
    }
    // Getter for rid
    public String getRid() {
        return rid;
    }

    // Setter for rid
    public void setRid(String rid) {
        this.rid = rid;
    }

    // Getter for rtype
    public String getRtype() {
        return rtype;

    }
}
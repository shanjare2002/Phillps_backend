package philps.lights.demo.Light;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTest {
    private String status;
    private boolean usertest;

    @JsonCreator
    public UserTest(
            @JsonProperty("status") String status,
            @JsonProperty("usertest") boolean usertest) {
        this.status = status;
        this.usertest = usertest;
    }


}

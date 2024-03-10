package philps.lights.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api")
public class   LoginController {
    private final LoginHandler loginHandler;

    @GetMapping("/hubAddress")
    public String hubAddressExists() {
        return this.loginHandler.getInternalipaddress();
    }
    @GetMapping("/login")
    public boolean login() throws IOException {
        return this.loginHandler.tryLogin();
    }
            @Autowired
    public LoginController(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

}


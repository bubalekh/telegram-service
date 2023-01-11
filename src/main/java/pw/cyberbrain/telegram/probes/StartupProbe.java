package pw.cyberbrain.telegram.probes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StartupProbe {
    @GetMapping
    public ResponseEntity<String> startupProbe() {
        return new ResponseEntity<>("{\"status\":\"UP\"}", HttpStatus.OK);
    }
}

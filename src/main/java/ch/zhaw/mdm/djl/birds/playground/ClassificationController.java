package ch.zhaw.mdm.djl.birds.playground;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ClassificationController {

    private Inference inference;

    @PostConstruct
    public void init() {
        try {
            inference = new Inference();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren von Inference", e);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "Classification app is up and running!";
    }

    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws Exception {
        System.out.println(image);
        return inference.predict(image.getBytes()).toJson();
    }

}
package e.group.thesismanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThesismanagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ThesismanagerApplication.class, args);
        System.out.println(" Please open: \nhttp://localhost:8080/\n");
    }
}
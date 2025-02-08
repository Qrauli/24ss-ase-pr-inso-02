package at.ase.respond.datafeeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DatafeederApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatafeederApplication.class, args);
    }
}

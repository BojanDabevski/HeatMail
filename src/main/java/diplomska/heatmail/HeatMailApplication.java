package diplomska.heatmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"diplomska.heatmail.repository","diplomska.heatmail.api","diplomska.heatmail.config",
        "diplomska.heatmail.dto", "diplomska.heatmail.model","diplomska.heatmail.service"})
@SpringBootApplication
public class HeatMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeatMailApplication.class, args);
    }

}

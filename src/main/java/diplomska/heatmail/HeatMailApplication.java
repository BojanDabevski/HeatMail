package diplomska.heatmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


//@ComponentScan(basePackages = {"diplomska.heatmail.repository","diplomska.heatmail.api","diplomska.heatmail.config",
//        "diplomska.heatmail.dto", "diplomska.heatmail.model","diplomska.heatmail.service"})
@SpringBootApplication
@ServletComponentScan
public class HeatMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeatMailApplication.class, args);
    }


}

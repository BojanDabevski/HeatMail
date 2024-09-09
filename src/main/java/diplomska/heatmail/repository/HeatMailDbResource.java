package diplomska.heatmail.repository;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class HeatMailDbResource extends RouteBuilder {

    @Qualifier("heatMailDataSource")
    @Autowired
    DataSource heatMailDataSource;

    @Override
    public void configure() throws Exception {


    }
}

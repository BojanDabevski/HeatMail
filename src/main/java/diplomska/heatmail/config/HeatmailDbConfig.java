package diplomska.heatmail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@Primary
@Component
public class HeatmailDbConfig {

    @Bean("heatMailDataSource")
    @ConfigurationProperties(prefix = "heatmail.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}

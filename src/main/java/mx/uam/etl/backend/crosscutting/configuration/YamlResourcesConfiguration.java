package mx.uam.etl.backend.crosscutting.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Data;

@Configuration
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "variables")
@Data
public class YamlResourcesConfiguration {

	private String databaseHost;
	private String databaseUser;
	private String databasePassword;

}

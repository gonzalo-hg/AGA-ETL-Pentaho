package mx.uam.springboot.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import mx.uam.springboot.app.negocio.errores.StorageProperties;
import mx.uam.springboot.app.negocio.interfaces.StorageService;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MongoProjectEjemploApplication {

	public static void main(String[] args){
		SpringApplication.run(MongoProjectEjemploApplication.class, args);		
	}
	
	/**
	 * Elimina todos los archivos que existen en la carpeta "upload-dir" e inicializa el storageService
	 * @param storageService
	 * @return
	 */
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}

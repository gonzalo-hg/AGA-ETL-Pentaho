package mx.uam.etl.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import mx.uam.etl.backend.business.PentahoETLService;
import mx.uam.etl.backend.business.configuration.StorageProperties;
import mx.uam.etl.backend.business.interfaces.StorageService;

import javax.annotation.PostConstruct;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ExtractAndLoadFilesApplication {
	
	@Autowired
	PentahoETLService service;

	public static void main(String[] args){
		SpringApplication.run(ExtractAndLoadFilesApplication.class, args);		
	}
	
	/*
	@PostConstruct
	public void prueba() {
		try {
			this.service.prueba();
		} catch (KettleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
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

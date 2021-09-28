package mx.uam.springboot.app.negocio;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PentahoETLService {
	
	public void extractAndLoadAGA(String filePath) {
		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");
		
		try {
			//Se agregan los folders que contienen plugins a utilizar durante el step
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/pentaho", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/classes", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
			KettleEnvironment.init();
			//Se define transMeta con la ruta de la transformación con extensión .ktr
			TransMeta transMeta = new TransMeta("transform1.ktr");
			Trans trans = new Trans(transMeta);
			//Se comparte un parámetro con los metadatos de la transformación
			trans.shareVariablesWith(transMeta);
			//El parámetro compartido es la ruta del AGA.BFD que se va a tomar para la extracción de datos
			transMeta.setParameterValue("pathName", filePath);
			trans.setLogLevel(LogLevel.ERROR);
			trans.execute(null);
			trans.waitUntilFinished();
			log.info("Extracción y carga de AGA.DBF ha concluido");

			if (trans.getErrors() > 0) {
				log.info("Ocurrió un error durante la extracción y carga de AGA.DBF");
			}
		} catch (KettleException e) {
			e.printStackTrace();
		}
		
	}

}

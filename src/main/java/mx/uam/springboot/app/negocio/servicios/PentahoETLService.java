package mx.uam.springboot.app.negocio.servicios;

import java.util.List;

import org.pentaho.di.core.KettleEnvironment;
import mx.uam.springboot.app.negocio.modelo.FileDataDto;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PentahoETLService {

	/**
	 * Se encarga de ejecutar las rutinas (jobs) de Pentaho correspondientes a los archivos que subió el usuario al directorio upload-dir desde el frontend
	 * @param filesData Lista que contiene todas las variables de control que proporcionan información de los archivos cargados (llave del archivo, nombre real del archivo cargado y extensión requerida para el archivo)
	 * @param uploadPathDir La ruta del directorio que contiene los archivos (se ha especificado que el nombre del directorio de archivos sea "upload-dir")
	 */
	public void extractAndLoadFiles(List<FileDataDto> filesData, String uploadPathDir) {
		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");
		
		try {
			//Se agregan los folders que contienen plugins a utilizar durante el step
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/pentaho", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/classes", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
			KettleEnvironment.init();
			//Se define transMeta con la ruta de la transformación con extensión .ktr
			JobMeta jobMeta = new JobMeta("JOBBD/JOBunirbasededatos.kjb",null);
			Job job = new Job(null,jobMeta);
			job.shareVariablesWith(jobMeta);
			
			int indexHistorialExamenes = 1;

			for(FileDataDto f: filesData) {
				System.out.println(f.getValue());
				switch(f.getKey())
				{
				   case "AGA" :
					  jobMeta.setParameterValue("pathAGA", uploadPathDir+"/"+f.getValue());
				      break; 
				   case "Historial de exámenes de recuperación" :
					   if(indexHistorialExamenes<=10) {
						   jobMeta.setParameterValue("pathHEGR"+indexHistorialExamenes, uploadPathDir+"/"+f.getValue());
						   indexHistorialExamenes++;
					   }
				      break;
				   case "Inscritos en ABC" :
					   jobMeta.setParameterValue("pathABC", uploadPathDir+"/"+f.getValue());
				      break;
				   
				   default : 
				      
				}
			}
			job.start();
			job.waitUntilFinished();
			log.info("Extracción y carga de todos los archivos ha concluido");

			if (job.getErrors() > 0) {
				log.info("Ocurrió un error durante la extracción y carga de los archivos");
			}
		} catch (KettleException e) {
			e.printStackTrace();
		}
		
	}

}

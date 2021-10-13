package mx.uam.springboot.app.negocio.servicios;

import java.util.List;

import org.pentaho.di.core.KettleEnvironment;
import mx.uam.springboot.app.negocio.modelo.FileDataDto;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PentahoETLService {
	
	/*public void extractAndLoadAGAantiguo(String filePath) {
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
		
	}*/
	
	public void extractAndLoadAGA(List<FileDataDto> filesData, String uploadPathDir) {
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
			
			System.out.println("uploadPathDir= "+uploadPathDir);
			for(FileDataDto f: filesData) {
				System.out.println(f.getValue());
				switch(f.getKey())
				{
				   case "AGA" :
					  jobMeta.setParameterValue("pathAGA", uploadPathDir+"/"+f.getValue());
				      break; 
				   /*case "Historial de exámenes de recuperación Plan:52" :
					   jobMeta.setParameterValue("pathHEGR1", uploadPathDir+"/"+f.getValue());
				      break; 
				   case "Historial de exámenes de recuperación Plan:53" :
					   jobMeta.setParameterValue("pathHEGR2", uploadPathDir+"/"+f.getValue());
				      break;
				   case "Historial de exámenes de recuperación Plan:54" :
					   jobMeta.setParameterValue("pathHEGR3", uploadPathDir+"/"+f.getValue());
				      break;
				   case "Historial de exámenes de recuperación Plan:55" :
					   jobMeta.setParameterValue("pathHEGR4", uploadPathDir+"/"+f.getValue());
				      break;
				   case "Historial de exámenes de recuperación Plan:56" :
					   jobMeta.setParameterValue("pathHEGR5", uploadPathDir+"/"+f.getValue());
				      break;
				   case "Historial de exámenes de recuperación Plan:57" :
					   jobMeta.setParameterValue("pathHEGR6", uploadPathDir+"/"+f.getValue());
				      break;*/
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
			
			
			
			/*jobMeta.setParameterValue("pathHEGR2", filePath+"/53kar20o.txt");
			jobMeta.setParameterValue("pathHEGR3", filePath+"/54kar20o.txt");
			jobMeta.setParameterValue("pathHEGR4", filePath+"/55kar20o.txt");
			jobMeta.setParameterValue("pathHEGR5", filePath+"/56kar20o.txt");
			jobMeta.setParameterValue("pathHEGR6", filePath+"/57kar20o.txt");
			jobMeta.setParameterValue("pathABC", filePath+"/ins_abc_21i_1abril_1.txt");*/
			//job.getJobMeta().setInternalKettleVariables(job);
			//Se comparte un parámetro con los metadatos de la transformación
			//trans.shareVariablesWith(transMeta);
			//El parámetro compartido es la ruta del AGA.BFD que se va a tomar para la extracción de datos
			//transMeta.setParameterValue("pathName", filePath);
			/*trans.setLogLevel(LogLevel.ERROR);
			trans.execute(null);
			trans.waitUntilFinished();*/
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

package mx.uam.etl.backend.business;

import java.util.List;

import org.pentaho.di.core.KettleEnvironment;
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
import mx.uam.etl.backend.business.model.FileDataDto;
import mx.uam.etl.backend.errormanagement.DabataBaseNotGeneratedException;

@Slf4j
@Service
public class PentahoETLService {

	/**
	 * Se encarga de ejecutar las rutinas (jobs) de Pentaho correspondientes a los
	 * archivos que subió el usuario al directorio upload-dir desde el frontend
	 * 
	 * @param filesData     Lista que contiene todas las variables de control que
	 *                      proporcionan información de los archivos cargados (llave
	 *                      del archivo, nombre real del archivo cargado y extensión
	 *                      requerida para el archivo)
	 * @param uploadPathDir La ruta del directorio que contiene los archivos (se ha
	 *                      especificado que el nombre del directorio de archivos
	 *                      sea "upload-dir")
	 * @throws KettleException
	 */
	public void extractAndLoadFiles(List<FileDataDto> filesData, String uploadPathDir) throws KettleException {
		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");

		// Se agregan los folders que contienen plugins a utilizar durante el step
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/pentaho", false, true));
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/classes", false, true));
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
		KettleEnvironment.init();
		// Se define transMeta con la ruta de la transformación con extensión .ktr
		JobMeta jobMeta = new JobMeta("JOBBD/JOBunirbasededatosBueno.kjb", null);
		Job job = new Job(null, jobMeta);
		job.shareVariablesWith(jobMeta);

		int indexHistorialExamenes = 1;

		for (FileDataDto f : filesData) {
			System.out.println(f.getValue());
			switch (f.getKey()) {
			case "AGA":
				jobMeta.setParameterValue("pathAGA", uploadPathDir + "/" + f.getValue());
				break;
			case "Historial de exámenes de recuperación":
				if (indexHistorialExamenes <= 10) {
					jobMeta.setParameterValue("pathHEGR" + indexHistorialExamenes, uploadPathDir + "/" + f.getValue());
					indexHistorialExamenes++;
				}
				break;
			case "Inscritos en ABC":
				jobMeta.setParameterValue("pathABC", uploadPathDir + "/" + f.getValue());
				break;
			default:

			}
		}
		job.start();
		job.waitUntilFinished();
		log.info("Extracción y carga de todos los archivos ha concluido");

		if (job.getErrors() > 0) {
			log.error("Ocurrió un error durante la extracción y carga de los archivos");

			throw new DabataBaseNotGeneratedException();
		}

	}

	/**
	 * Se encarga de ejecutar las rutinas (jobs) de Pentaho correspondientes a los
	 * archivos que subió el usuario al directorio upload-dir desde el frontend
	 * 
	 * @param filesData     Lista que contiene todas las variables de control que
	 *                      proporcionan información de los archivos cargados (llave
	 *                      del archivo, nombre real del archivo cargado y extensión
	 *                      requerida para el archivo)
	 * @param uploadPathDir La ruta del directorio que contiene los archivos (se ha
	 *                      especificado que el nombre del directorio de archivos
	 *                      sea "upload-dir")
	 * @throws KettleException
	 */
	public void extractAndLoadAnnualFiles(List<FileDataDto> filesData, String uploadPathDir) throws KettleException {
		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");

		// Se agregan los folders que contienen plugins a utilizar durante el step
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/pentaho", false, true));
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/classes", false, true));
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
		KettleEnvironment.init();
		// Se define transMeta con la ruta de la transformación con extensión .ktr
		JobMeta jobMeta = new JobMeta("JOBBD/jobParaAGAs.kjb", null);
		Job job = new Job(null, jobMeta);
		jobMeta.setParameterValue("pathFolder", uploadPathDir + "/" );
		job.shareVariablesWith(jobMeta);
		log.info("jobParaAGAs.kjb empieza");
		job.start();
		job.waitUntilFinished();
		log.info("Extracción y carga de todos los archivos ha concluido");

		if (job.getErrors() > 0) {
			log.error("Ocurrió un error durante la extracción y carga de los archivos");

			throw new DabataBaseNotGeneratedException();
		}

	}

	public void prueba() throws KettleException {
		log.info("Ejecutando prueba()");

		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");

		try {
			// Se agregan los folders que contienen plugins a utilizar durante el step
			StepPluginType.getInstance().getPluginFolders()
					.add(new PluginFolder("pentahoPlugins/pentaho", false, true));
			StepPluginType.getInstance().getPluginFolders()
					.add(new PluginFolder("pentahoPlugins/classes", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
			KettleEnvironment.init();
			// Se define transMeta con la ruta de la transformación con extensión .ktr
			TransMeta transMeta = new TransMeta("JOBBD/IAGA.ktr");
			Trans trans = new Trans(transMeta);
			// Se comparte un parámetro con los metadatos de la transformación
			trans.shareVariablesWith(transMeta);
			// El parámetro compartido es la ruta del AGA.BFD que se va a tomar para la
			// extracción de datos
			transMeta.setParameterValue("pathAGA", "upload-dir/aga_lic_2021I_izt_4a_sem.DBF");
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
	
	public void prueba2() throws KettleException {
		log.info("Ejecutando prueba()");

		log.info("Ejecutando pentahoETLService.extractAndLoadAga()");

		try {
			// Se agregan los folders que contienen plugins a utilizar durante el step
			StepPluginType.getInstance().getPluginFolders()
					.add(new PluginFolder("pentahoPlugins/pentaho", false, true));
			StepPluginType.getInstance().getPluginFolders()
					.add(new PluginFolder("pentahoPlugins/classes", false, true));
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("pentahoPlugins/libswt", false, true));
			KettleEnvironment.init();
			// Se define transMeta con la ruta de la transformación con extensión .ktr
			TransMeta transMeta = new TransMeta("JOBBD/IAGA.ktr");
			Trans trans = new Trans(transMeta);
			// Se comparte un parámetro con los metadatos de la transformación
			trans.shareVariablesWith(transMeta);
			// El parámetro compartido es la ruta del AGA.BFD que se va a tomar para la
			// extracción de datos
			transMeta.setParameterValue("pathAGA", "upload-dir/aga_lic_2021I_izt_4a_sem.DBF");
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

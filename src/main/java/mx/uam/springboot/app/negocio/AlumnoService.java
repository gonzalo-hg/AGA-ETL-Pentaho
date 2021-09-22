package mx.uam.springboot.app.negocio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;




@Service
public class AlumnoService {

	public void findByMatricula() {
		System.out.println("Servicio");
		System.out.println("Pentaho Service");
		try {
			 StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("/home/antonio/Descargas/PDI7/data-integration/system/karaf/system/pentaho", false, true));
			  StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("/home/antonio/Descargas/PDI7/data-integration/classes", false, true));
			  StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("/home/antonio/Descargas/PDI7/data-integration/libswt", false, true));
			KettleEnvironment.init();
			TransMeta transMeta = new TransMeta("AGAver7.ktr");
			Trans trans = new Trans(transMeta);
			trans.setLogLevel(LogLevel.ERROR);
			trans.execute(null);
			trans.waitUntilFinished();

			if (trans.getErrors() > 0) {
				System.out.println("Ocurrio un error");
			}
		} catch (KettleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

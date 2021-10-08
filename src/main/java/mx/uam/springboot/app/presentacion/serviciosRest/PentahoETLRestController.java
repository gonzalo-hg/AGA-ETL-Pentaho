package mx.uam.springboot.app.presentacion.serviciosRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mx.uam.springboot.app.negocio.PentahoETLService;

@Slf4j
@RestController
public class PentahoETLRestController {
	
	@Autowired
	private PentahoETLService pentahoETLService;
	
	
	@PostMapping("/pentahoetl/aga")
	public void extractAndLoadAGA() {
		log.info("Ejecutando pentahoETLRestController.extractAndLoadAGA()");
		//pentahoETLService.extractAndLoadAGA("/Users/priscilianojimenez/Documents/Ulises/21-P/Proyecto terminal/aga_lic_2021I_izt_4a_sem.DBF");
	}
	
}

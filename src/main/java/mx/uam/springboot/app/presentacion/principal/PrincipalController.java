package mx.uam.springboot.app.presentacion.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mx.uam.springboot.app.negocio.PentahoETLService;
import mx.uam.springboot.app.presentacion.serviciosRest.PentahoETLRestController;

/**
 * 
 * Controlador principal
 * 
 *
 */
@Slf4j
@Controller
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class PrincipalController {
	
	@Autowired
	private PentahoETLService pentahoETLService;
	
	/**
	 * Este método está mapeado a la raíz del sitio
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/")
    public String getAgregarUsuario(Model model) {
		// Redirige a la vista principal
    	return "vistaPrincipal/Principal";
    }
	
	@GetMapping("/cargarArchivo")
    public String cargarArchivo(Model model) {       
		// Redirige a la vista principal
    	return "vistaCargaBD/CargarAGA";
    }
	
	@PostMapping("/cargarArchivo")
    public String cargarAGA(@ModelAttribute("path") String string) {
		log.info("Ejecutando pentahoETLRestController.extractAndLoadAGA()");
		log.info("Ruta del archivo: " + string);
		pentahoETLService.extractAndLoadAGA("Archivos/");
		return "vistaPrincipal/Principal";
    }
	
	@GetMapping("/generaReporte")
    public String generaReporte(Model model) {       
		// Redirige a la vista principal
    	return "vistaGeneraReporte/GeneraReporteExito";
    }


}

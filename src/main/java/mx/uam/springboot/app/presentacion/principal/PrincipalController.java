package mx.uam.springboot.app.presentacion.principal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import lombok.extern.slf4j.Slf4j;
import mx.uam.springboot.app.negocio.PentahoETLService;

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
	@GetMapping("/principal")
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
		//pentahoETLService.extractAndLoadAGA(string);
		return "vistaPrincipal/Principal";
    }
	
	@GetMapping("/generaReporte")
    public String generaReporte(Model model) {       
		// Redirige a la vista principal
    	return "vistaGeneraReporte/GeneraReporteExito";
    }
	
	@RequestMapping("/file/{fileName:.+}")
    public void downloadFileResource( HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     @PathVariable("fileName") String fileName) 
    {
        //If user is not authorized - he should be thrown out from here itself
         
        //Authorized user will download the file
        String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/");
        Path file = Paths.get(dataDirectory, fileName);
        if (Files.exists(file)) 
        {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}

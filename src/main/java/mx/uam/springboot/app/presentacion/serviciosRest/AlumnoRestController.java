package mx.uam.springboot.app.presentacion.serviciosRest;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import mx.uam.springboot.app.negocio.AlumnoService;
//import mx.uam.springboot.app.negocio.modelo.Alumno;
//port mx.uam.springboot.app.negocio.modelo.dto.AlumnoDto;

@RestController
public class AlumnoRestController {
	
	@Autowired
	private AlumnoService alumnoService;
	
	
	@GetMapping("/alumnos/matricula")
	public void findByMatricula() {
		System.out.println("Controller");
		alumnoService.findByMatricula();
		//return alumnoRepository.findByMAT(matricula);
	}
	
	
}

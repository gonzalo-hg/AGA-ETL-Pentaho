package mx.uam.springboot.app.presentacion.fileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.uam.springboot.app.negocio.errores.StorageFileNotFoundException;
import mx.uam.springboot.app.negocio.interfaces.StorageService;
import mx.uam.springboot.app.negocio.modelo.FileDataDto;
import mx.uam.springboot.app.negocio.servicios.PentahoETLService;
import mx.uam.springboot.app.presentacion.principal.PrincipalController;

@Slf4j
@Controller
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class FileUploadController {

	private final StorageService storageService;
	private final List<FileDataDto> fileDataList = new LinkedList<FileDataDto>();
	
	@Autowired
	private PentahoETLService pentahoETLService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}
		
	/**
	 * 
	 * @return Una lista con los nombres de todos los archivos que se han subido
	 */
	@GetMapping(path="/files",
			    produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> listUploadedFiles() {
		
		List<String> fileNames = storageService.loadAll().map(
				path -> path.getFileName().toString())
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(fileNames);
	}
	
	/**
	 * Cargar el archivo (si existe) y se lo envía al navegador para descarga
	 * @param filename El nombre del archivo a cargar que será enviado al navegador
	 * @return Un response header tipo CONTENT_DISPOSITION que contiene el archivo de respuesta
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	/**
	 * Maneja la subida de un archivo al navegador. El archivo es almacenado en el directorio "upload-dir" .
	 * @param file El archivo obtenido del navegador
	 * @param redirectAttributes
	 * @return ResponseEntity.ok("")
	 */
	@PostMapping(path = "/")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		
		try {
			storageService.store(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded " + file.getOriginalFilename() + "!");
			
			//Invocar al ETL Pentaho
			List<String> fileNames = storageService.loadAll().map(
					path -> path.getFileName().toString())
					.collect(Collectors.toList());
			
			if(!fileNames.isEmpty()) {
				String filePath = storageService.getRootLocation()+"/"+fileNames.get(0);
			}

			return ResponseEntity.ok("");
		} catch (Exception e) {
			HttpStatus status;
			if(e instanceof IllegalArgumentException) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			throw new ResponseStatusException(status, e.getMessage());
		}
	}
	
	/**
	 * Recibe la información de los archivos que se están subiendo al navegador. Guarda esta información en una variable de control local que es una lista de tipo FileDataDto
	 * @param newData El objeto llave-valor con la información del archivo que se ha subido
	 * @return
	 */
	@PostMapping(path = "/fileData", consumes =
			MediaType.APPLICATION_JSON_VALUE, produces =
			MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<FileDataDto> addFileData(@RequestBody FileDataDto newData){
			try {
				//FileDataDto nuevo = FileDataDto.creaDto(null, null)
				this.fileDataList.add(newData);
				System.out.println();
				return ResponseEntity.status(HttpStatus.CREATED).body(newData);
			} catch(Exception ex) {
				HttpStatus status;
				if(ex instanceof IllegalArgumentException) {
					status = HttpStatus.BAD_REQUEST;
				} else {
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
				throw new ResponseStatusException(status, ex.getMessage());
				
			}
	}
	
	/**
	 * Genera una nueva base de datos Mongo a partir de los archivos subidos al servidor
	 * @return
	 */
	@PostMapping("/generarbd")
	public String generabd() {

		pentahoETLService.extractAndLoadAGA(this.fileDataList, storageService.getRootLocation());
		return "redirect:/";
	}
	

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
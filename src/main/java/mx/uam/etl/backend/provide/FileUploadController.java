package mx.uam.etl.backend.provide;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import mx.uam.etl.backend.business.PentahoETLService;
import mx.uam.etl.backend.business.interfaces.StorageService;
import mx.uam.etl.backend.business.model.FileDataDto;
import mx.uam.etl.backend.business.model.ResponseTransfer;
import mx.uam.etl.backend.errormanagement.ErrorManager;
import mx.uam.etl.backend.errormanagement.StorageFileNotFoundException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class FileUploadController {

	private final StorageService storageService;
	private final List<FileDataDto> fileDataList = new LinkedList<FileDataDto>();

	@Autowired
	private PentahoETLService pentahoETLService;
	
	@Autowired
	private ErrorManager errorManager;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	/**
	 * 
	 * @return Una lista con los nombres de todos los archivos que se han subido
	 */
	@GetMapping(path = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> listUploadedFiles() {

		List<String> fileNames = storageService.loadAll().map(path -> path.getFileName().toString())
				.collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileNames);
	}

	/**
	 * Carga en memoria el archivo con el nombre recibido (si existe) y se lo envía
	 * al navegador para descarga
	 * 
	 * @param filename El nombre del archivo a cargar que será enviado al navegador
	 * @return Un response header tipo CONTENT_DISPOSITION que contiene el archivo
	 *         de respuesta
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	/**
	 * Elimina el archivo con el nombre dado del directorio upload-dir 
	 * @param filename El nombre del archivo que se quiere eliminar
	 * @return
	 */
	@DeleteMapping(path = "/files/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseTransfer> deleteFile(@PathVariable String filename) {

		try {
			storageService.deleteFile(filename);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseTransfer("No se ha encontrado un archivo con el nombre '" + filename
							+ "' dentro del directorio de archivos del servidor"));
		}
		//Se elimina el fileData del archivo que se eliminó
		FileDataDto fileToRemove = new FileDataDto();
		if (!this.fileDataList.isEmpty()) {
			for(FileDataDto fileData: this.fileDataList) {
				if(fileData.getValue().equals(filename)) 
					fileToRemove = fileData;
			}
			this.fileDataList.remove(fileToRemove);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseTransfer("¡El archivo '" + filename + "' se ha eliminado con éxito!"));
	}
	
	/**
	 * Elimina todos los archivos existentes en el directorio upload-dir 
	 * @return
	 */
	@DeleteMapping(path = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseTransfer> deleteAll() {
		this.storageService.deleteAll();
		this.fileDataList.clear();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseTransfer("Se han eliminado todos los archivos del servidor"));
	}

	
	/**
	 * 
	 * @param file              El archivo obtenido del navegador
	 * @param fileKey           La 'llave' o 'customTitle' del componente
	 *                          file-upload. Esta llave es una etiqueta para el
	 *                          archivo asignada por el equipo de desarrollo.
	 * @param fileValue         El nombre original del archivo que cargó el usuario
	 * @param requiredExtension
	 * @return HttpStatus.OK cuando el archivo se ha cargado exitosamente; ResponseStatusException en caso contrario
	 */
	@PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseTransfer> handleFileUpload(
			@RequestParam("file") MultipartFile file,
			@RequestParam("key") String fileKey, 
			@RequestParam("value") String fileValue,
			@RequestParam("requiredExtension") String requiredExtension) {

		/**
		 * Se verifica que la extensión requerida del archivo en el frontend sea la
		 * misma que la extensión del archivo que cargó el usuario
		 */
		String extension = "";
		switch (requiredExtension) {
		case ".txt":
			extension = "text/plain";
			break;
		case ".DBF":
			extension = "application/octet-stream";
			break;
		default:
			extension = "";
			break;
		}
		System.out.println("Nombre: " + file.getOriginalFilename());
		System.out.println("Extension: " + file.getContentType());
		if (file.getContentType().contentEquals(extension)) {
			try {
				// Guarda todos los nombres de todos los archivos que existen en la carpeta
				// upload-dir en una lista local
				List<String> fileNames = storageService.loadAll().map(path -> path.getFileName().toString())
						.collect(Collectors.toList());
				// Se verifica que no exista un archivo con el mismo nombre que el archivo que
				// se intenta cargar
				if (!fileNames.contains(fileValue)) {
					// Almacena el archivo en la carpeta upload-dir
					storageService.store(file);
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseTransfer("Ya existe un archivo con el nombre '" + fileValue
									+ "' almacenado en el servidor. Intente subir otro archivo con un nombre distinto"));
				}
				// Se actualiza la lista con los nombres de los archivos
				fileNames = storageService.loadAll().map(path -> path.getFileName().toString())
						.collect(Collectors.toList());
				// Comprueba que el archivo que se acaba de subir se encuentre en la lista
				// actualizada para enviar al frontend una respuesta de éxito
				if (fileNames.contains(fileValue)) {
					// Se registra la información de ese archivo en una variable de control
					// almacenada localmente en una lista
					this.fileDataList.add(new FileDataDto(fileKey, fileValue, requiredExtension));
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseTransfer(
							"¡El archivo '" + file.getOriginalFilename() + "' ha sido cargado al servidor con éxito!"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				HttpStatus status;
				if (e instanceof IllegalArgumentException) {
					status = HttpStatus.BAD_REQUEST;
				} else {
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
				throw new ResponseStatusException(status, e.getMessage());
			}

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTransfer(
					"El archivo " + file.getOriginalFilename() + " no cumple con la extensión requerida"));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseTransfer("Ha ocurrido un error en el servidor"));
	}

	/**
	 * Genera una nueva base de datos Mongo a partir de los archivos subidos al
	 * servidor
	 * 
	 * @return
	 */
	/*@PostMapping("/generateDB")
	public ResponseEntity<ResponseTransfer> generateDB() {
		
		try {
			pentahoETLService.extractAndLoadFiles(this.fileDataList, storageService.getRootLocation());
			return  ResponseEntity.status(HttpStatus.OK).body(new ResponseTransfer(
					"La base de datos se ha generado con éxito"));
		} catch (KettleException e) {
			throw new ResponseStatusException(errorManager.getHttpStatusForException(e), "Ha ocurrido una excepción durante la ingesta de archivos: "+e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(errorManager.getHttpStatusForException(e), "Ha ocurrido un error: "+e.getMessage(), e);
		}
	}*/
	
	/**
	 * Genera una nueva base de datos Mongo a partir de los archivos subidos al
	 * servidor
	 * 
	 * @return
	 */
	@PostMapping("/generateAnnualDB")
	public ResponseEntity<ResponseTransfer> generateAnnualDB() {
		
		try {
			pentahoETLService.extractAndLoadAnnualFiles(this.fileDataList, storageService.getRootLocation());
			return  ResponseEntity.status(HttpStatus.OK).body(new ResponseTransfer(
					"La base de datos se ha generado con éxito"));
		} catch (KettleException e) {
			throw new ResponseStatusException(errorManager.getHttpStatusForException(e), "Ha ocurrido una excepción durante la ingesta de archivos: "+e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(errorManager.getHttpStatusForException(e), "Ha ocurrido un error: "+e.getMessage(), e);
		}
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
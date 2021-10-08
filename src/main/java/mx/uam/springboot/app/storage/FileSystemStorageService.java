package mx.uam.springboot.app.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import mx.uam.springboot.app.presentacion.principal.PrincipalController;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}
	

	@Override
	public void store(MultipartFile file) {
		log.info("Ejecutando fileSystemStorageService.store()");
		log.info("Guardando archivo: "+file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				log.info("Ruta destino = "+destinationFile);
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
				
				boolean copyCompleted = false;
				//Asegura que el archivo se haya subido al servidor satisfactoriamente
				/*while(true) {
		            
		            RandomAccessFile ran = null;
		            try {
		                ran = new RandomAccessFile(file, "rw");
		                copyCompleted = true;
		                break;
		            } catch (Exception ex) {
		                log.info("El archivo aún está cargándose" + ex.getMessage());
		            } finally {
		                if(ran != null) try {
		                    ran.close();
		                } catch (IOException ex) {
		                     
		                }
		                ran = null;
		            }
		 
		        }
		        
		       if(copyCompleted){
		           log.info("Carga de "+file.getOriginalFilename()+" completada");
		       }*/
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}


	@Override
	public String getRootLocation() {
		return this.rootLocation.toString();
	}
	
	
}
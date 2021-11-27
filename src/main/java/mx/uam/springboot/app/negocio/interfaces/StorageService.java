package mx.uam.springboot.app.negocio.interfaces;

import org.apache.commons.vfs2.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import mx.uam.springboot.app.negocio.recursos.StorageFileNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();
	
	String getRootLocation();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename) throws StorageFileNotFoundException;
	
	void deleteFile(String filename) throws IOException;

	void deleteAll();

}